/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.common.domain.CountResult;
import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.community.app.command.WorkQueryInRangeCommand;
import com.beautysight.liurushi.community.domain.work.ContentType;
import com.beautysight.liurushi.community.domain.work.Work;
import com.beautysight.liurushi.community.domain.work.WorkRepo;
import com.beautysight.liurushi.community.domain.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.work.cs.ContentSectionRepo;
import com.beautysight.liurushi.community.domain.work.picstory.Shot;
import com.beautysight.liurushi.community.domain.work.present.Slide;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.OrderClause;
import com.google.common.base.Optional;
import org.bson.types.ObjectId;
import org.mongodb.morphia.aggregation.Accumulator;
import org.mongodb.morphia.aggregation.Group;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

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

        for (Shot shot : work.story().controls()) {
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
                                              Range range,
                                              List<ContentType> supportedContentTypes) {
        if (source == Work.Source.pgc) {
            return findPgcWorkProfilesInRange(range, supportedContentTypes);
        } else if (source == Work.Source.ugc) {
            return findUgcWorkProfilesInRange(range, supportedContentTypes);
        }
        throw new IllegalArgumentException("Illegal work.source: " + source);
    }

    @Override
    public List<Work> findAuthorWorkProfilesIn(WorkQueryInRangeCommand command) {
        Conditions conditions = Conditions.newInstance()
                .andEqual("authorId", toMongoId(command.authorId()))
                .andLte("contentTypes", ContentType.transformToInt(command.supportedContentTypes));
        return findInOrderById(Optional.of(conditions), command.range, workProfileFieldsFilter);
    }

    @Override
    public Work getWorkOnlyWithPictureStory(String workId) {
        Work work = newQuery().retrievedFields(true, pictureStoryFields)
                .field("id").equal(new ObjectId(workId)).get();

        // TODO 这里的逻辑跟getFullWork重复
        List<Shot> shots = work.story().controls();
        List<ObjectId> sectionIds = new ArrayList<>(shots.size());
        for (Shot shot : shots) {
            sectionIds.add(shot.sectionId());
        }

        Iterable<ContentSection> sections = contentSectionRepo.findAll(sectionIds);
        Map<ObjectId, ContentSection> sectionsMap = new HashMap<>(sectionIds.size());
        for (ContentSection section : sections) {
            sectionsMap.put(section.id(), section);
        }

        for (Shot shot : work.story().controls()) {
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
    public List<Work> findUgcWorkProfilesBy(Work.PresentPriority presentPriority, Range range) {
        Conditions conditions = Conditions.newInstance()
                .andEqual("source", Work.Source.ugc)
                .andEqual("presentPriority", presentPriority.val());
        return findInOrderById(Optional.of(conditions), range, workProfileFieldsFilter);
    }

    @Override
    public void setPresentPriorityAndCalcOrderingVal(Work work) {
        UpdateOperations<Work> updateOps = newUpdateOps().set("presentPriority", work.presentPriority())
                .set("ordering", work.ordering());
        Query<Work> query = newQuery().field("id").equal(work.id());
        datastore.update(query, updateOps);
    }

    @Override
    public CountResult countWorksByPresentPriority(Work.PresentPriority presentPriority) {
        Iterator<CountResult> aggregation = datastore.createAggregation(Work.class)
                .match(newQuery().field("presentPriority").equal(presentPriority.val()))
                .group((String) null, Group.grouping("count", new Accumulator("$sum", 1)))
                .aggregate(CountResult.class);
        if (aggregation.hasNext()) {
            return aggregation.next();
        }
        return new CountResult(0);
    }

    @Override
    protected Class<Work> entityClass() {
        return Work.class;
    }

    private List<Work> findUgcWorkProfilesInRange(Range range, List<ContentType> supportedContentTypes) {
        Conditions conditions = Conditions.newInstance()
                .andEqual("source", Work.Source.ugc)
                .andLte("contentTypes", ContentType.transformToInt(supportedContentTypes));

        if (range.referencePoint().isPresent()) {
            Work work = getBasicWork(range.referencePoint().get());
            range.setCursor("ordering", work.ordering());
        }

        OrderClause orderClauseForBefore = new OrderClause().addInvertibleDesc("ordering").addFixedDesc("id");
        return find(Optional.of(conditions), range, orderClauseForBefore, workProfileFieldsFilter);
    }

    private List<Work> findPgcWorkProfilesInRange(Range range, List<ContentType> supportedContentTypes) {
        Conditions conditions = Conditions.newInstance()
                .andEqual("source", Work.Source.pgc)
                .andLte("contentTypes", ContentType.transformToInt(supportedContentTypes));
        return findInOrderById(Optional.of(conditions), range, workProfileFieldsFilter);
    }

    private Work getBasicWork(String workId) {
        return newQuery().field("id").equal(toMongoId(workId))
                .retrievedFields(true, workBasicFieldsArray).get();
    }

}
