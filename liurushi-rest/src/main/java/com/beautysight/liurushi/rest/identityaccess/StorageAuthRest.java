/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.identityaccess;

import com.beautysight.liurushi.identityaccess.app.StorageAuthApp;
import com.beautysight.liurushi.identityaccess.app.command.GetDownloadUrlCommand;
import com.beautysight.liurushi.identityaccess.app.command.GetUploadTokenCommand;
import com.beautysight.liurushi.identityaccess.app.presentation.DownloadUrlPresentation;
import com.beautysight.liurushi.identityaccess.app.presentation.UploadTokenPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-18.
 *
 * @author chenlong
 * @since 1.0
 */
@RestController
public class StorageAuthRest {

    @Autowired
    private StorageAuthApp authApp;

    @RequestMapping(value = "/upload_token", method = RequestMethod.POST)
    public UploadTokenPresentation getUploadToken(@RequestBody(required = false) GetUploadTokenCommand command) {
        if (command == null) {
            command = new GetUploadTokenCommand();
        }
        return authApp.getUploadToken(command);
    }

    @RequestMapping(value = "/download_url", method = RequestMethod.POST)
    public DownloadUrlPresentation getDownloadUrl(@RequestBody GetDownloadUrlCommand command) {
        return authApp.getDownloadUrl(command);
    }

}
