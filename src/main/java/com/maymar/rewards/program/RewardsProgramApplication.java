package com.maymar.rewards.program;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootApplication
public class RewardsProgramApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RewardsProgramApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Date : " + LocalDate.now().minusMonths(3));
		System.out.println("Time : " + LocalTime.now());
	}
}
