import axios from "axios";

const baseURL = process.env.NEXT_PUBLIC_API_URL?.replace(/\/$/, "");
if (!baseURL) throw new Error("NEXT_PUBLIC_API_URL is not configured.");

export const api = axios.create({
  baseURL,
  timeout: 15_000,
  headers: { Accept: "application/json" },
});
