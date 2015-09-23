/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work.picstory;

import com.beautysight.liurushi.community.domain.work.Control;
import com.beautysight.liurushi.community.domain.work.layout.Block;
import com.beautysight.liurushi.community.domain.work.layout.BlockLocator;

/**
 * 镜头。镜头就是指所拍摄到的画面。镜头里可以是图画，也可以是文字。
 *
 * @author chenlong
 * @since 1.0
 */
public class Shot extends Control {

    // 目前位置属性未要求客户端提供
    private Block.Position position;
    private Block.Size size;
    private PictureArea picAreaInShot;

    // 对发表成功的作品来说，该属性可选。
    private PictureArea picVisibleArea;

    public void calculatePosition(BlockLocator locator) {
        this.position = locator.locateOneBlockBy(this.size);
    }

}