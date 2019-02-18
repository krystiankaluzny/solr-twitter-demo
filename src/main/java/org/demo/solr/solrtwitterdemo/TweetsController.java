package org.demo.solr.solrtwitterdemo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
class TweetsController {

    private final TweetRepository dbTweetRepository;

    TweetsController(TweetRepository dbTweetRepository) {
        this.dbTweetRepository = dbTweetRepository;
    }

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("phrase", "");
        return "index.html";
    }

    @GetMapping("/solr/find")
    public String findPhraseInSolr(String solrPhrase, Model model) {

        return "index.html";
    }

    @GetMapping("/db/find")
    public String findPhraseInRdbms(String dbPhrase, Model model) {

        List<Tweet> tweets = dbTweetRepository.findTweetsContainingPhrase(dbPhrase);

        model.addAttribute("dbPhrase", dbPhrase);
        model.addAttribute("dbTweets", tweets);

        return "index.html";
    }
}
