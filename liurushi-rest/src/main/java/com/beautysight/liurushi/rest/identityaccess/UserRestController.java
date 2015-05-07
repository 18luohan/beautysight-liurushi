/*
 * Copyright (C) 2014, Shaimei Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.identityaccess;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-06.
 *
 * @author chenlong
 * @since 1.0
 */
@RestController("/users")
public class UserRestController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String signUp() {
        return "ok";
    }

}
