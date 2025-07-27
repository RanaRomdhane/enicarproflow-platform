# ENICARPROFLOW - PFA & Internship Management Platform  

![Java](https://img.shields.io/badge/Backend-Spring_Boot-6DB33F?logo=spring)  
![Next.js](https://img.shields.io/badge/Frontend-Next.js-000000?logo=next.js)  
![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1?logo=mysql)  
![Tomcat](https://img.shields.io/badge/Server-Tomcat_10.1-F8DC75?logo=apache-tomcat)  

## 🚀 Project Overview  
**ENICARPROFLOW** is a centralized web platform designed to streamline the management of **Final Year Projects (PFA)** and **Summer Internships** for ENICarthage University. It replaces manual processes with an automated system for:  
- **Students**: Topic selection, progress tracking, report submission  
- **Teachers**: Project supervision, topic validation, student mentoring  
- **Department Heads**: Oversight, reporting, and conflict resolution  

---

## ✨ Key Features  

### 👨‍🎓 For Students  
✅ **Smart Topic Selection** – Rank preferred projects (5-10 choices) or propose custom topics  
📊 **Progress Tracking** – Submit milestone reports with real-time feedback  
📝 **Digital Submissions** – Upload posters (1st year) or detailed reports (2nd year)  
💬 **Integrated Messaging** – Direct communication with supervisors  

### 👨‍🏫 For Teachers  
🛠️ **Topic Proposals** – Submit and manage project ideas  
📈 **Dashboard Analytics** – Monitor all supervised projects in one place  
✏️ **Evaluation Tools** – Validate reports and provide graded feedback  

### 👨‍💼 For Department Heads  
⚡ **Automated Assignments** – Match students to projects based on merit & preferences  
🚨 **Issue Detection** – Flag delayed/blocked projects instantly  
📑 **Centralized Reporting** – Generate institutional insights (completion rates, grades, etc.)  

---

## 🛠 Tech Stack  

| Layer          | Technology                          | Purpose                                  |  
|----------------|-------------------------------------|------------------------------------------|  
| **Frontend**   | Next.js (React)                     | Dynamic UI with server-side rendering    |  
| **Backend**    | Spring Boot (Java)                  | Robust API development & business logic  |  
| **Database**   | MySQL                               | Relational data storage                  |  
| **Auth**       | JWT + Spring Security               | Role-based access control                |  
| **Deployment** | Tomcat 10.1                         | Scalable application hosting             |  

---

## 🚀 Getting Started  

### Prerequisites  
- JDK 17+  
- Node.js 18+  
- MySQL 8.0+  

### Installation  
1. **Clone the repository**:  
   ```bash  
   git clone https://github.com/RanaRomdhane/enicarproflow-platform.git 
   cd enicarproflow-platform

2. **Backend Setup**:

  ```bash 
    cd backend  
    ./mvnw spring-boot:run  
    

3. **Frontend Setup**:

  ```bash 
cd frontend  
npm install  
npm run dev  


4. **Access the app at** : http://localhost:3000