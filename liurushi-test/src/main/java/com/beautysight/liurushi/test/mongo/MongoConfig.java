/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.test.mongo;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-20.
 *
 * @author chenlong
 * @since 1.0
 */
public @interface MongoConfig {

    /**
     * The bean name of the {@link com.mongodb.MongoClient} against which the
     * scripts should be executed.
     * <p>The name is only required if there is more than one bean of type
     * {@code MongoClient} in the test's {@code ApplicationContext}. If there
     * is only one such bean, it is not necessary to specify a bean name.
     * <p>Defaults to an empty string, requiring that one of the following is
     * true:
     * <ol>
     * <li>An explicit bean name is defined in a global declaration of
     * {@code @SqlConfig}.
     * <li>The data source can be retrieved from the transaction manager
     * by using reflection to invoke a public method named
     * {@code getDataSource()} on the transaction manager.
     * <li>There is only one bean of type {@code DataSource} in the test's
     * {@code ApplicationContext}.</li>
     * <li>The {@code DataSource} to use is named {@code "dataSource"}.</li>
     * </ol>
     * @see org.springframework.test.context.transaction.TestContextTransactionUtils#retrieveDataSource
     */
    String dataSource() default "";

}
