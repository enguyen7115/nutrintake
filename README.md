# Nutrintake
Nutrintake: The Nutrient Tracking App
Nutrintake is a tracking app that allows users to track the foods they eat and their nutritional value; along with setting goals and limits for themselves
in regards to the nutrients they consume.


## Frontend Implementation
The frontend is built using Thymeleaf within the Spring Boot application.

### Implemented
- Dashboard page displaying daily totals (calories, protein, sugar)
- Food creation form
- Food logging form (servings + date)
- totals calculation displayed in the UI
- Table showing logged food entries
- Navigation header
- CSS styling for layout and usability
- Form validation for required inputs

### Planned Frontend Enhancements
- Weekly totals display
- Nutrient goal
- limit notifications
- login page
- Chart visualizations

## Running the Application (Frontend Development)

### Requirements
- Java 17 (or compatible JDK)
- IntelliJ IDEA (recommended) or any IDE with Maven support

### Steps to Run

1. Clone the repository: https://github.com/PinataLad/nutrintake.git
   
3. Open the project in IntelliJ.

4. Locate the main Spring Boot application file:

5. Run the application using IntelliJ's Run button.

6. Open your browser and navigate to: http://localhost:8080/dashboard

## Application Pages Overview

### Dashboard (`/dashboard`)
The dashboard page allows users to:
- Create new food items
- Log servings of food for a selected date
- View calculated daily totals (calories, protein, sugar)
- See a table of foods logged for the day

### Foods (`/foods`)
The foods page allows users to:
- Create and define new food items
- View all saved food items
- Edit or delete food items (planned feature)
- Manage nutritional values per serving

### Goals (`/goals`) (Planned)
The goals page will allow users to:
- Set daily nutrient limits
- Set weekly nutrient limits
- View whether totals exceed limits


## Backend Implementation
The backend of Nutrintake uses SQLite, and is responsible for managing data storage, business logic, and communication with the frontend.

## Implemented
- User registration, including password hashing and login authentication
- Add and store food items in database
- Logging of daily food servings
- Calculation of nutrient totals
- Goal information storage (note: currently no frontend connectivity)

## Planned Backend Features
- Weekly log retrieval and storage
- Goal persistance
- User-based data
- Food and log deletion and editing

## Running the Backend
Backend is automatically ran when the application is ran. When the application starts:
1. An SQLite database is created if it does not already exist
2. Tables are initialized
3. Controllers handle requests
4. Data is stroed and retrieved from nutrition.db
The database file is stored in the project root and can be viewed with the IntelliJ database viewer.

## SQLite Information
Note: SQLite JAR file required for database implementation and application start.
1. Download sqlite-jdbc-3.51.3.0.jar from https://github.com/xerial/sqlite-jdbc/releases/tag/3.51.3.0
2. In IntelliJ, go to File > Project Structure > Modules
3. From there, click the + button over the "Export" column
4. Click "1 JARs or Directories"
5. Select the sqlite-jdbc-3.51.3.0.jar
6. Click "Apply" and "OK"
