package com.repoverse.backend.dto;

import lombok.Data;

@Data
public class GithubContributorDto {

    private String login;

    private int contributions;

    private String avatar_url;
}