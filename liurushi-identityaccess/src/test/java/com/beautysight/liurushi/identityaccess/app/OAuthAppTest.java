/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app;

import com.beautysight.liurushi.identityaccess.app.command.DeviceDTO;
import com.beautysight.liurushi.identityaccess.app.presentation.AccessTokenPresentation;
import com.beautysight.liurushi.test.SpringBasedAppTest;
import com.beautysight.liurushi.test.utils.JsonsForTest;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chenlong
 * @since 1.0
 */
public class OAuthAppTest extends SpringBasedAppTest {

    @Autowired
    private OAuthApp oAuthApp;

    @Test
    public void testGetIfAbsentIssueBasicTokenFor() throws Exception {
        DeviceDTO device = JsonsForTest.readJsonFileInSameDirWithTestClassAsModel(
                OAuthAppTest.class, "device.json", DeviceDTO.class);
        AccessTokenPresentation accessToken = oAuthApp.getIfAbsentIssueBasicTokenFor(device);
        assertNotNull(accessToken);
    }

}