/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.test.mongo;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-20.
 *
 * @author chenlong
 * @since 1.0
 */
@Inherited
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface Cleanup {

    /**
     * The names of mongo-json-docs files that are prepared for a unit test. The files must be
     * placed into the same directory with the XxxTest. And the json docs will be insert into the
     * given mongo collection in the files.
     *
     * @return names of mongo-json-docs files
     */
    String[] value() default {};

}
