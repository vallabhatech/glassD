export type GraphNode = {
  id: string;
  label: string;
  type: string;
  stars: number;
  language: string | null;
  description: string | null;
  score: number;
  hierarchyLevel: string;
  avatarUrl: string | null;
  x: number;
  y: number;
  z: number;
};

export type GraphEdge = { source: string; target: string; relationship: string };

export type EcosystemGraph = {
  ecosystem: string;
  nodes: GraphNode[];
  edges: GraphEdge[];
  clusters: { name: string; nodeCount: number }[];
};
