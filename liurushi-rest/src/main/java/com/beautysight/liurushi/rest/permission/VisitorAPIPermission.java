/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.permission;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-30.
 *
 * @author chenlong
 * @since 1.0
 */
@Inherited
@Retention(RUNTIME)
@Target({ METHOD })
public @interface VisitorApiPermission {

    /**
     * Indicates whether the restful API that the API annotated by this annotation
     * can be requested by the visitors.
     * @return
     */
    boolean value() default false;

}
