package com.repoverse.backend.controller;

import com.repoverse.backend.dto.EcosystemGraphResponse;
import com.repoverse.backend.service.EcosystemService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ecosystem")
@RequiredArgsConstructor
@Validated
public class EcosystemController {
    private final EcosystemService ecosystemService;

    @GetMapping("/{name}")
    public EcosystemGraphResponse getEcosystem(
            @PathVariable
            @Pattern(regexp = "^[a-zA-Z0-9._-]{1,80}$", message = "Invalid ecosystem name")
            String name) {
        return ecosystemService.buildEcosystemGraph(name.trim());
    }
}
