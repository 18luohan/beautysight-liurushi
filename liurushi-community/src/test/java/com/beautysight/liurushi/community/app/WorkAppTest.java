/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.common.utils.Jsons;
import com.beautysight.liurushi.community.app.command.PublishWorkCommand;
import com.beautysight.liurushi.test.SpringBasedAppTest;
import com.beautysight.liurushi.test.utils.Files;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chenlong
 * @since 1.0
 */
//@Cleanup
public class WorkAppTest extends SpringBasedAppTest {

    @Autowired
    private WorkApp workApp;

    @Test
    public void saveWork() throws Exception {
        PublishWorkCommand command = Jsons.toObject(
                Files.fileInSameDirWith(WorkAppTest.class, "PublishWorkCommand.json"),
                PublishWorkCommand.class);
        workApp.publishWork(command);
    }

    @Test
    public void clean() {

    }

}