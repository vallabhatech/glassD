package com.repoverse.backend.utils;

public class RelationshipAnalyzer {

    public static String determineRelationship(
            String sourceType,
            String targetType) {

        /*
         * UI libraries orbit frameworks
         */
        if (sourceType.equals("framework")
                && targetType.equals("ui-library")) {

            return "uses-ui-library";
        }

        /*
         * Frameworks depend on runtimes
         */
        if (sourceType.equals("framework")
                && targetType.equals("runtime")) {

            return "runs-on";
        }

        /*
         * Tooling supports frameworks
         */
        if (sourceType.equals("tooling")
                && targetType.equals("framework")) {

            return "supports";
        }

        /*
         * Similar ecosystem type
         */
        if (sourceType.equals(targetType)) {
            return "same-category";
        }

        return "related";
    }
}