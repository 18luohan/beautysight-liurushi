/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo;

import com.beautysight.liurushi.common.domain.Range;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.mongodb.WriteResult;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @author chenlong
 * @since 1.0
 */
public abstract class AbstractMongoRepository<T> implements MongoRepository<T> {

    private static final String ID_FIELD = "id";
    private static final OrderClause DESC_BY_ID = new OrderClause().addInvertibleDesc(ID_FIELD);

    @Autowired
    protected Datastore datastore;

    @Override
    public <S extends T> S save(S entity) {
        Assert.notNull(entity, "The given entity must not be null!");
        datastore.save(entity);
        return entity;
    }

    @Override
    public void merge(T entity) {
        Assert.notNull(entity, "The given entity must not be null!");
        datastore.merge(entity);
    }

    @Override
    public <S extends T> Iterable<S> save(Iterable<S> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");

        List<S> result = convertIterableToList(entities);
        for (S entity : result) {
            save(entity);
        }
        return result;
    }

    @Override
    public T findOne(String id) {
        return findOne(toMongoId(id));
    }

    @Override
    public T findOne(ObjectId id) {
        Assert.notNull(id, "The given id must not be null!");
        return datastore.get(entityClass(), id);
    }

    @Override
    public boolean exists(ObjectId id) {
        Assert.notNull(id, "The given id must not be null!");
//        Key<?> key = datastore.exists(new Key<T>(entityClass(), id));
//        // TODO 需要确认这样判断是否存在靠不靠谱？
//        return (key != null);
        return false;
    }

    /**
     * Return all documents in a collection, so that could be costly with large result sets.
     *
     * @return
     */
    @Override
    public Iterable<T> findAll() {
        return datastore.find(entityClass()).asList();
    }

    @Override
    public Iterable<T> findAll(Iterable<ObjectId> ids) {
        if (isNullOrEmpty(ids)) {
            return Collections.emptyList();
        }
        return datastore.createQuery(entityClass()).field("id").in(ids).asList();
    }

    @Override
    public long count() {
        return datastore.getCount(entityClass());
    }

    @Override
    public void delete(ObjectId id) {
        Assert.notNull(id, "The given id must not be null!");
        WriteResult result = datastore.delete(entityClass(), id);
        checkWriteResult(Op.DELETE, result, 1);
    }

    @Override
    public int remove(String id) {
        Preconditions.checkArgument(StringUtils.isNotBlank(id), "The given id must not be blank!");
        WriteResult result = datastore.delete(entityClass(), toMongoId(id));
        return result.getN();
    }

    /**
     * Deletes the given entity (by @Id)
     */
    @Override
    public void delete(T entity) {
        Assert.notNull(entity, "The given entity must not be null!");
        WriteResult result = datastore.delete(entity);
        checkWriteResult(Op.DELETE, result, 1);
    }

