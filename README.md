# 🚀 AI Event Concierge

AI Event Concierge is a full-stack AI-powered web application that generates event plans (venue, location, estimated cost, and reasoning) from natural language prompts using Google Gemini API.

---

## 🌐 Live Demo

👉 https://ai-event-concierge-six.vercel.app/

---

## 🛠️ Tech Stack

- **Frontend:** React.js, Axios  
- **Backend:** Spring Boot (Java)  
- **Database:** MongoDB Atlas  
- **AI API:** Google Gemini API  
- **Deployment:** Vercel (Frontend), Railway (Backend)

---

## ⚙️ How to Run Locally

Follow these steps to run the project on your local machine.

---

### 🔹 Prerequisites

Make sure you have installed:

- Java 17+  
- Node.js (v18+ recommended)  
- Maven (or use Maven Wrapper)  
- MongoDB Atlas account  
- Gemini API Key  

---

### 🔥 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/ai-event-concierge.git
cd ai-event-concierge

### 2. Set Environment Variables
For mac/linux
export MONGO_URI="your_mongodb_uri"
export GEMINI_API_KEY="your_gemini_api_key"

For windows
set MONGO_URI=your_mongodb_uri
set GEMINI_API_KEY=your_gemini_api_key

### 3. Run Backend (Spring Boot)
./mvnw spring-boot:run
Backend will run on:
http://localhost:8080

### 4. Run Frontend (React)
Open terminal and write 
cd frontend
npm install
npm start
Frontend will run on: http://localhost:3000

### 5. Test the Application
	1.	Open the frontend in browser
	2.	Enter an event idea
	3.	Click Generate
	4.	View AI-generated event suggestions
	5.	Refresh page → history persists


🔗 API Endpoints
	•	Generate Event: POST /api/generate-event
	•	Get History: GET /api/history
