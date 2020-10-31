package ru.avperm.avsystemtelegram.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("application")
public class TelegramBotSettings {

    private String token;
    private String botName;
}
