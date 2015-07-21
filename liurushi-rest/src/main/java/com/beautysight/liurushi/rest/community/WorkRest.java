/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.community;

import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.app.WorkApp;
import com.beautysight.liurushi.community.app.command.PublishWorkCommand;
import com.beautysight.liurushi.community.app.presentation.WorkPresentation;
import com.beautysight.liurushi.community.app.presentation.WorkProfilePresentation;
import com.beautysight.liurushi.community.domain.service.AuthorService;
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
    @Autowired
    private AuthorService authorService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void publishWork(@RequestBody PublishWorkCommand command) {
        command.setAuthor(authorService.getAuthorBy(RequestContext.getAccessToken()));
        command.validate();
        workApp.publishWork(command);
    }

    @RequestMapping(value = "/{workId}", method = RequestMethod.GET)
    @VisitorApiPermission(true)
    public WorkPresentation getWorkBy(@PathVariable("workId") String workId) {
        PreconditionUtils.checkRequired("url path variable workId", workId);
        return workApp.getWorkBy(workId);
    }

    @RequestMapping(value = "/pgc/latest", method = RequestMethod.GET)
    @VisitorApiPermission(true)
    public WorkProfilePresentation getPgcLatestWorkProfiles(@RequestParam("count") int count) {
        PreconditionUtils.checkGreaterThanZero("request param count", count);
        return workApp.getPgcLatestWorkProfiles(count);
    }

    @RequestMapping(value = "/pgc", method = RequestMethod.GET)
    @VisitorApiPermission(true)
    public WorkProfilePresentation getPgcWorkProfilesInRange(@RequestParam("referenceWork") String referenceWork,
                                                             @RequestParam("offset") int offset) {
        PreconditionUtils.checkRequired("request param referenceWork", referenceWork);
        PreconditionUtils.checkGreaterThanOrEqZero("request param offset", offset);
        return workApp.findPgcWorkProfilesInRange(referenceWork, offset);
    }

    @RequestMapping(value = "/ugc/latest", method = RequestMethod.GET)
    @VisitorApiPermission(true)
    public WorkProfilePresentation getUgcLatestWorkProfiles(@RequestParam("count") int count) {
        PreconditionUtils.checkGreaterThanZero("request param count", count);
        return workApp.getUgcLatestWorkProfiles(count);
    }

    @RequestMapping(value = "/ugc", method = RequestMethod.GET)
    @VisitorApiPermission(true)
    public WorkProfilePresentation getUgcWorkProfilesInRange(@RequestParam("referenceWork") String referenceWork,
                                                             @RequestParam("offset") int offset) {
        PreconditionUtils.checkRequired("request param referenceWork", referenceWork);
        PreconditionUtils.checkGreaterThanOrEqZero("request param offset", offset);
        return workApp.findUgcWorkProfilesInRange(referenceWork, offset);
    }

}
