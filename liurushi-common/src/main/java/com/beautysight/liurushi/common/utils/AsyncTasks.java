/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chenlong
 * @since 1.0
 */
public class AsyncTasks {

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void submit(Runnable task) {
        executor.submit(task);
    }

}
