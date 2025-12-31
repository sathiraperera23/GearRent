# GearRent Pro – Equipment Rental Management System
## Project Description

GearRent Pro is a JavaFX-based desktop application developed to manage a multi-branch equipment rental business.
The system supports branch-wise inventory management, equipment rentals, reservations, customer handling, pricing rules, deposits, returns, damages, overdue rentals, and reporting.

The application follows a layered architecture:

JavaFX UI (FXML + Controllers)

Service Layer (Business logic)

DAO Layer (Manual JDBC – no JPA)

MySQL Database

Role-based access control is implemented to restrict features based on user roles.

### Database Connectivity
Configure your database in the DBConnection file under the db package.
Enter your mysql username and password and the correct database name. 

### Running the application
Open the MainApp class and run it in an IDE like Intellij.