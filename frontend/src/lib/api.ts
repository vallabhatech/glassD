import axios from "axios";

const baseURL = (process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080").replace(/\/$/, "");

export const api = axios.create({
  baseURL,
  timeout: 15_000,
  headers: { Accept: "application/json" },
});