    /**
     * Deletes the given entities (by @Id)
     *
     * @param entities
     */
    @Override
    public void delete(Iterable<? extends T> entities) {
        Preconditions.checkArgument(!isNullOrEmpty(entities), "The given entities must not be null or empty!");

        for (T entity : entities) {
            delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        datastore.delete(datastore.createQuery(entityClass()));
    }

    @Override
    public Iterable<T> findAll(Sort sort) {
        Assert.notNull(sort, "The given sort must not be null!");
        return datastore.find(entityClass()).order(orderingClause(sort)).asList();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        Assert.notNull(pageable, "The given pageable must not be null!");
        Long count = count();

        List<T> list = datastore.createQuery(entityClass())
                .order(orderingClause(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .asList();

        return new PageImpl<T>(list, pageable, count);
    }

    protected void checkWriteResult(Op op, WriteResult result, int expected) {
        if (result.getN() != expected) {
            throw new IncorrectOperationResultException(
                    String.format("%s: expected %s, actual %s", op, expected, result.getN()));
        }
    }

    protected String orderingClause(Sort sort) {
        Assert.notNull(sort, "The given sort must not be null");

        Iterator<Sort.Order> it = sort.iterator();
        StringBuilder orderBy = new StringBuilder();
        while (it.hasNext()) {
            Sort.Order order = it.next();
            if (order.getDirection() == Sort.Direction.DESC) {
                orderBy.append("-");
            }
            orderBy.append(order.getProperty());
            if (it.hasNext()) {
                orderBy.append(", ");
            }
        }

        Assert.hasLength(orderBy.toString(), "Must specify at least one sorting field");
        return orderBy.toString();
    }

    protected List<T> findInOrderById(Optional<Conditions> conditions, Range range) {
        return findInOrderById(conditions, range, Optional.<FieldsFilter>absent());
    }

    protected List<T> findInOrderById(Optional<Conditions> conditions, Range range, Optional<FieldsFilter> fieldsFilter) {
        if (range.referencePoint().isPresent()) {
            range.setCursor(ID_FIELD, toMongoId(range.referencePoint().get()));
        }
        return this.find(conditions, range, DESC_BY_ID, fieldsFilter);
    }

    protected List<T> find(Optional<Conditions> conditions, Range range,
                           OrderClause orderClauseForBefore, Optional<FieldsFilter> fieldsFilter) {
        Preconditions.checkArgument(range.offset() > 0, "Assert range.offset() > 0");

        if (!range.cursor().isPresent()) {
            return findLatest(conditions, range.offset(), orderClauseForBefore, fieldsFilter);
        }

        Range.Cursor cursor = range.cursor().get();
        List<T> result = new ArrayList<>();
        if (range.both() || range.after()) {
            Query<T> query = newQuery(conditions)
                    .field(cursor.primaryOrderField()).greaterThan(cursor.fieldVal())
                    .order(orderClauseForBefore.inverseOrderClause()) // 升序
                    .limit(range.offset());
            filterFields(query, fieldsFilter);
            List<T> ascendingList = query.asList();
            if (!CollectionUtils.isEmpty(ascendingList)) {
                // 降序排列
                Collections.reverse(ascendingList);
                result.addAll(ascendingList);
            }
        }

        if (range.both() || range.before()) {
            Query<T> query = newQuery(conditions)
                    .field(cursor.primaryOrderField()).lessThanOrEq(cursor.fieldVal())
                    .order(orderClauseForBefore.orderClause()) // 降序
                    .limit(range.offset() + 1); // 目前morphia组件还不支持$natural查询修饰符
            filterFields(query, fieldsFilter);
            List<T> descendingList = query.asList();
            if (!CollectionUtils.isEmpty(descendingList)) {
                // 只有both需要将参考点数据作为结果的一部分返回
                if (range.before()) {
                    result.addAll(descendingList.subList(1, descendingList.size()));
                } else {
                    result.addAll(descendingList);
                }
            }
        }

        return result;
    }

    private List<T> findLatest(Optional<Conditions> conditions, int count, OrderClause orderClauseForBefore, Optional<FieldsFilter> fieldsFilter) {
        Query<T> query = newQuery(conditions).order(orderClauseForBefore.orderClause()).limit(count);
        filterFields(query, fieldsFilter);
        return query.asList();
    }

    protected List<T> findBy(Conditions conditions) {
        return newQuery(conditions).asList();
    }

    protected Optional<T> findOneBy(Conditions conditions) {
        return findOneBy(newQuery(conditions));
    }

    protected Optional<T> findOneBy(Query<T> query) {
//        long count = query.countAll();
//        if (count > 1) {
//            throw new DuplicateEntityException(String.format(
//                    "Expected 1 %s, but actual %s, conditions: %s",
//                    entityClass().getSimpleName(), count, conditions));
//        }
//        if (count == 0) {
//            return Optional.absent();
//        }
        return Optional.fromNullable(query.get());
    }

    protected Query<T> newQuery() {
        return datastore.createQuery(entityClass());
    }

    protected Query<T> newQuery(Optional<Conditions> conditions) {
        if (conditions.isPresent()) {
            return newQuery(conditions.get());
        }
        return newQuery();
    }

    protected Query<T> newQuery(Conditions conditions) {
        Assert.notNull(conditions, "conditions must not be null");
        Query<T> query = newQuery();
        Iterator<Conditions.Condition> it = conditions.iterator();
        while (it.hasNext()) {
            Conditions.Condition condition = it.next();
            if (condition.op == Conditions.Operator.eq) {
                query.field(condition.field).equal(condition.val);
            } else if (condition.op == Conditions.Operator.gte) {
                query.field(condition.field).greaterThanOrEq(condition.val);
            } else if (condition.op == Conditions.Operator.lt) {
                query.field(condition.field).lessThan(condition.val);
            } else if (condition.op == Conditions.Operator.lte) {
                query.field(condition.field).lessThanOrEq(condition.val);
            } else if (condition.op == Conditions.Operator.gt) {
                query.field(condition.field).greaterThan(condition.val);
            } else if (condition.op == Conditions.Operator.containAll) {
                query.field(condition.field).hasAllOf((Iterable) condition.val);
            } else {
                throw new IllegalStateException("Illegal query operator:" + condition.op);
            }
        }
        return query;
    }

    protected UpdateOperations<T> newUpdateOps() {
        return datastore.createUpdateOperations(entityClass());
    }

    protected ObjectId toMongoId(String id) {
        return new ObjectId(id);
    }

    protected List<ObjectId> toMongoIds(Collection<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }

        List<ObjectId> mongoIds = new ArrayList<>(ids.size());
        for (String id : ids) {
            mongoIds.add(toMongoId(id));
        }
        return mongoIds;
    }

    protected abstract Class<T> entityClass();

    private static boolean isNullOrEmpty(Iterable<?> iterable) {
        if (iterable == null) {
            return true;
        }

        if (iterable instanceof Collection) {
            return CollectionUtils.isEmpty((Collection<?>) iterable);
        }

        return false;
    }

    private static <T> List<T> convertIterableToList(Iterable<T> entities) {
        if (entities instanceof List) {
            return (List<T>) entities;
        }

        int capacity = tryDetermineRealSizeOrReturn(entities, 10);
        if (capacity == 0 || entities == null) {
            return Collections.emptyList();
        }

        List<T> list = new ArrayList<T>(capacity);
        for (T entity : entities) {
            list.add(entity);
        }

        return list;
    }

    private static int tryDetermineRealSizeOrReturn(Iterable<?> iterable, int defaultSize) {
        return iterable == null ? 0 : (iterable instanceof Collection) ? ((Collection<?>) iterable).size() : defaultSize;
    }

    private void filterFields(Query<T> query, Optional<FieldsFilter> filter) {
        if (filter.isPresent()) {
            query.retrievedFields(filter.get().include, filter.get().fields);
        }
    }

    protected enum Op {
        DELETE, UPDATE
    }

    protected static class Conditions {

        private final List<Condition> conditions;

        private Conditions() {
            conditions = new ArrayList<>(5);
        }

        public static Conditions newInstance() {
            return new Conditions();
        }

        public Conditions andEqual(String field, Object val) {
            conditions.add(new Condition(field, Operator.eq, val));
            return this;
        }

        public Conditions andGte(String field, Object val) {
            conditions.add(new Condition(field, Operator.gte, val));
            return this;
        }

        public Conditions andLte(String field, Object val) {
            conditions.add(new Condition(field, Operator.lte, val));
            return this;
        }

        public Conditions andGt(String field, Object val) {
            conditions.add(new Condition(field, Operator.gt, val));
            return this;
        }

        public Conditions andLt(String field, Object val) {
            conditions.add(new Condition(field, Operator.lt, val));
            return this;
        }

        public Conditions andContainAllOf(String field, Iterable<?> values) {
            conditions.add(new Condition(field, Operator.containAll, values));
            return this;
        }

        public Iterator<Condition> iterator() {
            return conditions.iterator();
        }

        public Conditions clone() {
            Conditions newOne = new Conditions();
            for (Condition condition : this.conditions) {
                newOne.conditions.add(condition);
            }
            return newOne;
        }

        @Override
        public String toString() {
            StringBuilder conditionStr = new StringBuilder();
            for (Condition condition : conditions) {
                conditionStr.append(condition.field)
                        .append(" ").append(condition.op)
                        .append(" ").append(condition.val)
                        .append(",");
            }
            return new StringBuilder("{")
                    .append(conditionStr.substring(0, conditionStr.length()))
                    .append("}").toString();
        }

        private static class Condition {
            private String field;
            private Operator op;
            private Object val;

            private Condition(String field, Operator op, Object val) {
                Assert.hasText(field, "field must not be blank");
                Assert.notNull(op, "op must not be null");
                if (val instanceof String) {
                    Assert.hasText((String) val, "val must not be blank");
                } else {
                    Assert.notNull(val, "val must not be null");
                }
                this.field = field;
                this.op = op;
                this.val = val;
            }
        }

        private enum Operator {
            eq, gte, lt, lte, gt, containAll
        }
    }

    public static class Fields {

        private List<String> fields;

        private Fields(int capacity) {
            this.fields = new ArrayList<>(capacity);
        }

        private Fields(List<String> fields) {
            Preconditions.checkArgument((fields != null), "fields is null");
            this.fields = fields;
        }

        public static Fields newInstance(Integer... fieldsCount) {
            int capacity = 10;
            if (!ObjectUtils.isEmpty(fieldsCount)) {
                capacity = fieldsCount[0].intValue();
            }
            return new Fields(capacity);
        }

        public Fields append(String... fieldNames) {
            Assert.notEmpty(fieldNames, "fieldNames must not be blank");
            for (String field : fieldNames) {
                this.fields.add(field);
            }
            return this;
        }

        public Fields copyThenAppend(String... fieldNames) {
            Assert.notEmpty(fieldNames, "fieldNames must not be blank");

            Fields copy = new Fields(new ArrayList<String>());
            copy.fields.addAll(this.fields);
            for (String field : fieldNames) {
                copy.fields.add(field);
            }
            return copy;
        }

        public String[] toArray() {
            Preconditions.checkState(!CollectionUtils.isEmpty(fields), "fields must not be blank");
            return fields.toArray(new String[fields.size()]);
        }

    }

    public static class FieldsFilter {
        private boolean include = true;
        private String[] fields;

        public FieldsFilter(boolean include, String[] fields) {
            this.include = include;
            this.fields = fields;
        }
    }

}
