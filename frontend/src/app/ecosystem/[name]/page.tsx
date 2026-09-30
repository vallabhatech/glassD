import GraphCanvas from "@/components/graph/GraphCanvas";
import { fetchEcosystem } from "@/lib/ecosystem";

export default async function EcosystemPage({ params }: { params: Promise<{ name: string }> }) {
  const { name } = await params;
  const ecosystem = decodeURIComponent(name);
  const data = await fetchEcosystem(ecosystem);
  return <main className="graph-page"><GraphCanvas data={data} /></main>;
}
