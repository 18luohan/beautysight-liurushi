/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.community.domain.model.work.OffsetDirection;
import com.beautysight.liurushi.community.domain.model.work.Work;
import com.beautysight.liurushi.community.domain.model.work.WorkRepo;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.google.common.base.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
@Repository
public class WorkRepoImpl extends AbstractMongoRepository<Work> implements WorkRepo {

    private static final Fields workBasicFields = Fields.newInstance().copyThenAppend("id", "authorId", "source", "publishedAt");
    private static final String[] workProfileFields = workBasicFields.copyThenAppend("pictureStory.title", "pictureStory.cover.picture").toArray();
    private static final String[] pictureStoryFields = workBasicFields.copyThenAppend("pictureStory").toArray();

    @Override
    public List<Work> getLatestWorks(Work.Source source, int count) {
        Query<Work> query = newQuery();
        query.retrievedFields(true, workProfileFields)
                .field("source").equal(source)
                // 目前morphia组件还不支持$natural查询修饰符
                .order("-id").limit(count);
        return query.asList();
    }

    @Override
    public List<Work> findWorksInRange(Work.Source source,
                                       Optional<String> referenceWorkId, int offset, OffsetDirection direction) {
        List<Work> result = new ArrayList<>();

        if (!referenceWorkId.isPresent()) {
            return getLatestWorks(source, offset);
        }

        Query<Work> query;
        boolean isReferenceWorkAdded = false;
        if (offset > 0 && (direction == OffsetDirection.both || direction == OffsetDirection.after)) {
            query = newQuery();
            query.retrievedFields(true, workProfileFields)
                    .field("source").equal(source)
                    .field("id").greaterThanOrEq(new ObjectId(referenceWorkId.get()))
                    // 目前morphia组件还不支持$natural查询修饰符
                    .order("id").limit(offset);
            List<Work> ascendingList = query.asList();
            if (CollectionUtils.isNotEmpty(ascendingList)) {
                // 倒序排列
                Collections.reverse(ascendingList);
                result.addAll(ascendingList);
                isReferenceWorkAdded = true;
            }
        }

        if (offset > 0 && (direction == OffsetDirection.both || direction == OffsetDirection.before)) {
            query = newQuery();
            query.retrievedFields(true, workProfileFields)
                    .field("source").equal(source)
                    .field("id").lessThanOrEq(new ObjectId(referenceWorkId.get()))
                    // 目前morphia组件还不支持$natural查询修饰符
                    .order("-id").limit(offset + 1);
            List<Work> descendingList = query.asList();
            if (CollectionUtils.isNotEmpty(descendingList)) {
                if (isReferenceWorkAdded) {
                    result.addAll(descendingList.subList(1, descendingList.size()));
                } else {
                    result.addAll(descendingList);
                }
            }
        }

        return result;
    }

    @Override
    public Work getPictureStoryOf(String workId) {
        Query<Work> query = newQuery();
        query.retrievedFields(true, pictureStoryFields)
                .field("id").equal(new ObjectId(workId));
        return query.get();
    }

    @Override
    protected Class<Work> entityClass() {
        return Work.class;
    }

}
