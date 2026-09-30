package com.repoverse.backend.service;

import com.repoverse.backend.dto.*;
import com.repoverse.backend.utils.*;
import com.repoverse.backend.wire.WireAction;
import com.repoverse.backend.wire.WireService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EcosystemServiceImpl implements EcosystemService {
    private final WireService wireService;

    @Override
    public EcosystemGraphResponse buildEcosystemGraph(String ecosystem) {
        GithubSearchResponse response = (GithubSearchResponse) wireService.execute(
                WireAction.GITHUB_SEARCH_REPOS, Map.of("ecosystem", ecosystem));

        List<GraphNodeDto> nodes = new ArrayList<>();
        List<GraphEdgeDto> edges = new ArrayList<>();
        Set<String> contributorIds = new HashSet<>();

        for (GithubRepositoryDto repo : Optional.ofNullable(response.getItems()).orElse(List.of())) {
            String type = RepositoryClassifier.classify(repo.getName(), repo.getDescription());
            double score = GraphScoreCalculator.calculateScore(repo.getStargazers_count(), type);

            nodes.add(GraphNodeDto.builder()
                    .id(repo.getFull_name())
                    .label(repo.getName())
                    .type(type)
                    .stars(repo.getStargazers_count())
                    .language(repo.getLanguage())
                    .description(repo.getDescription())
                    .score(score)
                    .hierarchyLevel(HierarchyAnalyzer.determineHierarchy(score))
                    .build());
        }

        for (GithubRepositoryDto repo : Optional.ofNullable(response.getItems()).orElse(List.of()).stream().limit(3).toList()) {
            String[] parts = repo.getFull_name().split("/", 2);
            if (parts.length != 2) continue;

            List<GithubContributorDto> contributors = (List<GithubContributorDto>) wireService.execute(
                    WireAction.GITHUB_GET_CONTRIBUTORS, Map.of("owner", parts[0], "repo", parts[1]));

            for (GithubContributorDto contributor : contributors.stream().limit(3).toList()) {
                if (contributorIds.add(contributor.getLogin())) {
                    nodes.add(GraphNodeDto.builder()
                            .id(contributor.getLogin())
                            .label(contributor.getLogin())
                            .type("contributor")
                            .score(contributor.getContributions())
                            .hierarchyLevel("contributor")
                            .avatarUrl(contributor.getAvatar_url())
                            .build());
                }
                edges.add(GraphEdgeDto.builder()
                        .source(contributor.getLogin())
                        .target(repo.getFull_name())
                        .relationship("contributes-to")
                        .build());
            }
        }

        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                GraphNodeDto source = nodes.get(i);
                GraphNodeDto target = nodes.get(j);
                if ("contributor".equals(source.getType()) || "contributor".equals(target.getType())) continue;

                String relationship = RelationshipAnalyzer.determineRelationship(source.getType(), target.getType());
                if (!"related".equals(relationship)) {
                    edges.add(GraphEdgeDto.builder()
                            .source(source.getId())
                            .target(target.getId())
                            .relationship(relationship)
                            .build());
                }
            }
        }

        List<ClusterDto> clusters = nodes.stream()
                .collect(java.util.stream.Collectors.groupingBy(GraphNodeDto::getType, LinkedHashMap::new, java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .map(entry -> ClusterDto.builder().name(entry.getKey()).nodeCount(entry.getValue().intValue()).build())
                .toList();

        GraphForceEngine.applyForces(nodes, edges);

        return EcosystemGraphResponse.builder()
                .ecosystem(ecosystem)
                .nodes(nodes)
                .edges(edges)
                .clusters(clusters)
                .build();
    }
}
