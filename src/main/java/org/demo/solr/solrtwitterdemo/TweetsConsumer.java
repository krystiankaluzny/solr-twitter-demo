package org.demo.solr.solrtwitterdemo;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
class TweetsConsumer {

    private final ObjectMapper objectMapper;
    private final StreamingTwitterClient streamingTwitterClient;
    private final TweetRepository dbTweetRepository;
    private final TweetRepository solrTweetRepository;

    private Disposable disposable;

    TweetsConsumer(ObjectMapper objectMapper,
                   StreamingTwitterClient streamingTwitterClient,
                   TweetRepository dbTweetRepository,
                   TweetRepository solrTweetRepository) {
        this.objectMapper = objectMapper;
        this.streamingTwitterClient = streamingTwitterClient;
        this.dbTweetRepository = dbTweetRepository;
        this.solrTweetRepository = solrTweetRepository;
    }

    void startConsuming() {

        stopStream();

        disposable = streamingTwitterClient.tweets()
            .map(this::parseTweet)
            .filter(Tweet::isValid)
            .subscribe(tweet -> {
                log.debug("{}", tweet);
                dbTweetRepository.add(tweet);
                solrTweetRepository.add(tweet);
            });
    }

    @PreDestroy
    void stopStream() {

        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    private Tweet parseTweet(String tweetString) {

        try {
            return objectMapper.readValue(tweetString, Tweet.class);
        } catch (Exception e) {
            log.error("Cannot parse JSON: {}", tweetString, e);
        }
        return new Tweet();
    }
}
