package com.repoverse.backend.utils;

public class RepositoryClassifier {

    public static String classify(
            String repoName,
            String description) {

        String name = repoName == null
                ? ""
                : repoName.toLowerCase();

        String desc = description == null
                ? ""
                : description.toLowerCase();

        String combined = name + " " + desc;

        /*
         * MOBILE FRAMEWORKS
         */
        if (name.contains("react-native")) {
            return "mobile-framework";
        }

        /*
         * UI LIBRARIES
         */
        if (combined.contains("ui") ||
                combined.contains("component") ||
                combined.contains("design system") ||
                combined.contains("react ui")) {

            return "ui-library";
        }

        /*
         * RUNTIMES
         */
        if (combined.contains("runtime")) {
            return "runtime";
        }

        /*
         * TOOLING
         */
        if (combined.contains("tool") ||
                combined.contains("cli") ||
                combined.contains("build tool")) {

            return "tooling";
        }

        /*
         * FRAMEWORKS
         */
        if (combined.contains("framework") ||
                combined.contains("react")) {

            return "framework";
        }

        return "repository";
    }
}