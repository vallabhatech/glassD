import axios from "axios";

const API_BASE = process.env.NEXT_PUBLIC_API_URL;

export const fetchEcosystem = async (name: string) => {
    const res = await axios.get(`${API_BASE}/ecosystem/${name}`);
    return res.data;
};