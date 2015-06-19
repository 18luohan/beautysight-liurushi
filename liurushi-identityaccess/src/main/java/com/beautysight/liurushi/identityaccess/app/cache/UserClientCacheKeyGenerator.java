/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.cache;

import com.beautysight.liurushi.identityaccess.app.command.AccessTokenDTO;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 为 user client cache 生成 key。
 * @author chenlong
 * @since 1.0
 */
@Component("userClientCacheKeyGenerator")
public class UserClientCacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        AccessTokenDTO dto = (AccessTokenDTO) params[0];
        return new AccessTokenAsKey(dto);
    }

}
