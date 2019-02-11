package org.demo.solr.solrtwitterdemo.db;

import org.demo.solr.solrtwitterdemo.Tweet;
import org.demo.solr.solrtwitterdemo.TweetRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
class DbTweetRepository implements TweetRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void add(Tweet tweet) {

        TweetEntity tweetEntity = TweetEntity.fromTweet(tweet);

        entityManager.persist(tweetEntity);

    }

    @Override
    public List<Tweet> findTweetsContainingPhrase(String phrase) {

        TypedQuery<TweetEntity> query = entityManager.createQuery("select t from TweetEntity t where lower(t.text) LIKE lower(:phrase)", TweetEntity.class);
        query.setParameter("phrase", "%" + phrase + "%");

        return query.getResultList()
            .stream()
            .map(TweetEntity::toTweet)
            .collect(Collectors.toList());
    }
}
