package org.demo.solr.solrtwitterdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class SolrTwitterDemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SolrTwitterDemoApplication.class, args);
    }

    private final TweetsConsumer tweetsConsumer;

    @Autowired
    public SolrTwitterDemoApplication(TweetsConsumer tweetsConsumer) {
        this.tweetsConsumer = tweetsConsumer;
    }

    @Override
    public void run(String... args) throws Exception {

        tweetsConsumer.startConsuming();
    }
}

