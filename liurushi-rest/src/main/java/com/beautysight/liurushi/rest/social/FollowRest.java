/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.social;

import com.beautysight.liurushi.common.domain.OffsetDirection;
import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.rest.common.APIs;
import com.beautysight.liurushi.rest.common.RequestContext;
import com.beautysight.liurushi.rest.permission.VisitorApiPermission;
import com.beautysight.liurushi.social.app.FollowApp;
import com.beautysight.liurushi.social.app.FollowOrNotCommand;
import com.beautysight.liurushi.social.app.FollowersVM;
import com.beautysight.liurushi.social.app.FollowingsVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenlong
 * @since 1.0
 */
@RestController
@RequestMapping(APIs.FOLLOW_V1)
public class FollowRest {

    @Autowired
    private FollowApp followApp;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void follow(@RequestBody FollowOrNotCommand command) {
        command.followerId = RequestContext.currentUserId();
        command.validate();
        followApp.follow(command);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void unfollow(@RequestBody FollowOrNotCommand command) {
        command.followerId = RequestContext.currentUserId();
        command.validate();
        followApp.unfollow(command);
    }

    @RequestMapping(value = "/followers", method = RequestMethod.GET)
    @VisitorApiPermission
    public FollowersVM getFollowersOf(@RequestParam String userId,
                                                @RequestParam(required = false) String referencePoint,
                                                @RequestParam Integer offset,
                                                @RequestParam(required = false) OffsetDirection direction) {
        Range range = new Range(referencePoint, offset, direction);
        return followApp.findFollowersInRange(userId, range, RequestContext.optionalCurrentUserId());
    }

    @RequestMapping(value = "/followings", method = RequestMethod.GET)
    @VisitorApiPermission
    public FollowingsVM getFollowingsOf(@RequestParam String userId,
                                                 @RequestParam(required = false) String referencePoint,
                                                 @RequestParam Integer offset,
                                                 @RequestParam(required = false) OffsetDirection direction) {
        Range range = new Range(referencePoint, offset, direction);
        return followApp.findFollowingsInRange(userId, range, RequestContext.optionalCurrentUserId());
    }

}
