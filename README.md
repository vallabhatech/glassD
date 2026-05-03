# OpenSource Galaxy

**Visualizing the Open Source Universe Through Interactive 3D Ecosystem Graphs**

OpenSource Galaxy is a full-stack application that transforms open-source ecosystems into interactive 3D graphs. Instead of exploring repositories through lists and dependency trees, users can navigate a visual galaxy of connected projects and technologies.

---

## Features

* Interactive 3D ecosystem visualization
* GitHub ecosystem discovery
* Dynamic graph generation
* Repository relationship exploration
* Node selection and inspection
* Production-ready frontend and backend architecture

### Current Interaction

* Enter an ecosystem name (e.g. `react`)
* Explore the generated 3D graph
* Click on any sphere/node
* Repository information appears in the **top-right information panel**
* Rotate, zoom, and navigate the ecosystem using mouse controls

---

## Tech Stack

### Frontend

* Next.js
* TypeScript
* React
* Three.js
* React Three Fiber
* Axios

### Backend

* Java 21
* Spring Boot
* Maven

### APIs

* GitHub REST API

---

## Running Locally

> Since hosted backend instances may sleep or become unavailable on free plans, running locally is the recommended approach.

### Prerequisites

Install:

* Java 21+
* Maven
* Node.js 20+
* npm

---

## Clone Repository

```bash
git clone https://github.com/your-username/OpenSource-Galaxy.git

cd OpenSource-Galaxy
```

---

## Backend Setup

Navigate to backend:

```bash
cd backend
```

Create:

```text
src/main/resources/application-local.properties
```

Add:

```properties
github.token=YOUR_GITHUB_TOKEN
spring.profiles.active=local
```

Run backend:

```bash
mvn spring-boot:run
```

Backend will start at:

```text
http://localhost:8080
```

Verify:

```text
http://localhost:8080/ecosystem/react
```

You should receive JSON data.

---

## Frontend Setup

Open another terminal:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Create:

```text
.env.local
```

Add:

```env
NEXT_PUBLIC_API_BASE=http://localhost:8080
```

Run frontend:

```bash
npm run dev
```

Frontend will start at:

```text
http://localhost:3000
```

---

## Usage

1. Open:

```text
http://localhost:3000
```

2. Enter an ecosystem name such as:

```text
react
```

```text
spring
```

```text
nextjs
```

3. Explore the generated graph.

4. Click on a sphere to view repository details in the **top-right information panel**.

---

## Project Structure

```text
OpenSource-Galaxy
│
├── backend
│  
├── frontend
│
└── README.md
```

---

## Future Roadmap

* Density controls
* Advanced graph clustering
* Ecosystem depth selection
* Repository statistics
* Contributor insights
* Improved lighting and bloom effects
* Metallic node materials
* Galaxy-scale visual layouts
* Search recommendations
* Multi-ecosystem comparison

---

## License

MIT License

---

Built with ❤️ for developers who think dependency trees deserve better than spreadsheets.
