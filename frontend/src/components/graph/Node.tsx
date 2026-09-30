"use client";

import { useMemo, useState } from "react";
import { useCursor } from "@react-three/drei";
import type { GraphNode } from "@/types/ecosystem";

type Props = { node: GraphNode; selected: boolean; onSelect: (node: GraphNode) => void };

export default function NodeMesh({ node, selected, onSelect }: Props) {
  const [hovered, setHovered] = useState(false);
  useCursor(hovered);
  const radius = useMemo(() => Math.min(7, 2.8 + Math.log10(Math.max(0, node.stars) + 1) * 0.8), [node.stars]);

  return (
    <mesh position={[node.x, node.y, node.z]} onClick={(event) => { event.stopPropagation(); onSelect(node); }}
      onPointerOver={() => setHovered(true)} onPointerOut={() => setHovered(false)}
      scale={selected ? 1.2 : hovered ? 1.08 : 1}>
      <sphereGeometry args={[radius, 24, 24]} />
      <meshStandardMaterial color={selected ? "#b2a9ff" : node.type === "contributor" ? "#4fd1c5" : "#7567e8"}
        emissive={selected ? "#b2a9ff" : "#7567e8"} emissiveIntensity={selected ? 2 : hovered ? 1.4 : 0.8}
        roughness={0.35} metalness={0.2} />
    </mesh>
  );
}
