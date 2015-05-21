/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.test;

import com.beautysight.liurushi.test.mongo.MongoScriptsTestExecutionListener;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-13.
 *
 * @author chenlong
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/*.xml")
@ActiveProfiles("unit-test")
@TestExecutionListeners(value = { MongoScriptsTestExecutionListener.class },
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public abstract class SpringBasedAppTest {
}
