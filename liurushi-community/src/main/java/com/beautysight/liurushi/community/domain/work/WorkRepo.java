/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work;

import com.beautysight.liurushi.common.domain.CountResult;
import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.community.app.command.AuthorWorksRange;
import com.beautysight.liurushi.community.app.command.WorkQueryInRangeCommand;
import com.beautysight.liurushi.community.domain.work.cs.ContentSection;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;
import com.google.common.base.Optional;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public interface WorkRepo extends MongoRepository<Work> {

    Optional<Work> get(String workId);

    Work getFullWork(String workId);

    Work getWorkProfile(String workId);

    List<Work> findWorkProfilesInRange(Work.Source source, Range range, List<ContentSection.Type> supportedContentTypes);

    List<Work> findAuthorWorkProfilesIn(WorkQueryInRangeCommand command);

    Work getWorkOnlyWithPictureStory(String workId);

    void increaseLikeTimesBy(int increment, String workId);

    List<Work> findUgcWorkProfilesInRange(Range range, Work.PresentPriority presentPriority);

    void setPresentPriorityOf(String workId, Work.PresentPriority presentPriority);

    CountResult countWorksByPresentPriority(Work.PresentPriority presentPriority);

    AbstractMongoRepository.Fields workBasicFields = AbstractMongoRepository.Fields.newInstance().append("id", "title", "subtitle", "authorId", "source", "presentPriority", "publishedAt");
    String[] workBasicFieldsArray = workBasicFields.toArray();
    String[] workProfileFields = workBasicFields.copyThenAppend("stats", "story.cover.sectionId").toArray();
    String[] pictureStoryFields = workBasicFields.copyThenAppend("story").toArray();
    Optional<AbstractMongoRepository.FieldsFilter> workBasicFieldsFilter = Optional.of(new AbstractMongoRepository.FieldsFilter(true, workProfileFields));

}