package org.demo.solr.solrtwitterdemo.db;


import org.demo.solr.solrtwitterdemo.Tweet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;


@Entity
@Data
class TweetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String text;
    private String profileImgUrl;
    private String lang;
    private String userScreenName;
    private String userLocation;

    private TweetEntity() {
    }

    static TweetEntity fromTweet(Tweet tweet) {

        TweetEntity tweetEntity = new TweetEntity();

        tweetEntity.setText(tweet.getText());
        tweetEntity.setProfileImgUrl(tweet.getProfileImageUrl());
        tweetEntity.setLang(tweet.getLang());

        Tweet.User user = tweet.getUser();
        if(user != null) {
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
