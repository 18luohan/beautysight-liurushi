/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.community;

import com.beautysight.liurushi.community.app.ContentApp;
import com.beautysight.liurushi.community.app.PublishContentCommand;
import com.beautysight.liurushi.rest.common.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-26.
 *
 * @author chenlong
 * @since 1.0
 */
@RestController
@RequestMapping("/contents")
public class ContentRest {

    @Autowired
    private ContentApp contentApp;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void publishContent(@RequestBody PublishContentCommand command) {
        command.validate();
        command.setAuthor(RequestContext.thisUserClient().user());
        contentApp.publishContent(command);
    }

}
