/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.community.domain.model.content.PictureStory;
import com.beautysight.liurushi.community.domain.model.content.PictureStoryRepo;
import com.beautysight.liurushi.community.domain.model.content.Work;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
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

    public List<PictureStory> findPictureStoriesInRange(Work.Source source, String referenceWorkId, int offset) {
        List<PictureStory> result = new ArrayList<>();

        Query<PictureStory> query;
        if (offset > 0) {
            query = newQuery();
            query.retrievedFields(true, "id", "title", "cover.picture", "authorId", "publishedAt")
                    .field("source").equal(source)
                    .field("id").greaterThan(new ObjectId(referenceWorkId))
                    // 目前morphia组件还不支持$natural查询修饰符
                    .order("id").limit(offset);
            List<PictureStory> ascendingList = query.asList();
            if (CollectionUtils.isNotEmpty(ascendingList)) {
                // 倒序排列
                Collections.reverse(ascendingList);
                result.addAll(ascendingList);
            }
        }

        query = newQuery();
        query.retrievedFields(true, "id", "title", "cover.picture", "authorId", "publishedAt")
                .field("source").equal(source)
                .field("id").lessThanOrEq(new ObjectId(referenceWorkId))
                // 目前morphia组件还不支持$natural查询修饰符
                .order("-id").limit(offset + 1);
        List<PictureStory> descendingList = query.asList();
        if (CollectionUtils.isNotEmpty(descendingList)) {
            result.addAll(query.asList());
        }

        return result;
    }

    @Override
    protected Class<PictureStory> entityClass() {
        return PictureStory.class;
    }

}
