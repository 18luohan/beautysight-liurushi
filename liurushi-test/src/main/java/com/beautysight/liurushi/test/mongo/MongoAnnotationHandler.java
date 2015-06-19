/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.test.mongo;

import com.beautysight.liurushi.test.utils.Files;
import com.google.common.collect.Lists;
import org.jongo.Jongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-20.
 *
 * @author chenlong
 * @since 1.0
 */
public abstract class MongoAnnotationHandler<A extends Annotation> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Map<ExecutionPhase, MongoAnnotationHandler> registry;

    static {
        registry = new HashMap<>();
        registry.put(ExecutionPhase.BEFORE_TEST_METHOD, new PrepareHandler());
        registry.put(ExecutionPhase.AFTER_TEST_METHOD, new CleanupHandler());
    }

    static MongoAnnotationHandler getHandler(ExecutionPhase executionPhase) {
        MongoAnnotationHandler handler = registry.get(executionPhase);
        if (handler == null) {
            throw new IllegalStateException(String.format(
                    "Found no corresponding annotation handler to ExecutionPhase %s", executionPhase));
        }

        return handler;
    }

    void executeMongoScripts(TestContext testContext) throws Exception {
        AnnotationWrapper<A> wrapper = getAnnotation(testContext);
        if (wrapper.instance == null) {
            return;
        }
        List<File> scriptFiles = getScripts(wrapper, testContext);

        JongoWrapper jongoWrapper = TestContextMongoUtils.retrieveJongoWrapper(testContext, null);
        List<JsonDoc> jsonDocs = JsonDocReader.read(scriptFiles);
        for (JsonDoc jsonDoc : jsonDocs) {
            onEach(jsonDoc, jongoWrapper.getJongo());
        }
    }

    /**
     * Gain script files
     */
    private List<File> getScripts(AnnotationWrapper<A> annotation, TestContext testContext) {
        String[] value = getValueOf(annotation.instance);
        boolean valueDeclared = !ObjectUtils.isEmpty(value);

        if (valueDeclared) {
            logger.debug("Test {} [{}] has been configured with @{}'s 'value' [{}].",
                    annotation.elementType, annotation.elementName,
                    annotation.getClass().getSimpleName(), ObjectUtils.nullSafeToString(value));
            return Files.filesInSameDirWith(testContext.getTestClass(), value);
        }

        return Lists.newArrayList(detectDefaultScript(annotation, testContext));
    }

    /**
     * Detect a default mongo script file by implementing the algorithm defined in
     * {@link Prepare#value}.
     */
    private File detectDefaultScript(AnnotationWrapper<A> annotation, TestContext testContext) {
        Assert.notNull(annotation, "annotation is null");

        Class<?> clazz = testContext.getTestClass();
        Method method = testContext.getTestMethod();

        if (!annotation.classLevel) {
            String secondDefaultScript = clazz.getSimpleName() + "." + method.getName()
                    + scriptSuffix(annotation.instance);
            File secondScriptFile = Files.fileInSameDirWith(clazz, secondDefaultScript);
            if (secondScriptFile.exists()) {
                if (logger.isInfoEnabled()) {
                    logger.info(String.format("Detected default mongo script \"%s\" for test %s [%s]",
                            secondScriptFile.getPath(), annotation.elementType, annotation.elementName));
                }
                return secondScriptFile;
            }
        }

        String firstDefaultScript = clazz.getSimpleName() + scriptSuffix(annotation.instance);
        File firstScriptFile = Files.fileInSameDirWith(clazz, firstDefaultScript);
        if (firstScriptFile.exists()) {
            if (logger.isInfoEnabled()) {
                logger.info(String.format("Detected default mongo script \"%s\" for test %s [%s]",
                        firstScriptFile.getPath(), annotation.elementType, annotation.elementName));
            }
            return firstScriptFile;
        } else {
            String msg = String.format("Could not detect default mongo script for test %s [%s]: "
                            + "%s does not exist. Either declare scripts via @%s or make the "
                            + "default mongo script available.", annotation.elementType, annotation.elementName,
                    firstScriptFile.getPath(), annotation.getClass().getSimpleName());
            logger.error(msg);
            throw new IllegalStateException(msg);
        }
    }

    protected AnnotationWrapper getAnnotation(TestContext testContext) {
        boolean classLevel = false;

        A prepareAnnotation = AnnotationUtils.getAnnotation(testContext.getTestMethod(), annotationType());
        if (prepareAnnotation == null) {
            prepareAnnotation = AnnotationUtils.getAnnotation(testContext.getTestClass(), annotationType());
            if (prepareAnnotation != null) {
                classLevel = true;
            }
        }

        Class<?> clazz = testContext.getTestClass();
        Method method = testContext.getTestMethod();
        String elementType = (classLevel ? "class" : "method");
        String elementName = (classLevel ? clazz.getName() : method.toString());

        return new AnnotationWrapper(prepareAnnotation, classLevel, elementType, elementName);
    }

    /**
     * Return the script file extension {@Code ".data"}.
     */
    protected String scriptFileExt() {
        return ".data";
    }

    /**
     * Return the script file name suffix.
     */
    protected String scriptSuffix(A annotation) {
        return scriptFileExt();
    }

    protected abstract Class<A> annotationType();

    protected abstract String[] getValueOf(A annotation);

    protected abstract void onEach(JsonDoc jsonDoc, Jongo jongo);

    private static class AnnotationWrapper<A extends Annotation> {
        private A instance;
        private boolean classLevel;
        private String elementType;
        private String elementName;

        private AnnotationWrapper(A instance, boolean classLevel, String elementType, String elementName) {
            this.instance = instance;
            this.classLevel = classLevel;
            this.elementType = elementType;
            this.elementName = elementName;
        }
    }

    /**
     * Enumeration of <em>phases</em> that dictate when mongo scripts are executed.
     */
    enum ExecutionPhase {

        /**
         * The configured mongo scripts will be executed <em>before</em> the
         * corresponding test method.
         */
        BEFORE_TEST_METHOD,

        /**
         * The configured mongo scripts will be executed <em>after</em> the
         * corresponding test method.
         */
        AFTER_TEST_METHOD
    }

}
