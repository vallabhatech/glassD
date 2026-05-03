package com.repoverse.backend.utils;

public class GraphScoreCalculator {

    public static double calculateScore(
            int stars,
            String type) {

        double baseScore = Math.log10(stars + 1) * 10;

        double multiplier = switch (type) {

            case "framework" -> 1.5;

            case "runtime" -> 1.4;

            case "ui-library" -> 1.2;

            case "tooling" -> 1.1;

            default -> 1.0;
        };

        return Math.round(baseScore * multiplier);
    }
}