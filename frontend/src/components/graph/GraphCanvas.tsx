"use client";

import * as THREE from "three";
import { useMemo, useRef, useState } from "react";
import { Canvas } from "@react-three/fiber";
import { OrbitControls } from "@react-three/drei";
import type { PerspectiveCamera } from "three";
import { EffectComposer, Bloom } from "@react-three/postprocessing";

type Node = {
    id: string;
    label: string;
    x: number;
    y: number;
    z: number;

    // optional future fields (safe for backend expansion)
    type?: string;
    stars?: number;
    description?: string;
    language?: string;
};

type Edge = {
    source: string;
    target: string;
};

type Props = {
    nodes: Node[];
    edges: Edge[];
};

/* ---------------- NODE ---------------- */

function NodeMesh({
    node,
    onClick,
}: {
    node: Node;
    onClick: (node: Node) => void;
}) {
    return (
        <mesh
            position={[node.x, node.y, node.z]}
            onClick={() => onClick(node)}
        >
            <sphereGeometry args={[5, 16, 16]} />
            <meshStandardMaterial
                color="#4f46e5"
                emissive="#4f46e5"
                emissiveIntensity={1.5}
            />
        </mesh>
    );
}

/* ---------------- EDGE ---------------- */

function EdgeLine({ from, to }: { from: Node; to: Node }) {
    const positions = useMemo(
        () =>
            new Float32Array([
                from.x, from.y, from.z,
                to.x, to.y, to.z,
            ]),
        [from, to]
    );

    return (
        <line>
            <bufferGeometry>
                <bufferAttribute
                    attach="attributes-position"
                    args={[positions, 3]}
                />
            </bufferGeometry>
            <lineBasicMaterial color="white" />
        </line>
    );
}

/* ---------------- MAIN ---------------- */

export default function GraphCanvas({ nodes, edges }: Props) {
    const cameraRef = useRef<PerspectiveCamera | null>(null);

    const [selectedNode, setSelectedNode] = useState<Node | null>(null);

    const safeNodes = nodes ?? [];
    const safeEdges = edges ?? [];

    /* CENTER GRAPH */
    const centeredNodes = useMemo(() => {
        if (!safeNodes.length) return [];

        const cx = safeNodes.reduce((s, n) => s + n.x, 0) / safeNodes.length;
        const cy = safeNodes.reduce((s, n) => s + n.y, 0) / safeNodes.length;
        const cz = safeNodes.reduce((s, n) => s + n.z, 0) / safeNodes.length;

        return safeNodes.map((n) => ({
            ...n,
            x: n.x - cx,
            y: n.y - cy,
            z: n.z - cz,
        }));
    }, [safeNodes]);

    /* MAP NODES */
    const nodeMap = useMemo(() => {
        const map = new Map<string, Node>();
        centeredNodes.forEach((n) => map.set(n.id, n));
        return map;
    }, [centeredNodes]);

    return (
        <div style={{ height: "100vh", width: "100%", position: "relative" }}>
            <Canvas
                camera={{ position: [0, 0, 200], fov: 60 }}
                onCreated={({ camera }) => {
                    cameraRef.current = camera as PerspectiveCamera;
                }}
            >
                <ambientLight intensity={0.6} />
                <pointLight position={[100, 100, 100]} />

                {/* NODES */}
                {centeredNodes.map((node) => (
                    <NodeMesh
                        key={node.id}
                        node={node}
                        onClick={(n) => setSelectedNode(n)}
                    />
                ))}

                {/* EDGES */}
                {safeEdges.map((edge, i) => {
                    const from = nodeMap.get(edge.source);
                    const to = nodeMap.get(edge.target);

                    if (!from || !to) return null;

                    return <EdgeLine key={i} from={from} to={to} />;
                })}

                <OrbitControls />

                <EffectComposer>
                    <Bloom intensity={1.2} luminanceThreshold={0.1} />
                </EffectComposer>
            </Canvas>

            {/* ---------------- SIDE PANEL ---------------- */}
            {selectedNode && (
                <div
                    style={{
                        position: "absolute",
                        top: 20,
                        right: 20,
                        width: 320,
                        padding: 16,
                        background: "rgba(0,0,0,0.85)",
                        color: "white",
                        borderRadius: 12,
                        backdropFilter: "blur(10px)",
                    }}
                >
                    <h2>{selectedNode.label}</h2>
                    <p>ID: {selectedNode.id}</p>
                    {selectedNode.type && <p>Type: {selectedNode.type}</p>}
                    {selectedNode.language && <p>Lang: {selectedNode.language}</p>}
                    {selectedNode.stars !== undefined && (
                        <p>⭐ {selectedNode.stars}</p>
                    )}
                    {selectedNode.description && (
                        <p>{selectedNode.description}</p>
                    )}

                    <button
                        onClick={() => setSelectedNode(null)}
                        style={{ marginTop: 10 }}
                    >
                        Close
                    </button>
                </div>
            )}
        </div>
    );
}