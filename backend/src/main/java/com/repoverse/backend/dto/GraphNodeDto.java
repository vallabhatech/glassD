package com.repoverse.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphNodeDto {

    private String id;

    private String label;

    private String type;

    private int stars;

    private String language;

    private String description;

    private double score;

    private String hierarchyLevel;

    private String avatarUrl;

    private double x;
    private double y;
    private double z;
}