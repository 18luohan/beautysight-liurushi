/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.community.domain.model.content.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 与内容相关的应用逻辑
 *
 * @author chenlong
 * @since 1.0
 */
@Service
public class ContentApp {

    @Autowired
    private PictureStoryRepo pictureStoryRepo;
    @Autowired
    private PresentationRepo presentationRepo;
    @Autowired
    private ContentSectionRepo contentSectionRepo;

    public void publishContent(PublishContentCommand command) {
        Map<String, ContentSection> contentSections = save(command.contentSections());
        PictureStory pictureStory = command.pictureStory.toPictureStory();
        Presentation presentation = command.presentation.toPresentation();
        setCover(pictureStory, contentSections, command);
        setContentSections(pictureStory, contentSections, command.pictureStory.shots);
        setContentSections(presentation, contentSections, command.presentation.slides);
        pictureStoryRepo.save(pictureStory);
        presentationRepo.save(presentation);
    }

    private Map<String, ContentSection> save(Map<String, ContentSection> newSections) {
        for (Map.Entry<String, ContentSection> entry : newSections.entrySet()) {
            ContentSection section = contentSectionRepo.save(entry.getValue());
            entry.setValue(section);
        }
        return newSections;
    }

    private void setCover(PictureStory pictureStory, Map<String, ContentSection> contentSections, PublishContentCommand command) {
        ContentSection coverSection = contentSections.get(command.pictureStory.cover.sectionId);
        pictureStory.cover().setContentSection(coverSection);
    }

    private void setContentSections(Content<? extends ComponentPart> content, Map<String, ContentSection> contentSections,
                                    List<? extends PublishContentCommand.ComponentPartDTO> partDTOs) {
        for (int i = 0; i < partDTOs.size(); i++) {
            String key = partDTOs.get(i).sectionId;
            content.componentParts().get(i).setContentSection(contentSections.get(key));
        }
    }

}
