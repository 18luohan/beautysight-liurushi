/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.community;

import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.app.WorkApp;
import com.beautysight.liurushi.community.app.command.FindWorkProfilesInRangeCommand;
import com.beautysight.liurushi.community.app.command.PublishWorkCommand;
import com.beautysight.liurushi.community.app.presentation.PublishWorkPresentation;
import com.beautysight.liurushi.community.app.presentation.WorkPresentation;
import com.beautysight.liurushi.community.app.presentation.WorkProfilePresentation;
import com.beautysight.liurushi.common.domain.OffsetDirection;
import com.beautysight.liurushi.fundamental.app.NotifyPicUploadedCommand;
import com.beautysight.liurushi.rest.common.APIs;
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
        command.setAuthor(Authors.currentAuthor());
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
    public WorkPresentation getWorkBy(@PathVariable("workId") String workId) {
        PreconditionUtils.checkRequired("url path variable workId", workId);
        return workApp.getWorkBy(workId);
    }

    @RequestMapping(value = "/pgc/latest", method = RequestMethod.GET)
    @VisitorApiPermission
    public WorkProfilePresentation getPgcLatestWorkProfiles(@RequestParam("count") int count) {
        PreconditionUtils.checkGreaterThanZero("request param count", count);
        return workApp.getPgcLatestWorkProfiles(count);
    }

    @RequestMapping(value = "/pgc", method = RequestMethod.GET)
    @VisitorApiPermission
    public WorkProfilePresentation getPgcWorkProfilesInRange(@RequestParam(required = false) String referenceWork,
                                                             @RequestParam Integer offset,
                                                             @RequestParam(required = false) OffsetDirection direction) {
        FindWorkProfilesInRangeCommand command = newQueryCommandAndValidate(referenceWork, offset, direction);
        return workApp.findPgcWorkProfilesInRange(command);
    }

    @RequestMapping(value = "/ugc/latest", method = RequestMethod.GET)
    @VisitorApiPermission
    public WorkProfilePresentation getUgcLatestWorkProfiles(@RequestParam("count") int count) {
        PreconditionUtils.checkGreaterThanZero("request param count", count);
        return workApp.getUgcLatestWorkProfiles(count);
    }

    @RequestMapping(value = "/ugc", method = RequestMethod.GET)
    @VisitorApiPermission
    public WorkProfilePresentation getUgcWorkProfilesInRange(@RequestParam(required = false) String referenceWork,
                                                             @RequestParam Integer offset,
                                                             @RequestParam(required = false) OffsetDirection direction) {
        FindWorkProfilesInRangeCommand command = newQueryCommandAndValidate(referenceWork, offset, direction);
        return workApp.findUgcWorkProfilesInRange(command);
    }

    private FindWorkProfilesInRangeCommand newQueryCommandAndValidate(
            String referenceWork, Integer offset, OffsetDirection direction) {
        FindWorkProfilesInRangeCommand command = new FindWorkProfilesInRangeCommand();
        command.referenceWork = referenceWork;
        command.offset = offset;
        if (direction != null) {
            command.direction = direction;
        }
        command.validate();
        return command;
    }

}