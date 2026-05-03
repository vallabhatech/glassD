package com.repoverse.backend.wire;

import java.util.Map;

public interface WireService {
    Object execute(WireAction action, Map<String, Object> params);
}