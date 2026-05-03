package com.repoverse.backend.utils;

import com.repoverse.backend.dto.GraphEdgeDto;
import com.repoverse.backend.dto.GraphNodeDto;

import java.util.List;

public class GraphForceEngine {

    private static final double REPULSION = 8000;
    private static final double SPRING = 0.02;
    private static final double DAMPING = 0.85;
    private static final int ITERATIONS = 80;

    public static void applyForces(List<GraphNodeDto> nodes, List<GraphEdgeDto> edges) {

        // init positions
        for (GraphNodeDto n : nodes) {
            n.setX(Math.random() * 400 - 200);
            n.setY(Math.random() * 400 - 200);
            n.setZ(Math.random() * 200 - 100);
        }

        for (int iter = 0; iter < ITERATIONS; iter++) {

            double[] fx = new double[nodes.size()];
            double[] fy = new double[nodes.size()];
            double[] fz = new double[nodes.size()];

            // ------------------------
            // REPULSION (all nodes)
            // ------------------------
            for (int i = 0; i < nodes.size(); i++) {
                for (int j = i + 1; j < nodes.size(); j++) {

                    GraphNodeDto a = nodes.get(i);
                    GraphNodeDto b = nodes.get(j);

                    double dx = a.getX() - b.getX();
                    double dy = a.getY() - b.getY();
                    double dz = a.getZ() - b.getZ();

                    double dist = Math.max(1, Math.sqrt(dx * dx + dy * dy + dz * dz));

                    double force = REPULSION / (dist * dist);

                    double fxDir = (dx / dist) * force;
                    double fyDir = (dy / dist) * force;
                    double fzDir = (dz / dist) * force;

                    fx[i] += fxDir;
                    fy[i] += fyDir;
                    fz[i] += fzDir;

                    fx[j] -= fxDir;
                    fy[j] -= fyDir;
                    fz[j] -= fzDir;
                }
            }

            // ------------------------
            // SPRING FORCE (edges)
            // ------------------------
            for (GraphEdgeDto e : edges) {

                GraphNodeDto source = find(nodes, e.getSource());
                GraphNodeDto target = find(nodes, e.getTarget());

                if (source == null || target == null)
                    continue;

                double dx = target.getX() - source.getX();
                double dy = target.getY() - source.getY();
                double dz = target.getZ() - source.getZ();

                fx[nodes.indexOf(source)] += dx * SPRING;
                fy[nodes.indexOf(source)] += dy * SPRING;
                fz[nodes.indexOf(source)] += dz * SPRING;

                fx[nodes.indexOf(target)] -= dx * SPRING;
                fy[nodes.indexOf(target)] -= dy * SPRING;
                fz[nodes.indexOf(target)] -= dz * SPRING;
            }

            // ------------------------
            // APPLY
            // ------------------------
            for (int i = 0; i < nodes.size(); i++) {

                GraphNodeDto n = nodes.get(i);

                n.setX(n.getX() + fx[i] * DAMPING);
                n.setY(n.getY() + fy[i] * DAMPING);
                n.setZ(n.getZ() + fz[i] * DAMPING);
            }
        }
    }

    private static GraphNodeDto find(List<GraphNodeDto> nodes, String id) {
        for (GraphNodeDto n : nodes) {
            if (n.getId().equals(id))
                return n;
        }
        return null;
    }
}