/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.community.app.command.AuthorWorksRange;
import com.beautysight.liurushi.community.domain.model.work.Work;
import com.beautysight.liurushi.community.domain.model.work.WorkRepo;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.google.common.base.Optional;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
@Repository
public class WorkRepoImpl extends AbstractMongoRepository<Work> implements WorkRepo {

    private static final Fields workBasicFields = Fields.newInstance().append("id", "authorId", "source", "publishedAt");
    private static final String[] workProfileFields = workBasicFields.copyThenAppend("pictureStory.title", "pictureStory.cover.picture").toArray();
    private static final String[] pictureStoryFields = workBasicFields.copyThenAppend("pictureStory").toArray();

    @Override
    public List<Work> getLatestWorks(Work.Source source, int count) {
        Conditions conditions = Conditions.of("source", source);
        FieldsFilter filter = new FieldsFilter(true, workProfileFields);
        return findLatest(conditions, count, Optional.of(filter));
    }

    @Override
    public List<Work> findWorksInRange(Work.Source source,
                                       Range range) {
        Conditions conditions = Conditions.of("source", source);
        FieldsFilter filter = new FieldsFilter(true, workProfileFields);
        return find(conditions, range, Optional.of(filter));
    }

    @Override
    public Work getPictureStoryOf(String workId) {
        Query<Work> query = newQuery();
        query.retrievedFields(true, pictureStoryFields)
                .field("id").equal(new ObjectId(workId));
        return query.get();
    }

    @Override
    public List<Work> findAuthorWorksIn(AuthorWorksRange range) {
        Conditions conditions = Conditions.of("authorId", toMongoId(range.authorId()));
        FieldsFilter filter = new FieldsFilter(true, workProfileFields);
        return find(conditions, range, Optional.of(filter));
    }

    @Override
    protected Class<Work> entityClass() {
        return Work.class;
    }

}
