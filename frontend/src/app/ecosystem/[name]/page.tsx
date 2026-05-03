import GraphCanvas from "@/components/graph/GraphCanvas";
import { fetchEcosystem } from "@/lib/ecosystem";

export default async function Page({
    params,
}: {
    params: { name: string };
}) {
    const data = await fetchEcosystem(params.name);

    return (
        <div style={{ height: "100vh", background: "#0a0a0a" }}>
            <GraphCanvas nodes={data.nodes} edges={data.edges} />
        </div>
    );
}