package org.demo.solr.solrtwitterdemo;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class WebConfiguration {

    @Bean
    AsyncHttpClient asyncHttpClient(@Value("${app.read.timeout:10000}") int readTimeout) {

        return new DefaultAsyncHttpClient(
            new DefaultAsyncHttpClientConfig.Builder()
                .setReadTimeout(readTimeout)
                .build()
        );
    }
}
