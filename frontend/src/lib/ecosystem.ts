import { api } from "@/lib/api";
import type { EcosystemGraph } from "@/types/ecosystem";

export async function fetchEcosystem(name: string): Promise<EcosystemGraph> {
  const normalized = name.trim();
  if (!normalized) throw new Error("Ecosystem name is required.");
  const { data } = await api.get<EcosystemGraph>(`/ecosystem/${encodeURIComponent(normalized)}`);
  return data;
}
