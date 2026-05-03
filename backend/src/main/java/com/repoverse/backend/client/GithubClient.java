package com.repoverse.backend.client;

import com.repoverse.backend.dto.GithubContributorDto;
import com.repoverse.backend.dto.GithubSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GithubClient {

        private final WebClient webClient;

        @Value("${github.token}")
        private String githubToken;

        public List<GithubContributorDto> getContributors(String owner, String repo) {

                String url = "https://api.github.com/repos/" + owner + "/" + repo + "/contributors";

                GithubContributorDto[] contributors = webClient.get()
                                .uri(url)
                                .header("Authorization", "token " + githubToken)
                                .header("Accept", "application/vnd.github+json")
                                .header("User-Agent", "RepoVerse-App")
                                .retrieve()
                                .bodyToMono(GithubContributorDto[].class)
                                .block();

                return contributors != null ? Arrays.asList(contributors) : List.of();
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
                                .retrieve()
                                .bodyToMono(GithubSearchResponse.class)
                                .block();
        }
}