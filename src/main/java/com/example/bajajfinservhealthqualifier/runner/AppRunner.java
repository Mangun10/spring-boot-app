package com.example.bajajfinservhealthqualifier.runner;

import com.example.bajajfinservhealthqualifier.service.ChallengeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements CommandLineRunner {

    private final ChallengeService challengeService;

    public AppRunner(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Application started, running the challenge...");
        challengeService.solveChallenge();
    }
}