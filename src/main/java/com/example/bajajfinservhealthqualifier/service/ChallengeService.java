package com.example.bajajfinservhealthqualifier.service;

import com.example.bajajfinservhealthqualifier.dto.SolutionRequest;
import com.example.bajajfinservhealthqualifier.dto.WebhookRequest;
import com.example.bajajfinservhealthqualifier.dto.WebhookResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ChallengeService {

    private final WebClient webClient;

    @Value("${api.base.url}")
    private String apiBaseUrl;

    public ChallengeService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(apiBaseUrl).build();
    }

    public void solveChallenge() {
        // Step 1: Generate Webhook
        generateWebhook()
            .flatMap(response -> {
                // Step 2: Solve SQL problem and submit
                String sqlQuery = solveSqlProblem();
                return submitSolution(response.getWebhook(), response.getAccessToken(), sqlQuery);
            })
            .subscribe(
                result -> System.out.println("Successfully submitted solution: " + result),
                error -> System.err.println("Error during challenge: " + error.getMessage())
            );
    }

    private Mono<WebhookResponse> generateWebhook() {
        WebhookRequest request = new WebhookRequest("Mangun10", "REG12347", "mangun10@example.com");
        System.out.println("Generating webhook with request: " + request);

        return webClient.post()
            .uri("/generateWebhook/JAVA")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), WebhookRequest.class)
            .retrieve()
            .bodyToMono(WebhookResponse.class)
            .doOnSuccess(response -> System.out.println("Webhook generated: " + response));
    }

    private String solveSqlProblem() {
        // Placeholder for the SQL query.
        // The registration number "REG12347" ends with 47 (odd).
        // I will add the actual query here once you provide the problem description.
        System.out.println("Solving SQL problem for odd registration number...");
        return "SELECT your_column FROM your_table WHERE your_condition;";
    }

    private Mono<String> submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        SolutionRequest solutionRequest = new SolutionRequest(sqlQuery);
        System.out.println("Submitting solution: " + solutionRequest);
        System.out.println("Webhook URL: " + webhookUrl);

        // The submission URL is the base URL + the webhook path from the response
        // As per instructions, the submission URL is constant.
        String submissionUrl = "/testWebhook/JAVA";

        return WebClient.create(apiBaseUrl).post()
            .uri(submissionUrl)
            .header(HttpHeaders.AUTHORIZATION, accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(solutionRequest), SolutionRequest.class)
            .retrieve()
            .bodyToMono(String.class);
    }
}