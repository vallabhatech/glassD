"use client";

import Link from "next/link";

export default function HomePage() {
  return (
    <div style={{ color: "white", padding: 20 }}>
      <h1>OpenSource Galaxy</h1>

      <p>Test ecosystem route:</p>

      <Link href="/ecosystem/react" style={{ color: "cyan" }}>
        Go to React Ecosystem
      </Link>
    </div>
  );
}