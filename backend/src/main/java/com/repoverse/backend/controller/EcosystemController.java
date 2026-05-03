package com.repoverse.backend.controller;

import com.repoverse.backend.dto.EcosystemGraphResponse;
import com.repoverse.backend.service.EcosystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ecosystem")
@RequiredArgsConstructor
public class EcosystemController {

    private final EcosystemService ecosystemService;

    @GetMapping("/{name}")
    public EcosystemGraphResponse getEcosystem(@PathVariable String name) {
        return ecosystemService.buildEcosystemGraph(name);
    }
}