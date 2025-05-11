# 🪴 Plant Care Management App
A full-stack web application designed to help botanical gardens and plant lovers manage their plant collections, care history, and daily tasks with ease and clarity.

---

## 🌟 Features
### 1. 👥 User & Organization Management
- Register organizations and administrators
- Add and remove users, assign admin roles
- Secure login and session handling using JWT authentication

### 2. 🌿 Plant Database
- Add plants with details: species, category, location, care requirements
- Upload and view plant photos
- Access full plant care history

### 3. ✅ Task Management
- Create care-related tasks (e.g., watering, fertilizing, repotting)
- Assign tasks to specific users
- Mark tasks as completed
- Receive notifications for new or upcoming tasks

### 4. 📅 Calendar View
- View tasks in an interactive calendar
- Filter tasks by date or completion status

### 5. 📝 Care History
- Log plant care actions with notes and optional images
- View detailed history per plant
- Track care progress over time

---

## 🛠️ Tech Stack
### Frontend:
- HTML, CSS, JavaScript
- Vanilla JS + modular file structure
- Hosted with Apache (XAMPP/WAMP)

### Backend:
- Java 17
- Spring Boot + Spring Security + JWT
- PostgreSQL (via Supabase or local instance)
- JPA (Hibernate), Maven
- REST API

---

## ⚙️ Installation
### 🖥️ Frontend (Apache)
#### 1. Requirements
- Apache server installed (e.g. XAMPP or WAMP)
- Access to Apache config files
#### 2. Placing the files
- Copy the frontend folder into the Apache root directory:
  - XAMPP: `C:\xampp\htdocs\plants`
  - WAMP: `C:\wamp64\www\plants`
- Ensure the main file `index.html` is in the `plants` folder
#### 3. Apache configuration
- Open the Apache config file (`httpd.conf` or `httpd-vhosts.conf`):
  - XAMPP: `C:\xampp\apache\conf\httpd.conf`
  - WAMP: `C:\wamp64\bin\apache\apacheX.X.X\conf\httpd.conf`
#### 4. Restart Apache
- XAMPP: use the XAMPP Control Panel → click **Stop** and then **Start** for Apache
- WAMP: right-click the WAMP icon → choose **Restart All Services**
#### 5. Open in browser
- Visit: [http://localhost/plants](http://localhost/plants)

### 🔙 Backend (Spring Boot)

#### 1. Clone the project
```bash
git clone https://github.com/sepeca/plants.git
cd backend
```
#### 2. Create the database
- Open pgAdmin
- Create a new database (e.g. `plants`)
- Open the Query Tool and run the SQL setup script
#### 3. Configure application.properties
- Make sure the file src/main/resources/application.properties includes:
```properties
  spring.datasource.url=jdbc:postgresql://localhost:5432/plants
  spring.datasource.username=your_user
  spring.datasource.password=your_password
  spring.jpa.hibernate.ddl-auto=none
  spring.jpa.show-sql=true
  server.port=8081
```
#### 4. Create the uploads directory
   If not present:
```bash
mkdir uploads
```
#### 5. Build the project
```bash
   mvn clean install
```
#### 6. Run the server
```bash
   mvn spring-boot:run
```


---

## 🔒 Roles & Permissions
- Admin: full access — manage users, plants, tasks, care records
- Worker: limited access — view/add care records, complete assigned tasks
