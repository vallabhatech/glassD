import { useState, useEffect } from "react";
import { fetchEcosystem } from "@/lib/ecosystem";
import { EcosystemGraph } from "@/types/ecosystem";

export default function EcosystemClient({ ecosystem }: { ecosystem: string }) {
    const [data, setData] = useState<EcosystemGraph | null>(null);

    useEffect(() => {
        fetchEcosystem(ecosystem).then(setData);
    }, [ecosystem]);

    if (!data) return <div>Loading...</div>;

    return <div>{data.nodes.length}</div>;
}