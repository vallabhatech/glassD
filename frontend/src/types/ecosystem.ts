export type EcosystemGraph = {
    ecosystem: string;

    nodes: {
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
    }[];

    edges: {
        source: string;
        target: string;
        relationship: string;
    }[];

    clusters: {
        name: string;
        nodeCount: number;
    }[];
};