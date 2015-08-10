/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work.cs;

/**
 * 文本块
 *
 * @author chenlong
 * @since 1.0
 */
public class TextBlock extends ContentSection {

    private String text;
    private CharacterStyle charStyle;
    private Alignment alignment;

    public TextBlock() {
        this.initialize();
    }

    private void initialize() {
        this.type = Type.text;
    }

    /**
     * 文字样式
     */
    public enum CharacterStyle {
        normal, header, quote
    }

    /**
     * 文本对齐方式
     */
    public enum Alignment {
        left, center, right
    }

}
