package com.repoverse.backend.utils;

public class HierarchyAnalyzer {

    public static String determineHierarchy(double score) {

        if (score >= 80) {
            return "core";
        }

        if (score >= 65) {
            return "major";
        }

        if (score >= 50) {
            return "standard";
        }

        return "minor";
    }
}