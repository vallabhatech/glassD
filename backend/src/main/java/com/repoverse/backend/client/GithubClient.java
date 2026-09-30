package com.repoverse.backend.client;

import com.repoverse.backend.dto.GithubContributorDto;
import com.repoverse.backend.dto.GithubSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GithubClient {
    private final WebClient webClient;

    @Value("${github.token:}")
    private String githubToken;

    public List<GithubContributorDto> getContributors(String owner, String repo) {
        GithubContributorDto[] contributors = webClient.get()
                .uri("/repos/{owner}/{repo}/contributors", owner, repo)
                .headers(this::applyHeaders)
                .retrieve()
                .bodyToMono(GithubContributorDto[].class)
                .block();

        return contributors == null ? List.of() : Arrays.asList(contributors);
    }

    public GithubSearchResponse searchRepositories(String ecosystem) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/repositories")
                        .queryParam("q", ecosystem)
                        .queryParam("sort", "stars")
                        .queryParam("order", "desc")
                        .queryParam("per_page", 10)
                        .build())
                .headers(this::applyHeaders)
                .retrieve()
                .bodyToMono(GithubSearchResponse.class)
                .block();
    }

    private void applyHeaders(HttpHeaders headers) {
        headers.set(HttpHeaders.ACCEPT, "application/vnd.github+json");
        headers.set(HttpHeaders.USER_AGENT, "OpenSource-Galaxy/1.0");
        if (!githubToken.isBlank()) {
            headers.setBearerAuth(githubToken);
        }
    }
}
