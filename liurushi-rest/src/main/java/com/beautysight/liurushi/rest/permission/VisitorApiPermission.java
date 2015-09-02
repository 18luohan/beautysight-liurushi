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
 * A restful API annotated by this annotation can also be requested by the visitors.
 * Otherwise it can only be accessed by the members.
 *
 * @author chenlong
 * @since 1.0
 */
@Inherited
@Retention(RUNTIME)
@Target({METHOD})
public @interface VisitorApiPermission {
}
