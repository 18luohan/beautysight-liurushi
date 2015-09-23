/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.community.domain.work.picstory.PictureStory;
import com.beautysight.liurushi.community.domain.work.picstory.PictureStoryRepo;
import com.beautysight.liurushi.community.domain.work.Work;
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
public class PictureStoryRepoImpl extends AbstractMongoRepository<PictureStory> implements PictureStoryRepo {

    public List<PictureStory> getLatestPictureStories(Work.Source source, int count) {
        Query<PictureStory> query = newQuery();
        query.retrievedFields(true, "id", "title", "cover.picture", "authorId", "publishedAt")
                .field("source").equal(source)
                // 目前morphia组件还不支持$natural查询修饰符
                .order("-id").limit(count);
        return query.asList();
    }

    public List<PictureStory> findPictureStoriesInRange(
            Work.Source source, Optional<String> referenceWorkId, int offset, Range.OffsetDirection direction) {
        List<PictureStory> result = new ArrayList<>();

        if (!referenceWorkId.isPresent()) {
            return getLatestPictureStories(source, offset);
        }

        Query<PictureStory> query;
        boolean isReferenceWorkAdded = false;
        if (offset > 0 && (direction == Range.OffsetDirection.both || direction == Range.OffsetDirection.after)) {
            query = newQuery();
            query.retrievedFields(true, "id", "title", "cover.picture", "authorId", "publishedAt")
                    .field("source").equal(source)
                    .field("id").greaterThanOrEq(new ObjectId(referenceWorkId.get()))
                    // 目前morphia组件还不支持$natural查询修饰符
                    .order("id").limit(offset);
            List<PictureStory> ascendingList = query.asList();
            if (CollectionUtils.isNotEmpty(ascendingList)) {
                // 倒序排列
                Collections.reverse(ascendingList);
                result.addAll(ascendingList);
                isReferenceWorkAdded = true;
            }
        }

        if (offset > 0 && (direction == Range.OffsetDirection.both || direction == Range.OffsetDirection.before)) {
            query = newQuery();
            query.retrievedFields(true, "id", "title", "cover.picture", "authorId", "publishedAt")
                    .field("source").equal(source)
                    .field("id").lessThanOrEq(new ObjectId(referenceWorkId.get()))
                    // 目前morphia组件还不支持$natural查询修饰符
                    .order("-id").limit(offset + 1);
            List<PictureStory> descendingList = query.asList();
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
    protected Class<PictureStory> entityClass() {
        return PictureStory.class;
    }

}
