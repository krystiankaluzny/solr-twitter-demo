package org.demo.solr.solrtwitterdemo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Tweet {

    @JsonProperty("text")
    private String text;

    @JsonProperty("profile_image_url")
    private String profileImageUrl;

    @JsonProperty("lang")
    private String lang;

    @JsonProperty("user")
    private User user = new User();

    public boolean isValid() {
        return StringUtils.isNoneEmpty(text, lang, user.getScreenName(), user.getLocation());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class User {

        @JsonProperty("screen_name")
        private String screenName;

        @JsonProperty("location")
        private String location;
    }
}