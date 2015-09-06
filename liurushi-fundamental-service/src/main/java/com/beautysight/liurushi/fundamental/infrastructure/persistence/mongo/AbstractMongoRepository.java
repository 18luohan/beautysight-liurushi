/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo;

import com.beautysight.liurushi.common.domain.OffsetDirection;
import com.beautysight.liurushi.common.domain.Range;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.mongodb.WriteResult;
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
        return findOne(new ObjectId(id));
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

    protected List<T> find(Conditions conditions, Range range) {
        return find(conditions, range, Optional.<FieldsFilter>absent());
    }

    protected List<T> findLatest(Conditions conditions, int count, Optional<FieldsFilter> fieldsFilter) {
        Query<T> query = newQuery(conditions)
                .order("-id").limit(count);
        filterFields(query, fieldsFilter);
        return query.asList();
    }

    protected List<T> find(Conditions conditions, Range range, Optional<FieldsFilter> fieldsFilter) {
        Preconditions.checkArgument(range.offset() > 0, "Assert range.offset() > 0");

        if (!range.referencePoint().isPresent()) {
            return findLatest(conditions, range.offset(), fieldsFilter);
        }

        List<T> result = new ArrayList<>();
        if (range.direction() == OffsetDirection.both || range.direction() == OffsetDirection.after) {
            Query<T> query = newQuery(conditions)
                    .field("id").greaterThanOrEq(new ObjectId(range.referencePoint().get()))
                    .order("id").limit(range.offset() + 1);
            filterFields(query, fieldsFilter);
            List<T> ascendingList = query.asList();
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(ascendingList)) {
                // 倒序排列
                Collections.reverse(ascendingList);
                result.addAll(ascendingList);
            }
        }

        if (range.direction() == OffsetDirection.both || range.direction() == OffsetDirection.before) {
            Query<T> query = newQuery(conditions)
                    .field("id").lessThan(new ObjectId(range.referencePoint().get()))
                    .order("-id").limit(range.offset()); // 目前morphia组件还不支持$natural查询修饰符
            filterFields(query, fieldsFilter);
            List<T> descendingList = query.asList();
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(descendingList)) {
                result.addAll(descendingList);
            }
        }

        return result;
    }

    protected List<T> findBy(Conditions conditions) {
        return newQuery(conditions).asList();
    }

    protected Optional<T> findOneBy(Conditions conditions) {
        Query<T> query = newQuery(conditions);
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

    protected Query<T> newQuery(Conditions conditions) {
        Assert.notNull(conditions, "The given conditions must not be null");
        Query<T> query = newQuery();
        Iterator<Conditions.And> it = conditions.iterator();
        while (it.hasNext()) {
            Conditions.And condition = it.next();
            query.field(condition.field).equal(condition.val);
        }
        return query;
    }

    protected UpdateOperations<T> newUpdateOps() {
        return datastore.createUpdateOperations(entityClass());
    }

    protected ObjectId toMongoId(String id) {
        return new ObjectId(id);
    }

    protected List<ObjectId> toMongoIds(List<String> ids) {
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

        private final List<And> conditions;

        private Conditions(String field, Object val) {
            conditions = new ArrayList<>(5);
            conditions.add(new And(field, val));
        }

        public static Conditions of(String field, Object val) {
            return new Conditions(field, val);
        }

        public Conditions and(String field, Object val) {
            conditions.add(new And(field, val));
            return this;
        }

        public Iterator<And> iterator() {
            return conditions.iterator();
        }

        @Override
        public String toString() {
            StringBuilder conditionStr = new StringBuilder("{");
            for (And condition : conditions) {
                conditionStr.append(condition.field)
                        .append(":")
                        .append(condition.val)
                        .append(",");
            }
            return new StringBuilder("{")
                    .append(conditionStr.substring(0, conditionStr.length()))
                    .append("}").toString();
        }

        private static class And {
            private String field;
            private Object val;

            private And(String field, Object val) {
                Assert.hasText(field, "The given field must not be blank");
                if (val instanceof String) {
                    Assert.hasText((String) val, "The given val must not be blank");
                } else {
                    Assert.notNull(val, "The given val must not be null");
                }
                this.field = field;
                this.val = val;
            }
        }

    }

    protected static class Fields {

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

    protected static class FieldsFilter {
        private boolean include = true;
        private String[] fields;

        public FieldsFilter(boolean include, String[] fields) {
            this.include = include;
            this.fields = fields;
        }
    }

}
