/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.community;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.app.WorkApp;
import com.beautysight.liurushi.community.app.WorkProfileVM;
import com.beautysight.liurushi.community.app.command.AuthorWorksRange;
import com.beautysight.liurushi.community.app.command.PublishWorkCommand;
import com.beautysight.liurushi.community.app.presentation.PublishWorkPresentation;
import com.beautysight.liurushi.community.app.presentation.WorkProfileList;
import com.beautysight.liurushi.community.app.presentation.WorkVM;
import com.beautysight.liurushi.fundamental.app.NotifyPicUploadedCommand;
import com.beautysight.liurushi.rest.common.APIs;
import com.beautysight.liurushi.rest.common.RequestContext;
import com.beautysight.liurushi.rest.permission.VisitorApiPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenlong
 * @since 1.0
 */
@RestController
@RequestMapping(APIs.WORKS_V1)
public class WorkRest {

    @Autowired
    private WorkApp workApp;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public PublishWorkPresentation publishWork(@RequestBody PublishWorkCommand command) {
        command.setAuthorId(RequestContext.currentUserId());
        command.validate();
        return workApp.publishWork(command);
    }

    @RequestMapping(value = "/{workId}/files/{fileId}", method = RequestMethod.PUT)
    public void notifyThatPicUploaded(@PathVariable("workId") String workId,
                                      @PathVariable("fileId") String fileId,
                                      @RequestBody NotifyPicUploadedCommand command) {
        command.validate();
        workApp.onPicSectionUploaded(workId, fileId, command);
    }

    @RequestMapping(value = "/{workId}", method = RequestMethod.GET)
    @VisitorApiPermission
    public WorkVM getWorkBy(@PathVariable("workId") String workId) {
        PreconditionUtils.checkRequired("url path variable workId", workId);
        return workApp.getFullWorkBy(workId, RequestContext.optionalCurrentUserId());
    }

    @RequestMapping(value = "/{workId}/profile", method = RequestMethod.GET)
    @VisitorApiPermission
    public WorkProfileVM getWorkProfileBy(@PathVariable("workId") String workId) {
        PreconditionUtils.checkRequired("url path variable workId", workId);
        return workApp.getWorkProfileBy(workId, RequestContext.optionalCurrentUserId());
    }

    @RequestMapping(value = "/pgc/latest", method = RequestMethod.GET)
    @VisitorApiPermission
    public WorkProfileList getPgcLatestWorkProfiles(@RequestParam("count") int count) {
        PreconditionUtils.checkGreaterThanZero("request param count", count);
        return workApp.getPgcLatestWorkProfiles(count, RequestContext.optionalCurrentUserId());
    }

    @RequestMapping(value = "/pgc", method = RequestMethod.GET)
    @VisitorApiPermission
    public WorkProfileList getPgcWorkProfilesInRange(@RequestParam(required = false) String referencePoint,
                                                     @RequestParam Integer offset,
                                                     @RequestParam(required = false) Range.OffsetDirection direction) {
        return workApp.findPgcWorkProfilesIn(new Range(referencePoint, offset, direction), RequestContext.optionalCurrentUserId());
    }

    @RequestMapping(value = "/ugc/latest", method = RequestMethod.GET)
    @VisitorApiPermission
    public WorkProfileList getUgcLatestWorkProfiles(@RequestParam("count") int count) {
        PreconditionUtils.checkGreaterThanZero("request param count", count);
        return workApp.getUgcLatestWorkProfiles(count, RequestContext.optionalCurrentUserId());
    }

    @RequestMapping(value = "/ugc", method = RequestMethod.GET)
    @VisitorApiPermission
    public WorkProfileList getUgcWorkProfilesInRange(@RequestParam(required = false) String referencePoint,
                                                     @RequestParam Integer offset,
                                                     @RequestParam(required = false) Range.OffsetDirection direction) {
        return workApp.findUgcWorkProfilesIn(new Range(referencePoint, offset, direction), RequestContext.optionalCurrentUserId());
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @VisitorApiPermission
    public WorkProfileList getAuthorWorks(@RequestParam String authorId,
                                          @RequestParam(required = false) String referencePoint,
                                          @RequestParam Integer offset,
                                          @RequestParam(required = false) Range.OffsetDirection direction) {
        PreconditionUtils.checkRequired("authorId", authorId);
        AuthorWorksRange range = new AuthorWorksRange(authorId, referencePoint, offset, direction);
        return workApp.findAuthorWorksIn(range);
    }

}