/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.test.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.test.context.TestContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Utility methods for working with data access related beans
 * within the <em>Spring TestContext Framework</em>.
 * This implementation refers to {@link org.springframework.test.context.transaction.TestContextTransactionUtils}.
 *
 * @author chenlong
 * @since 1.0
 */
public abstract class TestContextMongoUtils {

    private static final Logger logger = LoggerFactory.getLogger(TestContextMongoUtils.class);

    /**
     * Default bean name for a {@link JongoWrapper}: {@code "jongoWrapper"}.
     */
    public static final String DEFAULT_JONGO_NAME = "jongoWrapper";

    private TestContextMongoUtils() {
        /* prevent instantiation */
    }

    /**
     * Retrieve the {@link JongoWrapper} to use for the supplied {@linkplain TestContext
     * test context}.
     * <p>The following algorithm is used to retrieve the {@code JongoWrapper} from
     * the {@link org.springframework.context.ApplicationContext ApplicationContext}
     * of the supplied test context:
     * <ol>
     * <li>Look up the {@code JongoWrapper} by type and name, if the supplied
     * {@code name} is non-empty, throwing a {@link BeansException} if the named
     * {@code JongoWrapper} does not exist.
     * <li>Attempt to look up a single {@code JongoWrapper} by type.
     * <li>Attempt to look up the {@code JongoWrapper} by type and the
     * {@linkplain #DEFAULT_JONGO_NAME default mongo wrapper name}.
     *
     * @param testContext the test context for which the {@code JongoWrapper}
     *                    should be retrieved; never {@code null}
     * @param name        the name of the {@code JongoWrapper} to retrieve; may be {@code null}
     *                    or <em>empty</em>
     * @return the {@code JongoWrapper} to use, or {@code null} if not found
     * @throws BeansException if an error occurs while retrieving an explicitly
     *                        named {@code JongoWrapper}
     */
    public static JongoWrapper retrieveJongoWrapper(TestContext testContext, String name) {
        Assert.notNull(testContext, "TestContext must not be null");
        BeanFactory bf = testContext.getApplicationContext().getAutowireCapableBeanFactory();

        try {
            // look up by type and explicit name
            if (StringUtils.hasText(name)) {
                return bf.getBean(name, JongoWrapper.class);
            }
        } catch (BeansException ex) {
            logger.error(
                    String.format("Failed to retrieve JongoWrapper named '%s' for test context %s", name, testContext), ex);
            throw ex;
        }

        try {
            if (bf instanceof ListableBeanFactory) {
                ListableBeanFactory lbf = (ListableBeanFactory) bf;

                // look up single bean by type
                Map<String, JongoWrapper> jongoWrappers = BeanFactoryUtils.beansOfTypeIncludingAncestors(lbf,
                        JongoWrapper.class);
                if (jongoWrappers.size() == 1) {
                    return jongoWrappers.values().iterator().next();
                }
            }

            // look up by type and default name
            return bf.getBean(DEFAULT_JONGO_NAME, JongoWrapper.class);
        } catch (BeansException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Caught exception while retrieving jongoWrappers for test context " + testContext, ex);
            }
            return null;
        }
    }

}
