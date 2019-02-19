package org.demo.solr.solrtwitterdemo;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

@Configuration
class AppConfiguration {

    @Bean
    AsyncHttpClient asyncHttpClient(
        @Value("${app.read.timeout:1000}") int readTimeout,
        @Value("${app.connect.timeout:10000}") int connectTimeout,
        @Value("${app.request.timeout:10000}") int requestTimeout) {

        return new DefaultAsyncHttpClient(
            new DefaultAsyncHttpClientConfig.Builder()
                .setReadTimeout(readTimeout)
                .setConnectTimeout(connectTimeout)
                .setRequestTimeout(requestTimeout)
                .build()
        );
    }

    @Bean
    SolrClient solrClient() throws URISyntaxException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("solr");
        Path path = new File(url.toURI()).toPath();
        return new EmbeddedSolrServer(path, "tweets");
    }
}
