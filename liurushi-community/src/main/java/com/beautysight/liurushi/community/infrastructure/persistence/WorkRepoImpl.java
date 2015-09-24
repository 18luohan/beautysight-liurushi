/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.community.app.command.AuthorWorksRange;
import com.beautysight.liurushi.community.domain.work.Work;
import com.beautysight.liurushi.community.domain.work.WorkRepo;
import com.beautysight.liurushi.community.domain.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.work.cs.ContentSectionRepo;
import com.beautysight.liurushi.community.domain.work.picstory.Shot;
import com.beautysight.liurushi.community.domain.work.present.Slide;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.google.common.base.Optional;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
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
public class WorkRepoImpl extends AbstractMongoRepository<Work> implements WorkRepo {

    @Autowired
    private ContentSectionRepo contentSectionRepo;

    @Override
    public Optional<Work> get(String workId) {
        return findOneBy(newQuery().field("id").equal(toMongoId(workId)));
    }

    @Override
    public Work getFullWork(String workId) {
        Work work = findOne(workId);

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

        for (Shot shot : work.pictureStory().controls()) {
            shot.setContentSection(sectionsMap.get(shot.sectionId()));
        }

        return work;
    }

    @Override
    public Work getWorkProfile(String workId) {
        return newQuery()
                .retrievedFields(true, workProfileFields)
                .field("id").equal(toMongoId(workId)).get();
    }

    @Override
    public List<Work> findWorkProfilesInRange(Work.Source source,
                                              Range range) {
        if (source == Work.Source.pgc) {
            return findPgcWorkProfilesInRange(range);
        } else if (source == Work.Source.ugc) {
            return findUgcWorkProfilesInRange(range);
        }
        throw new IllegalArgumentException("Illegal work.source: " + source);
    }

    @Override
    public List<Work> findAuthorWorkProfilesIn(AuthorWorksRange range) {
        Conditions conditions = Conditions.newWithEqual("authorId", toMongoId(range.authorId()));
        return find(Optional.of(conditions), range, workBasicFieldsFilter);
    }

    @Override
    public Work getWorkOnlyWithPictureStory(String workId) {
        Work work = newQuery().retrievedFields(true, pictureStoryFields)
                .field("id").equal(new ObjectId(workId)).get();

        // TODO 这里的逻辑跟getFullWork重复
        List<Shot> shots = work.pictureStory().controls();
        List<ObjectId> sectionIds = new ArrayList<>(shots.size());
        for (Shot shot : shots) {
            sectionIds.add(shot.sectionId());
        }

        Iterable<ContentSection> sections = contentSectionRepo.findAll(sectionIds);
        Map<ObjectId, ContentSection> sectionsMap = new HashMap<>(sectionIds.size());
        for (ContentSection section : sections) {
            sectionsMap.put(section.id(), section);
        }

        for (Shot shot : work.pictureStory().controls()) {
            shot.setContentSection(sectionsMap.get(shot.sectionId()));
        }

        return work;
    }

    @Override
    public void increaseLikeTimesBy(int increment, String workId) {
        Query<Work> query = newQuery().field("id").equal(toMongoId(workId));
        UpdateOperations<Work> updateOps = newUpdateOps().inc("stats.likeTimes", increment);
        datastore.update(query, updateOps);
    }

    @Override
    public List<Work> findUgcWorkProfilesInRange(Range range, Work.PresentPriority presentPriority) {
        Conditions conditions = Conditions.newWithEqual("source", Work.Source.ugc)
                .andEqual("presentPriority", presentPriority.val());
        return find(Optional.of(conditions), range, workBasicFieldsFilter);
    }

    @Override
    public void selectOrCancel(String workId, Work.PresentPriority presentPriority) {
        UpdateOperations<Work> updateOps = newUpdateOps().set("presentPriority", presentPriority.val());
        Query<Work> query = newQuery().field("id").equal(toMongoId(workId));
        datastore.update(query, updateOps);
    }

    @Override
    protected Class<Work> entityClass() {
        return Work.class;
    }

    private List<Work> findUgcWorkProfilesInRange(Range range) {
        Conditions afterConditions = Conditions.newWithEqual("source", Work.Source.ugc);
        Conditions beforeConditions = Conditions.newWithEqual("source", Work.Source.ugc);
        if (range.referencePoint().isPresent()) {
            Work work = getBasicWork(range.referencePoint().get());
            afterConditions.andGte("presentPriority", work.presentPriority());
            beforeConditions.andLte("presentPriority", work.presentPriority());
        }
        OrderByFields descByFields = OrderByFields.descBy("presentPriority").addDescField("id");
        return find(Optional.of(afterConditions), Optional.of(beforeConditions), range, descByFields, workBasicFieldsFilter);
    }

    private List<Work> findPgcWorkProfilesInRange(Range range) {
        Conditions conditions = Conditions.newWithEqual("source", Work.Source.pgc);
        return find(Optional.of(conditions), range, workBasicFieldsFilter);
    }

    private Work getBasicWork(String workId) {
        return newQuery().field("id").equal(toMongoId(workId))
                .retrievedFields(true, workBasicFieldsArray).get();
    }

}
