package org.demo.solr.solrtwitterdemo;

import java.util.List;

public interface TweetRepository {

    void add(Tweet tweet);

    List<Tweet> findTweetsContainingPhrase(String phrase);
}
