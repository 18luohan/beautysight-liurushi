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
        command.validate();
        command.setAuthor(authorService.getAuthorBy(RequestContext.getAccessToken()));
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
    public WorkProfilePresentation getLatestWorkProfiles(@RequestParam("count") int count) {
        PreconditionUtils.checkGreaterThanZero("request param count", count);
        return workApp.getLatestWorkProfiles(count);
    }

    @RequestMapping(value = "/pgc", method = RequestMethod.GET)
    @VisitorApiPermission(true)
    public WorkProfilePresentation getLatestWorkProfiles(@RequestParam("referenceWork") String referenceWork,
                                                         @RequestParam("offset") int offset) {
        PreconditionUtils.checkRequired("request param referenceWork", referenceWork);
        PreconditionUtils.checkGreaterThanOrEqZero("request param offset", offset);
        return workApp.findWorkProfilesInRange(referenceWork, offset);
    }

}
