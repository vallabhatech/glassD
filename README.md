# OpenSource Galaxy

Interactive 3D exploration for open-source ecosystems.

OpenSource Galaxy turns a GitHub ecosystem search into a navigable graph of repositories and selected contributors. The frontend is built with Next.js, React Three Fiber, and Three.js; the backend uses Java 21 and Spring Boot to query GitHub and prepare graph data.

## What it does

- Search for an ecosystem such as `react`, `spring`, or `nextjs`.
- Discover highly starred repositories from GitHub.
- Classify repository nodes and calculate graph scores.
- Add a small contributor layer for leading repositories.
- Generate relationship edges and graph positions.
- Explore the result in a 3D canvas with orbit controls.
- Select nodes to inspect repository metadata.

## Architecture

```text
Browser
  │
  ▼
Next.js / React Three Fiber
  │  GET /ecosystem/{name}
  ▼
Spring Boot API
  │
  ├── EcosystemService
  ├── Graph scoring / classification
  └── GitHub client
       │
       ▼
    GitHub REST API
```

### Repository layout

```text
glassD/
├── .github/
│   └── workflows/
├── backend/
│   ├── src/main/java/com/repoverse/backend/
│   │   ├── client/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── service/
│   │   ├── utils/
│   │   └── wire/
│   └── pom.xml
├── frontend/
│   ├── src/app/
│   ├── src/components/graph/
│   ├── src/lib/
│   └── package.json
├── CHANGELOG.md
└── README.md
```

## Requirements

- Node.js 20+
- npm 10+
- Java 21+
- A GitHub token is recommended for higher API limits.

## Run locally

### 1. Clone

```bash
git clone https://github.com/vallabhatech/glassD.git
cd glassD
```

### 2. Start the backend

From `backend/`:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Set `GITHUB_TOKEN` in your environment before starting the backend. The backend also accepts `CORS_ALLOWED_ORIGINS` as a comma-separated list.

Health check:

```text
http://localhost:8080/health
```

### 3. Start the frontend

Create `frontend/.env.local`:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

Then:

```bash
cd frontend
npm ci
npm run dev
```

Open `http://localhost:3000`.

## Quality checks

Frontend:

```bash
cd frontend
npm run lint
npm run typecheck
npm run build
```

Backend:

```bash
cd backend
./mvnw test
```

GitHub Actions runs these checks on pushes and pull requests targeting `main`.

## API

### GET /health

Returns a small service-health payload.

### GET /ecosystem/{name}

Builds a graph for an ecosystem name.

Example:

```text
GET http://localhost:8080/ecosystem/react
```

The response contains:

- `ecosystem`
- `nodes`
- `edges`
- `clusters`

## Configuration

| Variable | Component | Purpose |
|---|---|---|
| `NEXT_PUBLIC_API_URL` | Frontend | Backend base URL |
| `GITHUB_TOKEN` | Backend | GitHub API authentication |
| `CORS_ALLOWED_ORIGINS` | Backend | Allowed browser origins |

Do not commit real tokens or `.env.local` files.

## Security and maintenance

- CodeQL scans Java and JavaScript/TypeScript.
- Dependabot checks npm, Maven, and GitHub Actions dependencies.
- Backend input is validated before ecosystem processing.
- GitHub credentials are read from environment variables.
- API responses use explicit JSON content types and bounded request timeouts.

See `CHANGELOG.md` for the modernization history.

## License

MIT
