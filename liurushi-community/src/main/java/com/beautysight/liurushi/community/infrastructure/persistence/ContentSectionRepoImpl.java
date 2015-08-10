/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.community.domain.model.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.model.work.cs.ContentSectionRepo;
import org.springframework.stereotype.Repository;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-23.
 *
 * @author chenlong
 * @since 1.0
 */
@Repository
public class ContentSectionRepoImpl extends AbstractMongoRepository<ContentSection> implements ContentSectionRepo {

    @Override
    protected Class<ContentSection> entityClass() {
        return ContentSection.class;
    }

}
