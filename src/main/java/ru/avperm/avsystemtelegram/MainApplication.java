package ru.avperm.avsystemtelegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableFeignClients
@EnableAutoConfiguration
public class MainApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(MainApplication.class, args);

	}

}
