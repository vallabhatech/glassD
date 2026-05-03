package com.repoverse.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EcosystemGraphResponse {

    private String ecosystem;

    private List<GraphNodeDto> nodes;

    private List<GraphEdgeDto> edges;

    private List<ClusterDto> clusters;
}