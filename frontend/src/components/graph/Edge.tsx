"use client";

import { Line } from "@react-three/drei";
import type { GraphNode } from "@/types/ecosystem";

type Props = { from: GraphNode; to: GraphNode; relationship: string };

export default function EdgeLine({ from, to, relationship }: Props) {
  return <Line points={[[from.x, from.y, from.z], [to.x, to.y, to.z]]}
    color={relationship === "contributes-to" ? "#4fd1c5" : "#77758d"}
    lineWidth={relationship === "contributes-to" ? 1.2 : 0.8} transparent opacity={0.42} />;
}
