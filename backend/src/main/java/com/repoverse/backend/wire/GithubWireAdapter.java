package com.repoverse.backend.wire;

import com.repoverse.backend.client.GithubClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GithubWireAdapter implements WireService {

    private final GithubClient githubClient;

    @Override
    public Object execute(WireAction action, Map<String, Object> params) {

        switch (action) {

            case GITHUB_SEARCH_REPOS:
                return githubClient.searchRepositories(
                        (String) params.get("ecosystem"));

            case GITHUB_GET_CONTRIBUTORS:
                return githubClient.getContributors(
                        (String) params.get("owner"),
                        (String) params.get("repo"));

            default:
                throw new IllegalArgumentException("Unsupported action: " + action);
        }
    }
}