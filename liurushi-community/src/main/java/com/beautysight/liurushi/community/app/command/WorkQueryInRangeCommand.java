/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.command;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.community.domain.work.ContentType;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public class WorkQueryInRangeCommand implements Command {

    public Range range;
    public Optional<String> loginUserId;
    public Optional<Integer> thumbnailSpec;
    public List<ContentType> supportedContentTypes;

    private String authorId;

    private static final List<ContentType> default_supported_content_types = Lists.newArrayList(ContentType.text, ContentType.image);

    public WorkQueryInRangeCommand(Range range, Optional<String> loginUserId, Integer thumbnailSpec) {
        this.range = range;
        this.loginUserId = loginUserId;
        this.thumbnailSpec = Optional.fromNullable(thumbnailSpec);
        this.setSupportedContentTypes(Optional.<String>absent());
    }

    public WorkQueryInRangeCommand(Range range, Optional<String> loginUserId, Integer thumbnailSpec, String supportedContentTypesStr) {
        this.range = range;
        this.loginUserId = loginUserId;
        this.thumbnailSpec = Optional.fromNullable(thumbnailSpec);
        this.setSupportedContentTypes(Optional.fromNullable(supportedContentTypesStr));
    }

    public WorkQueryInRangeCommand(Range range, String authorId) {
        this(range, authorId, null);
    }

    public WorkQueryInRangeCommand(Range range, String authorId, String supportedContentTypesStr) {
        this.range = range;
        this.authorId = authorId;
        this.setSupportedContentTypes(Optional.fromNullable(supportedContentTypesStr));
        // 个人中心/我的作品 图片规格为300
        this.thumbnailSpec = Optional.of(Integer.valueOf(300));
    }

    public String authorId() {
        return this.authorId;
    }

    private void setSupportedContentTypes(Optional<String> supportedContentTypesStr) {
        if (supportedContentTypesStr.isPresent()) {
            this.supportedContentTypes = parseSupportedContentTypesStr(supportedContentTypesStr.get());
        } else {
            this.supportedContentTypes = default_supported_content_types;
        }
    }

    private static List<ContentType> parseSupportedContentTypesStr(String supportedContentTypesStr) {
        try {
            String[] typeNames = supportedContentTypesStr.split(",");
            List<ContentType> types = new ArrayList<>(typeNames.length);
            for (String typeName : typeNames) {
                ContentType type = ContentType.valueOf(typeName.trim());
                if (!types.contains(type)) {
                    types.add(type);
                }
            }
            return types;
        } catch (Exception e) {
            throw new IllegalParamException(e, "supportedContentTypes: %s, err: %s",
                    supportedContentTypesStr, e.getMessage());
        }
    }

}