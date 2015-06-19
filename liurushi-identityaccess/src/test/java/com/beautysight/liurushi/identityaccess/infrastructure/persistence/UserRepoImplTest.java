/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.infrastructure.persistence;

import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.repo.UserRepo;
import com.beautysight.liurushi.test.SpringBasedAppTest;
import com.beautysight.liurushi.test.mongo.Cleanup;
import com.beautysight.liurushi.test.mongo.Prepare;
import com.beautysight.liurushi.test.utils.Reflections;
import com.google.common.base.Optional;
import org.joda.time.DateTime;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author chenlong
 * @since 1.0
 */
public class UserRepoImplTest extends SpringBasedAppTest {

    @Autowired
    private UserRepo userRepo;

    @Test
    @Prepare
    @Cleanup
    public void updateLastLoginTime() {
        User user = userRepo.withMobile("12345678901").get();
        Date expectedLastLoginTime = new DateTime(2015, 6, 1, 12, 30).toDate();
        Reflections.setField(user, "lastLogin", expectedLastLoginTime);
        userRepo.updateLastLoginTime(user);
        User updatedUser = userRepo.withMobile("12345678901").get();

        assertNotNull(updatedUser);
        assertNotNull(Reflections.getField(updatedUser, "lastLogin"));
        assertEquals("lastLoginTime", expectedLastLoginTime.getTime(),
                Reflections.<Date>getField(updatedUser, "lastLogin").getTime());
    }

    @Test
    @Prepare
    @Cleanup
    public void addAvatarFor() {
        User user = userRepo.withMobile("12345678901").get();
        User.Avatar newAvatar = new User.Avatar("key123", "hash123", 180);
        User updatedUser = userRepo.addAvatarFor(user.id(), newAvatar);

        Optional<User.Avatar> theAvatar = updatedUser.toUserLite().specificAvatar(180);
        assertTrue(theAvatar.isPresent());
        assertEquals("avatar.key", newAvatar.key(), theAvatar.get().key());
    }

}