package org.demo.solr.solrtwitterdemo;

import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.oauth.ConsumerKey;
import org.asynchttpclient.oauth.OAuthSignatureCalculator;
import org.asynchttpclient.oauth.RequestToken;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeoutException;

import io.netty.handler.codec.http.HttpHeaders;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
class StreamingTwitterClient implements AutoCloseable {

    private final AsyncHttpClient httpClient;
    private final Observable<String> tweetStream;

    public StreamingTwitterClient(AsyncHttpClient httpClient) {

        this.httpClient = httpClient;
        this.tweetStream = Observable.create(observableEmitter -> connectToTweeterStream(new StreamingObservableHandler(observableEmitter)));
    }

    Observable<String> tweets() {

        return tweetStream;
    }

    private void connectToTweeterStream(AsyncHandler<Object> asyncHandler) {

        final OAuthSignatureCalculator calculator = getOAuthSignatureCalculator();

        httpClient
            .prepareGet("https://stream.twitter.com/1.1/statuses/sample.json")
            .setSignatureCalculator(calculator)
            .execute(asyncHandler);
    }

    private OAuthSignatureCalculator getOAuthSignatureCalculator() {

        final ConsumerKey consumerKey = new ConsumerKey("UXTi7xA1mxrQawuhUVRAcBsmF", "ZXAJRSEnCc6bansHVlcVHtfjnQACcdzJ6VPudroEUufGcePCtm");
        final RequestToken requestToken = new RequestToken("1295001146-W7oX12GXjQ4Ef2kRZlVvJMEf6HoP4oqak4jrc81", "7gmtXuXYavfjMvjuwnVQ71dNuFGc1dZk3hWyGSTaMDMcH");
        return new OAuthSignatureCalculator(consumerKey, requestToken);
    }

    @Override
    public void close() throws Exception {

        httpClient.close();
    }

    private class StreamingObservableHandler implements AsyncHandler<Object> {

        private final ObservableEmitter<String> emitter;
        private StringBuffer builder;

        StreamingObservableHandler(ObservableEmitter<String> emitter) {
            this.emitter = emitter;
            builder = new StringBuffer();
        }

        @Override
        public State onStatusReceived(HttpResponseStatus httpResponseStatus) throws Exception {
            return State.CONTINUE;
        }

        @Override
        public State onHeadersReceived(HttpHeaders httpHeaders) throws Exception {
            return State.CONTINUE;
        }

        @Override
        public State onBodyPartReceived(HttpResponseBodyPart httpResponseBodyPart) throws Exception {

            ByteBuffer byteBuffer = httpResponseBodyPart.getBodyByteBuffer();

            if (emitter.isDisposed()) {
                return State.ABORT;
            }

            while (byteBuffer.remaining() > 0) {
                byte character = byteBuffer.get();
                if (character == '\n') {
                    emitter.onNext(builder.toString());
                    builder = new StringBuffer();
                }
                else {
                    builder.append((char) character);
                }
            }

            return State.CONTINUE;
        }

        @Override
        public void onThrowable(Throwable throwable) {

            if (throwable instanceof TimeoutException) {
                log.info("Timeout");
                connectToTweeterStream(this);
            }
            else {
                log.error("Some error", throwable);
                emitter.onError(throwable);
            }
        }

        @Override
        public Object onCompleted() throws Exception {

            if (emitter.isDisposed()) {
                if (builder.length() != 0) {
                    emitter.onNext(builder.toString());
                }
                emitter.onComplete();
            }

            return null;
        }
    }
}
