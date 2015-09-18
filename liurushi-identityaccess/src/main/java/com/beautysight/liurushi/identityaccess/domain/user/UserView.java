/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.user;

import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.Beans;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.Preconditions;
import org.springframework.util.Assert;

/**
 * @author chenlong
 * @since 1.0
 */
public class UserView extends ValueObject {

    private String id;
    private String globalId;
    private String nickname;
    private Gender gender;
    private String mobile;
    private String email;
    private User.Group group;
    private User.Origin origin;

    private String originalAvatarUrl;
    private String maxAvatarUrl;
    private String headerPhotoUrl;

    private User.Stats stats;

    private UserView() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Lite lite() {
        return new Lite();
    }

    public LiteAndStats liteAndStats() {
        return new LiteAndStats();
    }

    public Whole whole() {
        return new Whole();
    }

    public class Lite {

        public String getId() {
            return UserView.this.id;
        }

        public String getNickname() {
            return UserView.this.nickname;
        }

        public User.Group getGroup() {
            return group;
        }

        public String getMaxAvatarUrl() {
            return UserView.this.maxAvatarUrl;
        }

        @Override
        public String toString() {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("{id:").append(id)
                    .append(",mobile:").append(mobile)
                    .append(",...}");
            return strBuilder.toString();
        }

    }

    public class LiteAndStats extends Lite {

        public User.Stats getStats() {
            return UserView.this.stats;
        }

    }

    @JsonPropertyOrder({"id", "globalId", "nickname", "group", "origin", "gender", "mobile", "email", "maxAvatarUrl", "headerPhotoUrl", "isFollowed", "stats"})
    public class Whole extends LiteAndStats {

        public String getGlobalId() {
            return globalId;
        }

        public Gender getGender() {
            return gender;
        }

        public String getMobile() {
            return mobile;
        }

        public String getEmail() {
            return email;
        }

        public User.Origin getOrigin() {
            return origin;
        }

        public String getOriginalAvatarUrl() {
            return originalAvatarUrl;
        }

        public String getMaxAvatarUrl() {
            return maxAvatarUrl;
        }

        public String getHeaderPhotoUrl() {
            return headerPhotoUrl;
        }

    }

    public static class Builder {

        private UserView userView = new UserView();
        private boolean isComplete = false;

        private Builder() {
        }

        public Builder copyFrom(User user) {
            Assert.notNull(user);
            checkState();
            Beans.copyProperties(user, userView);
            userView.id = user.idStr();
            return this;
        }

        public Builder setMaxAvatarUrl(String url) {
            checkState();
            this.userView.maxAvatarUrl = url;
            return this;
        }

        public Builder setHeaderPhotoUrl(String url) {
            checkState();
            this.userView.headerPhotoUrl = url;
            return this;
        }

        public UserView build() {
            checkState();
            this.isComplete = true;
            return this.userView;
        }

        private void checkState() {
            Preconditions.checkState((!isComplete), "Already completed the building work, so can't rebuild");
        }

    }

}
