package org.demo.solr.solrtwitterdemo.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.demo.solr.solrtwitterdemo.Tweet;
import org.demo.solr.solrtwitterdemo.TweetRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
class SolrTweetRepository implements TweetRepository {

    private final SolrClient solrClient;

    SolrTweetRepository(SolrClient solrClient) {
        this.solrClient = solrClient;
    }

    @Override
    public void add(Tweet tweet) {

        TweetDocument tweetDocument = TweetDocument.fromTweet(tweet);

        try {
            solrClient.addBean(tweetDocument);
            solrClient.commit(true, false, false);
        } catch (IOException | SolrServerException e) {
            log.error("Cannot add tweet to solr", e);
        }
    }

    @Override
    public List<Tweet> findTweetsContainingPhrase(String phrase) {
        SolrQuery solrQuery = new SolrQuery("text:" + phrase);
        try {
            QueryResponse query = solrClient.query(solrQuery);

            return query.getBeans(TweetDocument.class)
                .stream()
                .map(TweetDocument::toTweet)
                .collect(Collectors.toList());
        } catch (SolrServerException | IOException e) {
            log.error("Cannot get solr tweets for phrase: {}", phrase, e);
        }
        return Collections.emptyList();
    }
}
