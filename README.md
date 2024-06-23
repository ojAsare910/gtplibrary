Library Management System Documentation
1. Introduction
This project provides functionalities for adding, updating, and deleting books, managing users, handling transactions, fines, reservations, and more. It includes a user-friendly graphical interface built using JavaFX for the frontend and MySQL for the backend database.
1. Features
Books Management:
Add new books with details like title, author, category, etc.
Update existing book information.
Delete books from the library catalog.
Users Management:
Add new users (patrons and staff) with their details.
Update user information including username, email, and user type.
Transactions:
Record borrowing and returning of books.
Track transaction history.
Fines Management:
Record fines for overdue books.
Mark fines as paid or unpaid.
Reservations:
Allow patrons to reserve books.
User Interface:
Tab-based navigation for easy access to different functionalities.
Data tables and forms for intuitive data entry and management.
2. Technologies Used
JavaFX: Java library for building rich client applications.
FXML: XML-based markup language for defining user interfaces in JavaFX.
MySQL: Relational database management system used for data storage.
Java Database Connectivity (JDBC): API for connecting Java applications to relational databases.
Maven: Dependency management tool for Java projects.
3. User Interface
The user interface consists of a main window with tabs for different functionalities:
Books: Manage books in the library.
Users: Manage library patrons and staff.
Transactions: View transaction history.
Fines: Manage fines for overdue books.
Reservations: Manage book reservations.
4. Functionality Overview
Add/Edit/Delete: Add new books and users, edit existing information, and delete records when necessary.
Data Validation: Validate user input to ensure data integrity.
Error Handling: Provide informative error messages for users when operations fail.
Responsive Design: Ensure the application adapts to different screen sizes for usability.
5. Code Structure
The project follows a structured approach with separation of concerns:
Controller Classes: Handle user interactions and business logic.
FXML Views: Define the graphical user interface using FXML markup.
DAO Classes: Implement data access logic to interact with the MySQL database.
Model Classes: Represent entities such as User, Book, Transaction, etc.
Utilities: Include helper classes for database connection
