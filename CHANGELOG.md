# Changelog

All notable changes to OpenSource Galaxy are documented here.

## 2026-09-30

### Added
- GitHub Actions CI for frontend linting, typechecking, builds, and backend tests.
- CodeQL analysis for Java and JavaScript/TypeScript.
- Dependabot configuration for npm, Maven, and GitHub Actions.
- Environment examples for local frontend/backend setup.
- Backend health endpoint at `GET /health`.

### Changed
- Modernized the landing page with ecosystem search and quick links.
- Improved the 3D graph with reusable node/edge components, hover states, star-scaled nodes, relationship styling, and a responsive details panel.
- Standardized frontend API configuration around `NEXT_PUBLIC_API_URL`.
- Hardened backend CORS, validation, GitHub API headers, and error responses.
- Removed duplicate graph positioning work in the backend service.
- Corrected the MIT copyright holder to the repository owner.

### History
- 2026-05-03 — Initial repository import and OpenSource Galaxy scaffold.
