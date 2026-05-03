package com.repoverse.backend.dto;

import lombok.Data;

@Data
public class GithubRepositoryDto {

    private String name;

    private String full_name;

    private String description;

    private int stargazers_count;

    private String language;
}