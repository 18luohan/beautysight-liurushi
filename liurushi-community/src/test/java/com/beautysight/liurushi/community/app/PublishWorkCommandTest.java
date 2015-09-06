/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.common.utils.Jsons;
import com.beautysight.liurushi.community.app.dpo.ContentSectionPayload;
import com.beautysight.liurushi.test.utils.Files;
import com.fasterxml.jackson.core.type.TypeReference;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public class PublishWorkCommandTest {

    @Test
    public void readContentSectionsJsonWithGenericType() {
        List<ContentSectionPayload> sections = Jsons.toObject(
                Files.fileInSameDirWith(PublishWorkCommandTest.class, "ContentSections.json"),
                new TypeReference<List<ContentSectionPayload>>() {}
        );
        assertEquals(6, sections.size());
        for (ContentSectionPayload sectionDTO : sections) {
            assertNotNull(sectionDTO.type);
        }
    }

}