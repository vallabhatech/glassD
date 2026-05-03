package com.repoverse.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class GithubSearchResponse {

    private List<GithubRepositoryDto> items;
}