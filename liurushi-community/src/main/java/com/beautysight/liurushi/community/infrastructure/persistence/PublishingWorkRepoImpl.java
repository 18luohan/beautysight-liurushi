/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.community.domain.model.work.draft.PublishingWork;
import com.beautysight.liurushi.community.domain.model.work.draft.PublishingWorkRepo;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author chenlong
 * @since 1.0
 */
@Repository
public class PublishingWorkRepoImpl extends AbstractMongoRepository<PublishingWork> implements PublishingWorkRepo {

    @Override
    public PublishingWork getAllFilesIn(String workId) {
        Query<PublishingWork> query = newQuery().retrievedFields(true, "files")
                .field("id").equal(new ObjectId(workId));
        return query.get();
    }

    @Override
    protected Class<PublishingWork> entityClass() {
        return PublishingWork.class;
    }

}
