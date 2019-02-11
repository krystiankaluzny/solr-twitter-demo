package org.demo.solr.solrtwitterdemo;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;

import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
class TweetsConsumer {

    private final ObjectMapper objectMapper;
    private final StreamingTwitterClient streamingTwitterClient;

    Disposable disposable;

    TweetsConsumer(ObjectMapper objectMapper, StreamingTwitterClient streamingTwitterClient) {
        this.objectMapper = objectMapper;
        this.streamingTwitterClient = streamingTwitterClient;
    }

    void startConsuming() {

        disposable = streamingTwitterClient.tweets()
            .map(this::parseTweet)
            .filter(Tweet::isValid)
            .subscribe(tweet -> {
                log.debug("{}", tweet);
            });
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
