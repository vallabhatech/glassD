package com.repoverse.backend.service;

import com.repoverse.backend.dto.EcosystemGraphResponse;

public interface EcosystemService {

    EcosystemGraphResponse buildEcosystemGraph(String ecosystem);
}