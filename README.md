# Student Attendance System with Facial Recognition


A comprehensive microservices-based student attendance management system with facial recognition, built using Spring Boot, React, and OpenCV.

## 🚀 Features

- **Facial Recognition Attendance**: Contactless attendance verification with face enrollment and matching
- **Role-Based Access Control**: Different interfaces for administrators and teachers
- **Real-Time Attendance Tracking**: View and manage attendance records instantly
- **School & Student Management**: Comprehensive management of educational institutions
- **Attendance Analytics**: Reports and insights on attendance patterns
- **Distributed System Architecture**: Scalable microservices design

## 🏗️ Architecture

The system consists of 7 microservices:

1. **Config Server**: Centralized configuration management
2. **Discovery Service**: Service registry and discovery with Eureka
3. **API Gateway**: Request routing and load balancing
4. **Students Service**: Student data management
5. **Schools Service**: School data management
6. **Attendance Service**: Attendance records and sessions
7. **Facial Recognition Service**: Face enrollment and verification

## 🛠️ Technologies

### Backend
- **Spring Boot & Spring Cloud**: Microservices framework
- **Spring Data JPA**: Database access
- **Spring Cloud Netflix**: Service discovery
- **OpenFeign**: Declarative REST client for service communication
- **Zipkin**: Distributed tracing and performance monitoring
- **PostgreSQL**: Relational database
- **OpenCV**: Computer vision for facial recognition
- **Docker**: Containerization

### Frontend
- **React**: Frontend framework
- **Material UI**: Component library
- **React Router**: Navigation
- **Axios**: API communication
- **React Webcam**: Camera access
- **Context API**: State management

## 📋 Prerequisites

- Java 17
- Node.js (v16+)
- Docker & Docker Compose
- PostgreSQL (or use the provided Docker configuration)
- Maven

## 🔧 Setup & Installation

### Clone the Repository

```bash
git clone https://github.com/yourusername/attendance-system.git
cd attendance-system
```

### Start Infrastructure Services

```bash
docker-compose up -d postgresql pgadmin zipkin
```

### Start Backend Services (in order)

1. **Config Server**:
```bash
cd config-server
mvn spring-boot:run
```

2. **Discovery Service** (after Config Server is up):
```bash
cd discovery
mvn spring-boot:run
```

3. **Gateway**:
```bash
cd gateway
mvn spring-boot:run
```

4. **Other Services** (after Gateway is up):
```bash
cd schools
mvn spring-boot:run

cd students
mvn spring-boot:run

cd attendance
mvn spring-boot:run

cd facial-recognition
mvn spring-boot:run
```

### Start Frontend

```bash
cd attendance-system-frontend
npm install
npm start
```

The application will be available at: http://localhost:3000

## 📱 Usage

### Login Credentials (Demo)

- **Admin:**
  - Username: admin
  - Password: password

- **Teacher:**
  - Username: teacher
  - Password: password

### Main Features

1. **School Management** (Admin only)
   - Create/Edit/Delete schools
   - View students by school

2. **Student Management**
   - Register/Edit/Delete students
   - Assign students to schools

3. **Face Enrollment**
   - Capture student faces
   - Train the facial recognition system

4. **Attendance Sessions**
   - Create attendance sessions
   - Specify class, subject, and time

5. **Take Attendance**
   - Mark attendance using facial recognition
   - Manual attendance marking option
   - View real-time verification results

6. **Reports**
   - View attendance records by session
   - Analyze attendance statistics

## 🐳 Docker Deployment

To deploy the entire stack using Docker:

```bash
# Build all services
./mvnw clean package -DskipTests

# Start everything with Docker Compose
docker-compose up -d
```

## 📦 Project Structure

```
attendance-system/
├── config-server/             # Centralized configuration
├── discovery/                 # Service discovery (Eureka)
├── gateway/                   # API Gateway
├── schools/                   # Schools service
├── students/                  # Students service
├── attendance/                # Attendance service
├── facial-recognition/        # Facial recognition service
├── attendance-system-frontend/# React frontend
└── docker-compose.yml         # Docker composition
```

## 📋 API Documentation

### Students API
- `GET /api/v1/students` - Get all students
- `GET /api/v1/students/{id}` - Get student by ID
- `GET /api/v1/students/school/{schoolId}` - Get students by school
- `POST /api/v1/students` - Create a new student
- `PUT /api/v1/students/{id}` - Update a student
- `DELETE /api/v1/students/{id}` - Delete a student

### Schools API
- `GET /api/v1/schools` - Get all schools
- `GET /api/v1/schools/{id}` - Get school by ID
- `GET /api/v1/schools/with-students/{id}` - Get school with its students
- `POST /api/v1/schools` - Create a new school
- `PUT /api/v1/schools/{id}` - Update a school
- `DELETE /api/v1/schools/{id}` - Delete a school

### Attendance API
- `GET /api/v1/attendance/sessions` - Get all sessions
- `GET /api/v1/attendance/sessions/{id}` - Get session by ID
- `GET /api/v1/attendance/sessions/active/school/{schoolId}` - Get active sessions by school
- `POST /api/v1/attendance/sessions` - Create a new session
- `PUT /api/v1/attendance/sessions/{id}/close` - Close a session
- `POST /api/v1/attendance` - Mark attendance manually
- `POST /api/v1/attendance/facial-recognition` - Mark attendance using facial recognition

### Facial Recognition API
- `POST /api/v1/facial-recognition/enroll` - Enroll a student's face
- `POST /api/v1/facial-recognition/verify` - Verify a face against enrolled data

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature-name`
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgements

- Spring Boot and Spring Cloud for the microservices framework
- OpenCV for facial recognition capabilities
- React and Material UI for the frontend components
- The open-source community for their valuable tools and libraries
