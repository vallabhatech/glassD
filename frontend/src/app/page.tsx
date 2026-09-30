"use client";

import { FormEvent, useState } from "react";
import { useRouter } from "next/navigation";

export default function HomePage() {
  const router = useRouter();
  const [ecosystem, setEcosystem] = useState("react");

  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const value = ecosystem.trim();
    if (value) router.push(`/ecosystem/${encodeURIComponent(value)}`);
  }

  return (
    <main className="landing">
      <section className="hero" aria-labelledby="hero-title">
        <span className="eyebrow">OPENSOURCE GALAXY</span>
        <h1 id="hero-title">Explore the open-source universe.</h1>
        <p>Turn a GitHub ecosystem into an interactive 3D map of repositories, contributors, and relationships.</p>
        <form className="search" onSubmit={handleSubmit}>
          <label htmlFor="ecosystem">Ecosystem</label>
          <div className="search-row">
            <input id="ecosystem" value={ecosystem} onChange={(event) => setEcosystem(event.target.value)} placeholder="react, spring, nextjs..." autoComplete="off" />
            <button type="submit">Explore</button>
          </div>
        </form>
        <div className="quick-links" aria-label="Popular ecosystems">
          {["react", "spring", "nextjs"].map((name) => (
            <button key={name} type="button" onClick={() => router.push(`/ecosystem/${name}`)}>{name}</button>
          ))}
        </div>
      </section>
    </main>
  );
}
