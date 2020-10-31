package ru.avperm.avsystemtelegram.config;

import feign.Logger;
import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusSpravsApiClientConfiguration {

    @Bean
    public Logger.Level avClientLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder avClienterrorDecoder() {
        return new ErrorDecoder.Default();
    }

    @Bean
    public OkHttpClient avClient() {
        return new OkHttpClient();
    }
}
