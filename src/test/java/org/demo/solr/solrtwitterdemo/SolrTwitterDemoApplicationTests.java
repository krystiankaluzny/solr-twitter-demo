package org.demo.solr.solrtwitterdemo;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SolrTwitterDemoApplicationTests {

    @Autowired
    private TweetRepository dbTweetRepository;

    @Autowired
    private TweetRepository solrTweetRepository;

    @Test
    public void searchingOnSolrShouldReturnTweetsWithMatchingWords() {

        solrTweetRepository.add(createTweet("Some text with abc"));
        solrTweetRepository.add(createTweet("Some aBc int text"));
        solrTweetRepository.add(createTweet("And abcde"));

        List<Tweet> tweets = solrTweetRepository.findTweetsContainingPhrase("abc");

        Assertions.assertThat(tweets)
            .hasSize(2)
            .extracting(Tweet::getText)
            .containsExactlyInAnyOrder(
                "Some text with abc",
                "Some aBc int text");
    }

    @Test
    public void searchingOnRDBMSShouldReturnAllTweetsContainingPhrase() {

        dbTweetRepository.add(createTweet("Some text with abc"));
        dbTweetRepository.add(createTweet("Some aBc int text"));
        dbTweetRepository.add(createTweet("And abcde"));

        List<Tweet> tweets = dbTweetRepository.findTweetsContainingPhrase("abc");

        Assertions.assertThat(tweets)
            .hasSize(3)
            .extracting(Tweet::getText)
            .containsExactlyInAnyOrder(
                "Some text with abc",
                "Some aBc int text",
                "And abcde");
    }

    private Tweet createTweet(String text) {
        Tweet tweet = new Tweet();
        tweet.setText(text);
        return tweet;
    }
}

