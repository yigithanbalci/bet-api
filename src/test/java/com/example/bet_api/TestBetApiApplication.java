package com.example.bet_api;

import org.springframework.boot.SpringApplication;

public class TestBetApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(BetApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
