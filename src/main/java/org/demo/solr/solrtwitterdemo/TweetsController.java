package org.demo.solr.solrtwitterdemo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
class TweetsController {

    private final TweetRepository dbTweetRepository;
    private final TweetRepository solrTweetRepository;

    TweetsController(TweetRepository dbTweetRepository, TweetRepository solrTweetRepository) {
        this.dbTweetRepository = dbTweetRepository;
        this.solrTweetRepository = solrTweetRepository;
    }

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("phrase", "");
        return "index.html";
    }

    @GetMapping("/find/solr")
    public String findPhraseInSolr(String phrase, Model model) {

        List<Tweet> tweets = getTweets(phrase, solrTweetRepository);

        model.addAttribute("phrase", phrase);
        model.addAttribute("solrTweets", tweets);

        return "index.html";
    }



    @GetMapping("/find/db")
    public String findPhraseInRdbms(String phrase, Model model) {

        List<Tweet> tweets = getTweets(phrase, dbTweetRepository);

        model.addAttribute("phrase", phrase);
        model.addAttribute("dbTweets", tweets);

        return "index.html";
    }

    @GetMapping("/find")
    public String findPhrase(String phrase, Model model) {

        List<Tweet> dbTweets = getTweets(phrase, dbTweetRepository);
        List<Tweet> solrTweets = getTweets(phrase, solrTweetRepository);

        model.addAttribute("phrase", phrase);
        model.addAttribute("dbTweets", dbTweets);
        model.addAttribute("solrTweets", solrTweets);

        return "index.html";
    }

    private List<Tweet> getTweets(String phrase, TweetRepository solrTweetRepository) {
        List<Tweet> tweets = Collections.emptyList();
        if (StringUtils.isNotEmpty(phrase)) {
            tweets = solrTweetRepository.findTweetsContainingPhrase(phrase);
        }
        return tweets;
    }
}
