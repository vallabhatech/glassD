package com.repoverse.backend.utils;

import com.repoverse.backend.dto.GraphNodeDto;

import java.util.List;

public class GraphPositionEngine {

    public static void assignPositions(List<GraphNodeDto> nodes) {

        double frameworkAngle = 0;
        double uiAngle = 0;
        double runtimeAngle = 0;
        double contributorAngle = 0;

        double radiusFramework = 120;
        double radiusUI = 220;
        double radiusRuntime = 320;
        double radiusContributor = 400;

        for (GraphNodeDto node : nodes) {

            switch (node.getType()) {

                case "framework" -> {
                    double angle = frameworkAngle;
                    node.setX(radiusFramework * Math.cos(angle));
                    node.setY(radiusFramework * Math.sin(angle));
                    node.setZ(20);
                    frameworkAngle += Math.PI / 4;
                }

                case "ui-library" -> {
                    double angle = uiAngle;
                    node.setX(radiusUI * Math.cos(angle));
                    node.setY(radiusUI * Math.sin(angle));
                    node.setZ(-20);
                    uiAngle += Math.PI / 5;
                }

                case "runtime" -> {
                    double angle = runtimeAngle;
                    node.setX(radiusRuntime * Math.cos(angle));
                    node.setY(radiusRuntime * Math.sin(angle));
                    node.setZ(40);
                    runtimeAngle += Math.PI / 3;
                }

                case "contributor" -> {
                    double angle = contributorAngle;
                    node.setX(radiusContributor * Math.cos(angle));
                    node.setY(radiusContributor * Math.sin(angle));
                    node.setZ(-40);
                    contributorAngle += Math.PI / 6;
                }

                default -> {
                    node.setX(Math.random() * 50);
                    node.setY(Math.random() * 50);
                    node.setZ(0);
                }
            }
        }
    }
}