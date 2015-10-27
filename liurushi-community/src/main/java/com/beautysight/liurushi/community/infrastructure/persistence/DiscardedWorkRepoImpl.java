/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.community.domain.work.DiscardedWork;
import com.beautysight.liurushi.community.domain.work.DiscardedWorkRepo;
import com.beautysight.liurushi.community.domain.work.WorkRepo;
import com.beautysight.liurushi.community.domain.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.work.cs.ContentSectionRepo;
import com.beautysight.liurushi.community.domain.work.picstory.Shot;
import com.beautysight.liurushi.community.domain.work.present.Slide;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.google.common.base.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
@Repository
public class DiscardedWorkRepoImpl extends AbstractMongoRepository<DiscardedWork> implements DiscardedWorkRepo {

    @Autowired
    private ContentSectionRepo contentSectionRepo;

    @Override
    public Optional<DiscardedWork> get(String workId) {
        return findOneBy(newQuery().field("id").equal(toMongoId(workId)));
    }

    @Override
    public DiscardedWork getFullWork(String workId) {
        DiscardedWork work = findOne(workId);

        List<Slide> slides = work.presentation().controls();
        List<ObjectId> sectionIds = new ArrayList<>(slides.size());
        for (Slide slide : slides) {
            sectionIds.add(slide.sectionId());
        }

        Iterable<ContentSection> sections = contentSectionRepo.findAll(sectionIds);
        Map<ObjectId, ContentSection> sectionsMap = new HashMap<>(sectionIds.size());
        for (ContentSection section : sections) {
            sectionsMap.put(section.id(), section);
        }

        for (Slide slide : slides) {
            slide.setContentSection(sectionsMap.get(slide.sectionId()));
        }

        for (Shot shot : work.story().controls()) {
            shot.setContentSection(sectionsMap.get(shot.sectionId()));
        }

        return work;
    }

    @Override
    public List<DiscardedWork> findInRange(Range range) {
        return find(Optional.<Conditions>absent(), range, WorkRepo.workBasicFieldsFilter);
    }

    @Override
    protected Class<DiscardedWork> entityClass() {
        return DiscardedWork.class;
    }

}