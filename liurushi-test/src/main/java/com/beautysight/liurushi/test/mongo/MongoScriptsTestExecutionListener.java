/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.test.mongo;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * 在测试执行前运行脚本准备、清理数据。
 * 该实现参考了{@link org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener}
 *
 * @author chenlong
 * @since 1.0
 */
public class MongoScriptsTestExecutionListener extends AbstractTestExecutionListener {

    /**
     * Returns {@code 5001}.
     */
    @Override
    public final int getOrder() {
        return 5001;
    }

    /**
     * Execute mongo scripts configured via {@link Prepare @Prepare} for the supplied
     * {@link TestContext} <em>before</em> the current test method.
     */
    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        MongoAnnotationHandler.getHandler(MongoAnnotationHandler.ExecutionPhase.BEFORE_TEST_METHOD)
                .executeMongoScripts(testContext);
    }

    /**
     * Execute SQL scripts configured via {@link Sql @Sql} for the supplied
     * {@link TestContext} <em>after</em> the current test method.
     */
    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        MongoAnnotationHandler.getHandler(MongoAnnotationHandler.ExecutionPhase.AFTER_TEST_METHOD)
                .executeMongoScripts(testContext);
    }

}
