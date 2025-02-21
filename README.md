# Payments Project

## Overview
This project is a full-stack application for processing payments. It consists of:
- **Backend**: Spring Boot (Java 11) with MySQL database
- **Frontend**: React (Vite + TypeScript)
- **Database**: MySQL for storing payment records
- **API Documentation**: OpenAPI with Swagger UI

`If you wish to see some of the 'thought process' that went into some decision making, please views the "Thoughts.md"`
---

## **Project Structure**
```
payments-project/
│── backend/   # Spring Boot Backend
│── frontend/  # React Frontend
```

---

## **2️⃣ MySQL Database Setup**

### **Step 1: Start MySQL & Create Database**
```sql
CREATE DATABASE payments;
CREATE USER 'payments_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON payments.* TO 'payments_user'@'localhost';
FLUSH PRIVILEGES;
```

### **Step 2: Configure Environment Variables**
In `backend/.env`, set the database credentials:
```ini
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_DB=payments
MYSQL_USER=payments_user
MYSQL_PASSWORD=your_password
```

---

## **3️⃣ Backend Setup (Spring Boot)**

### **Step 1: Navigate to Backend Directory**
```sh
cd backend
```

### **Step 2: Install Dependencies & Build**
```sh
mvn clean install
```

### **Step 3: Run the Backend**
```sh
mvn spring-boot:run
```

### **Step 4: Verify API is Running**
Visit `http://localhost:8080/swagger-ui.html` to access Swagger API documentation.

---

## **4️⃣ Frontend Setup (React + Vite + TypeScript)**

### **Step 1: Navigate to Frontend Directory**
```sh
cd frontend
```

### **Step 2: Install Dependencies**
```sh
npm install
```

### **Step 3: Run the Frontend**
```sh
npm run dev
```

### **Step 4: Access the Application**
Open `http://localhost:5173` in your browser.

---

## **5️⃣ Testing the Application**

### **Option 1: Using Postman**
- Open Postman and create a **POST request** to:
```
http://localhost:8080/api/payments
```
- Sample Request Body:
```json
{
    "firstName": "John",
    "lastName": "Doe",
    "zipCode": "12345",
    "cardNumber": "4111111111111111"
}
```
- Expected Response:
```json
{
    "success": true,
    "data": {
        "firstName": "John",
        "lastName": "Doe",
        "zipCode": "12345",
        "hashedCardNum": "abc123hashedvalue"
    }
}
```

### **Option 2: Using the Frontend**
- Open `http://localhost:5173`
- Fill out the payment form and submit
- You should see a success or error message based on validation

---

## **6️⃣ Running Backend Tests**
```sh
cd backend
mvn test
```

---

## **7️⃣ API Documentation**
- Open **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- View the **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

---

## **8️⃣ Troubleshooting**
### **Backend Fails to Start?**
- Ensure MySQL is running (`mysql -u root -p`)
- Verify database exists (`SHOW DATABASES;`)
- Check `.env` credentials match MySQL

### **Frontend Can’t Connect to Backend?**
- Ensure backend is running on `8080`
- Add CORS in `PaymentApi.java` if needed:
```java
@CrossOrigin(origins = "http://localhost:5173")
```

### **Port Already in Use?**
- Change backend port in `application.properties`:
```properties
server.port=9090
```
- Update frontend API URL accordingly.

---

## **9️⃣ Contributions & Future Improvements**
### **Possible Enhancements:**
- **Add more logging** for better tracking of requests and errors.
- **Implement advanced search queries** to retrieve payments based on card details and names.
- **Enhance frontend with UI improvements** styling, overall design etc. Ignored mostly because of the requirements
- Look at encryption specifically rather than hashing, which I felt was a simpler solution in this scenario

### **Want to Contribute?**
Fork this repo, make your changes, and submit a **Pull Request**! 🚀
