package org.demo.solr.solrtwitterdemo.solr;

import org.apache.solr.client.solrj.beans.Field;
import org.demo.solr.solrtwitterdemo.Tweet;

import lombok.Data;

@Data
public class TweetDocument {

    @Field
    private String text;
    @Field
    private String profileImgUrl;
    @Field
    private String lang;
    @Field
    private String userScreenName;
    @Field
    private String userLocation;

    static TweetDocument fromTweet(Tweet tweet) {

        TweetDocument tweetEntity = new TweetDocument();

        tweetEntity.setText(tweet.getText());
        tweetEntity.setProfileImgUrl(tweet.getProfileImageUrl());
        tweetEntity.setLang(tweet.getLang());

        Tweet.User user = tweet.getUser();
        if (user != null) {
            tweetEntity.setUserScreenName(user.getScreenName());
            tweetEntity.setUserLocation(user.getLocation());
        }

        return tweetEntity;
    }

    Tweet toTweet() {
        Tweet tweet = new Tweet();

        tweet.setText(text);
        tweet.setProfileImageUrl(profileImgUrl);
        tweet.setLang(lang);

        Tweet.User user = new Tweet.User();
        user.setScreenName(userScreenName);
        user.setLocation(userLocation);

        tweet.setUser(user);

        return tweet;
    }
}
