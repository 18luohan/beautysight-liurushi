/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.community.domain.work.present.Presentation;
import com.beautysight.liurushi.community.domain.work.present.PresentationRepo;
import org.springframework.stereotype.Repository;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-22.
 *
 * @author chenlong
 * @since 1.0
 */
@Repository
public class PresentationRepoImpl extends AbstractMongoRepository<Presentation> implements PresentationRepo {

    @Override
    protected Class<Presentation> entityClass() {
        return Presentation.class;
    }

}
