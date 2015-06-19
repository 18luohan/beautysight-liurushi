/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.common.utils.Jsons;
import com.beautysight.liurushi.test.SpringBasedAppTest;
import com.beautysight.liurushi.test.mongo.Cleanup;
import com.beautysight.liurushi.test.utils.Files;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-25.
 *
 * @author chenlong
 * @since 1.0
 */
@Cleanup
public class ContentAppTest extends SpringBasedAppTest {

    @Autowired
    private ContentApp contentApp;

    @Test
    public void testPublishContent() throws Exception {
        PublishContentCommand command = Jsons.toObject(
                Files.fileInSameDirWith(ContentAppTest.class, "PublishContentCommand.json"),
                PublishContentCommand.class);
        contentApp.publishContent(command);
    }

}