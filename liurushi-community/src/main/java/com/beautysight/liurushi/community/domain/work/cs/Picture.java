/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work.cs;

import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import org.mongodb.morphia.annotations.Reference;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片
 *
 * @author chenlong
 * @since 1.0
 */
public class Picture extends ContentSection {

    @Reference(value = "fileId", idOnly = true)
    private FileMetadata file;

    public Picture() {
        this.type = Type.image;
    }

    public void setFile(FileMetadata file) {
        this.file = file;
    }

    public FileMetadata file() {
        return this.file;
    }

    public String key() {
        return file.key();
    }

    public enum Format {
        jpg, jpeg, png, bmp, webp
    }

    public enum ThumbnailSpec {

        wk1080x, wk750x, wk720x, wk640x, wk480x, wk300x;

        private static final Map<Integer, ThumbnailSpec> specMap = new HashMap<>();

        static {
            specMap.put(Integer.valueOf(1080), wk1080x);
            specMap.put(Integer.valueOf(750), wk750x);
            specMap.put(Integer.valueOf(720), wk720x);
            specMap.put(Integer.valueOf(640), wk640x);
            specMap.put(Integer.valueOf(480), wk480x);
            specMap.put(Integer.valueOf(300), wk300x);
        }

        public static ThumbnailSpec of(Integer intSpec) {
            if (intSpec == null) {
                throw new IllegalParamException("intSpec is null");
            }
            ThumbnailSpec spec = specMap.get(intSpec);
            if (spec == null) {
                throw new IllegalParamException("Illegal intSpec:" + intSpec);
            }
            return spec;
        }

    }

}