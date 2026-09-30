"use client";

import { useMemo, useState } from "react";
import { Canvas } from "@react-three/fiber";
import { OrbitControls, PerspectiveCamera, Stars } from "@react-three/drei";
import { EffectComposer, Bloom } from "@react-three/postprocessing";
import NodeMesh from "@/components/graph/Node";
import EdgeLine from "@/components/graph/Edge";
import type { EcosystemGraph, GraphNode } from "@/types/ecosystem";
import styles from "./styles.module.scss";

export default function GraphCanvas({ data }: { data: EcosystemGraph }) {
  const [selectedNode, setSelectedNode] = useState<GraphNode | null>(null);
  const nodeMap = useMemo(() => new Map(data.nodes.map((node) => [node.id, node])), [data.nodes]);

  return (
    <div style={{ position: "relative", width: "100%", height: "100vh" }}>
      <Canvas dpr={[1, 1.75]} gl={{ antialias: true }}>
        <PerspectiveCamera makeDefault position={[0, 0, 210]} fov={60} />
        <color attach="background" args={["#05060a"]} />
        <ambientLight intensity={0.65} />
        <pointLight position={[100, 120, 160]} intensity={1600} decay={0} />
        <Stars radius={220} depth={100} count={1200} factor={2} saturation={0} fade />
        {data.edges.map((edge, index) => {
          const from = nodeMap.get(edge.source);
          const to = nodeMap.get(edge.target);
          if (!from || !to) return null;
          return <EdgeLine key={`${edge.source}-${edge.target}-${index}`} from={from} to={to} relationship={edge.relationship} />;
        })}
        {data.nodes.map((node) => (
          <NodeMesh key={node.id} node={node} selected={selectedNode?.id === node.id} onSelect={setSelectedNode} />
        ))}
        <OrbitControls enableDamping dampingFactor={0.08} minDistance={50} maxDistance={500} />
        <EffectComposer><Bloom intensity={1.1} luminanceThreshold={0.2} mipmapBlur /></EffectComposer>
      </Canvas>

      <header style={{ position: "absolute", zIndex: 5, top: 20, left: 20, pointerEvents: "none" }}>
        <div style={{ color: "#b2a9ff", fontSize: 12, fontWeight: 700, letterSpacing: ".16em" }}>ECOSYSTEM</div>
        <h1 style={{ margin: "6px 0", fontSize: 28 }}>{data.ecosystem}</h1>
        <p style={{ margin: 0, color: "#9ba3b4" }}>{data.nodes.length} nodes · {data.edges.length} relationships</p>
      </header>

      {selectedNode && (
        <aside className={styles.panel} aria-label="Selected node details">
          <h2 className={styles.panelTitle}>{selectedNode.label}</h2>
          <p className={styles.panelMeta}>{selectedNode.type} · {selectedNode.language ?? "Unknown language"}</p>
          <p className={styles.panelDescription}>{selectedNode.description || "No description available."}</p>
          <p className={styles.panelMeta}>★ {selectedNode.stars.toLocaleString()} · score {selectedNode.score.toFixed(1)}</p>
          <button className={styles.close} type="button" onClick={() => setSelectedNode(null)}>Close</button>
        </aside>
      )}
    </div>
  );
}
