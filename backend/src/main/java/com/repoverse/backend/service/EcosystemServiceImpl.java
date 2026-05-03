package com.repoverse.backend.service;

import com.repoverse.backend.client.GithubClient;
import com.repoverse.backend.dto.ClusterDto;
import com.repoverse.backend.dto.EcosystemGraphResponse;
import com.repoverse.backend.dto.GithubContributorDto;
import com.repoverse.backend.dto.GithubRepositoryDto;
import com.repoverse.backend.dto.GithubSearchResponse;
import com.repoverse.backend.dto.GraphEdgeDto;
import com.repoverse.backend.dto.GraphNodeDto;
import com.repoverse.backend.utils.GraphForceEngine;
import com.repoverse.backend.utils.GraphPositionEngine;
import com.repoverse.backend.utils.GraphScoreCalculator;
import com.repoverse.backend.utils.HierarchyAnalyzer;
import com.repoverse.backend.utils.RelationshipAnalyzer;
import com.repoverse.backend.utils.RepositoryClassifier;
import com.repoverse.backend.wire.WireAction;
import com.repoverse.backend.wire.WireService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EcosystemServiceImpl implements EcosystemService {

        private final WireService wireService;

        @Override
        public EcosystemGraphResponse buildEcosystemGraph(String ecosystem) {

                GithubSearchResponse response = (GithubSearchResponse) wireService.execute(
                                WireAction.GITHUB_SEARCH_REPOS,
                                Map.of("ecosystem", ecosystem));

                List<GraphNodeDto> nodes = new ArrayList<>();

                List<GraphEdgeDto> edges = new ArrayList<>();

                Set<String> contributorIds = new HashSet<>();

                /*
                 * --------------------------------
                 * REPOSITORY NODES
                 * --------------------------------
                 */

                for (GithubRepositoryDto repo : response.getItems()) {

                        String type = RepositoryClassifier.classify(
                                        repo.getName(),
                                        repo.getDescription());

                        double score = GraphScoreCalculator.calculateScore(
                                        repo.getStargazers_count(),
                                        type);

                        String hierarchy = HierarchyAnalyzer.determineHierarchy(score);

                        GraphNodeDto node = GraphNodeDto.builder()
                                        .id(repo.getFull_name())
                                        .label(repo.getName())
                                        .type(type)
                                        .stars(repo.getStargazers_count())
                                        .language(repo.getLanguage())
                                        .description(repo.getDescription())
                                        .score(score)
                                        .hierarchyLevel(hierarchy)
                                        .avatarUrl(null)
                                        .build();

                        nodes.add(node);
                }

                /*
                 * --------------------------------
                 * CONTRIBUTOR NODES
                 * --------------------------------
                 */

                for (GithubRepositoryDto repo : response.getItems().stream().limit(3).toList()) {

                        String[] parts = repo.getFull_name().split("/");

                        String owner = parts[0];
                        String repoName = parts[1];

                        List<GithubContributorDto> contributors = (List<GithubContributorDto>) wireService.execute(
                                        WireAction.GITHUB_GET_CONTRIBUTORS,
                                        Map.of(
                                                        "owner", owner,
                                                        "repo", repoName));

                        for (GithubContributorDto contributor : contributors.stream().limit(3).toList()) {

                                if (!contributorIds.contains(contributor.getLogin())) {

                                        GraphNodeDto contributorNode = GraphNodeDto.builder()
                                                        .id(contributor.getLogin())
                                                        .label(contributor.getLogin())
                                                        .type("contributor")
                                                        .score(contributor.getContributions())
                                                        .hierarchyLevel("contributor")
                                                        .avatarUrl(contributor.getAvatar_url())
                                                        .build();

                                        nodes.add(contributorNode);

                                        contributorIds.add(contributor.getLogin());
                                }

                                GraphEdgeDto contributionEdge = GraphEdgeDto.builder()
                                                .source(contributor.getLogin())
                                                .target(repo.getFull_name())
                                                .relationship("contributes-to")
                                                .build();

                                edges.add(contributionEdge);
                        }
                }

                /*
                 * --------------------------------
                 * SEMANTIC RELATIONSHIPS
                 * --------------------------------
                 */

                for (int i = 0; i < nodes.size(); i++) {

                        for (int j = i + 1; j < nodes.size(); j++) {

                                GraphNodeDto source = nodes.get(i);
                                GraphNodeDto target = nodes.get(j);

                                /*
                                 * skip contributor auto-linking
                                 */

                                if (source.getType().equals("contributor")
                                                || target.getType().equals("contributor")) {

                                        continue;
                                }

                                String relationship = RelationshipAnalyzer.determineRelationship(
                                                source.getType(),
                                                target.getType());

                                if (!relationship.equals("related")) {

                                        GraphEdgeDto edge = GraphEdgeDto.builder()
                                                        .source(source.getId())
                                                        .target(target.getId())
                                                        .relationship(relationship)
                                                        .build();

                                        edges.add(edge);
                                }
                        }
                }

                /*
                 * --------------------------------
                 * CLUSTERS
                 * --------------------------------
                 */

                List<ClusterDto> clusters = new ArrayList<>();

                long frameworkCount = nodes.stream()
                                .filter(node -> node.getType().equals("framework"))
                                .count();

                long uiCount = nodes.stream()
                                .filter(node -> node.getType().equals("ui-library"))
                                .count();

                long runtimeCount = nodes.stream()
                                .filter(node -> node.getType().equals("runtime"))
                                .count();

                long toolingCount = nodes.stream()
                                .filter(node -> node.getType().equals("tooling"))
                                .count();

                long contributorCount = nodes.stream()
                                .filter(node -> node.getType().equals("contributor"))
                                .count();

                if (frameworkCount > 0) {

                        clusters.add(
                                        ClusterDto.builder()
                                                        .name("frameworks")
                                                        .nodeCount((int) frameworkCount)
                                                        .build());
                }

                if (uiCount > 0) {

                        clusters.add(
                                        ClusterDto.builder()
                                                        .name("ui-libraries")
                                                        .nodeCount((int) uiCount)
                                                        .build());
                }

                if (runtimeCount > 0) {

                        clusters.add(
                                        ClusterDto.builder()
                                                        .name("runtimes")
                                                        .nodeCount((int) runtimeCount)
                                                        .build());
                }

                if (toolingCount > 0) {

                        clusters.add(
                                        ClusterDto.builder()
                                                        .name("tooling")
                                                        .nodeCount((int) toolingCount)
                                                        .build());
                }

                if (contributorCount > 0) {

                        clusters.add(
                                        ClusterDto.builder()
                                                        .name("contributors")
                                                        .nodeCount((int) contributorCount)
                                                        .build());
                }

                GraphPositionEngine.assignPositions(nodes);

                GraphPositionEngine.assignPositions(nodes);
                GraphForceEngine.applyForces(nodes, edges);

                /*
                 * --------------------------------
                 * FINAL RESPONSE
                 * --------------------------------
                 */

                return EcosystemGraphResponse.builder()
                                .ecosystem(ecosystem)
                                .nodes(nodes)
                                .edges(edges)
                                .clusters(clusters)
                                .build();
        }
}