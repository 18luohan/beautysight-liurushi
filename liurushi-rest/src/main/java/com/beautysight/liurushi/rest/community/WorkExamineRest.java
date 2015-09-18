/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.community;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.app.WorkApp;
import com.beautysight.liurushi.community.app.presentation.WorkProfileList;
import com.beautysight.liurushi.community.app.presentation.WorkVM;
import com.beautysight.liurushi.community.domain.model.work.Work;
import com.beautysight.liurushi.rest.common.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenlong
 * @since 1.0
 */
@RestController
@RequestMapping(APIs.WORKS_V1)
public class WorkExamineRest {

    @Autowired
    private WorkApp workApp;

    @RequestMapping(value = "/ugc/{presentPriority}", method = RequestMethod.GET)
    public WorkProfileList getUgcWorkProfilesInRange(@RequestParam(required = false) String referencePoint,
                                                     @RequestParam Integer offset,
                                                     @RequestParam(required = false) Range.OffsetDirection direction,
                                                     @PathVariable("presentPriority") String presentPriority) {
        return workApp.findUgcWorkProfilesInRange(new Range(referencePoint, offset, direction), Work.PresentPriority.of(presentPriority));
    }

    @RequestMapping(value = "/ugc/discarded", method = RequestMethod.GET)
    public WorkProfileList getDiscardedUgcWorkProfilesInRange(@RequestParam(required = false) String referencePoint,
                                                              @RequestParam Integer offset,
                                                              @RequestParam(required = false) Range.OffsetDirection direction) {
        return workApp.findDiscardedUgcWorkProfilesInRange(new Range(referencePoint, offset, direction));
    }

    @RequestMapping(value = "/{workId}/actions/select", method = RequestMethod.PUT)
    public void select(@PathVariable("workId") String workId) {
        workApp.selectWork(workId);
    }

    @RequestMapping(value = "/{workId}/actions/cancel_select", method = RequestMethod.PUT)
    public void cancelSelect(@PathVariable("workId") String workId) {
        workApp.cancelSelectWork(workId);
    }

    @RequestMapping(value = "/{workId}/actions/discard", method = RequestMethod.PUT)
    public void discard(@PathVariable("workId") String workId) {
        workApp.discardWork(workId);
    }

    @RequestMapping(value = "/{workId}/actions/cancel_discard", method = RequestMethod.PUT)
    public void cancelDiscard(@PathVariable("workId") String workId) {
        workApp.cancelDiscardWork(workId);
    }

    @RequestMapping(value = "/{workId}/actions/examine", method = RequestMethod.GET)
    public WorkVM getFullWorkForExamine(@PathVariable("workId") String workId) {
        PreconditionUtils.checkRequired("url path variable workId", workId);
        return workApp.getFullWorkForExamine(workId);
    }

}