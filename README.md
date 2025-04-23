# BTO Management System

This is a CLI HDB BTO Management System built for the SC2002 Object-Oriented Design & Programming course. The application simulates key functionalities of an HDB system for applicants, HDB officers, and managers.

---

## Features

#### *👤 Applicant Functionalities**

- View eligible BTO projects
- Apply for a BTO project
- View submitted application
- Request application withdrawal
- Submit an enquiry
- View, edit, or delete submitted enquiries
- Update account password
- Logout of the system

#### **🛡️ HDB Officer Functionalities**

*(inherits all Applicant features + officer-specific capabilities)*

- View eligible projects (as Applicant)
- Apply for projects not being handled
- Register to handle a BTO project
- View current registration status
- View details of the project assigned (even if visibility is off)
- View and respond to enquiries related to assigned project only
- Book flats for applicants with successful applications
- Generate booking receipts for applicants
- Update account password
- Logout

#### **🏗️ HDB Manager Functionalities**

- Create new BTO projects
- Edit project details
- Delete BTO projects
- Toggle visibility of projects
- View all projects in the system
- View only projects managed by the logged-in manager
- View officer registration requests
- Approve or reject officer registrations
- View applications submitted for managed projects
- Approve or reject BTO applications
- Process application withdrawal requests
- View all enquiries in the system
- Reply to enquiries related to managed projects
- Generate reports with various filters (e.g., marital status, flat type)
- Change account password
- Logout

---
##  Project Structure

```bash
├── src/                   # Source code
├── test/                  # JUnit test files grouped by functionality
├── out/                   # Compiled .class files
├── lib/                   # External libraries (JUnit 5)
├── bash_scripts/          # Shell scripts to compile, test, and generate docs
├── html/                  # Javadoc output
├── data/                  # Application CSV data for runtime
└── report.pdf             # Detailed project report
└── README.md              # Project readme file
```
---

## Compilation Instructions

1. Open a terminal and navigate to the **root directory** of the project.
2. Run the following command to compile all `.java` files and store the compiled `.class` files in the `out/` directory:

```bash
clear
javac -d out $(find src -name "*.java")
```

📦 All compiled files will be placed inside the out/ directory.

## Running the Program

Once compilation is successful:

1. Run the main program using the following command

```bash
java -cp out MainApp
```

---

##  **Javadoc**

To generate documentation:

Go the root directory of the project and run the following command

```bash
javadoc -d html -author -private -noqualifier all -version \
-sourcepath src \
boundary control model model.enums repo util src/MainApp.java
```
---

## Testing

- All unit tests are written using **JUnit 5**.

- Test coverage includes:
  
  ### **Login & Authentication (LoginTest.java)**
  
  | **Test Case No.** | **Description**               |
  | ----------------- | ----------------------------- |
  | **1**             | Valid User Login              |
  | **2**             | Invalid NRIC Format           |
  | **3**             | Incorrect Password            |
  | **4**             | Empty Passwor Check           |
  | **5**             | Password Change Functionality |
  
  
  
  ### **Project Visibility & Application (ApplicationTest.java)**
  
  | **Test Case No.** | **Description**                                        |
  | ----------------- | ------------------------------------------------------ |
  | **6**             | Project Visibility Based on User Group and Toggle      |
  | **7**             | Project Application                                    |
  | **8**             | Viewing Application Status after Visibility Toggle Off |
  | **9**             | Single Flat Booking per Successful Application         |
  
  
  
  ### **Enquiries (EnquiryTest.java)**
  
  | **Test Case No.** | **Description**                                    |
  | ----------------- | -------------------------------------------------- |
  | **10**            | Submit Enquiry                                     |
  | **11**            | Edit Enquiry                                       |
  | **12**            | Delete Enquiry                                     |
  | **13**            | Reply to Enquiry                                   |
  | **14**            | Cannot Reply to enquiries for not assigned project |
  
  
  
  ### **Officer Registration & Permissions (OfficerRegistrationTest.java)**
  
  | **Test Case No.** | **Description**                        |
  | ----------------- | -------------------------------------- |
  | **15**            | HDB Officer Registration Eligibility   |
  | **16**            | HDB Officer Registration Status        |
  | **17**            | Project Detail Access for HDB Officer  |
  | **18**            | Restriction on Editing Project Details |
  
  
  
  ### **Booking & Receipts (BookingTest.java)**
  
  | **Test Case No.** | **Description**                       |
  | ----------------- | ------------------------------------- |
  | **19**            | Flat Selection and Booking Management |
  | **20**            | Receipt Generation for Flat Booking   |
  
  
  
  ### **Project Management (ProjectManagementTest.java)**
  
  | **Test Case No.** | **Description**                                  |
  | ----------------- | ------------------------------------------------ |
  | **21**            | Create, Edit, and Delete BTO Project Listings    |
  | **22**            | Single Project Management per Application Period |
  | **23**            | Toggle Project Visibility                        |
  | **24**            | View All and Filtered Project Listings           |
  
  
  
  ### **Manager Controls (ManagerControlTest.java)**
  
  | **Test Case No.** | **Description**              |
  | ----------------- | ---------------------------- |
  | **25**            | Approve Officer Registration |
  | **26**            | Reject Officer Registration  |
  | **27**            | Approve BTO Application      |
  | **28**            | Reject BTO Application       |
  
  
  
  ### **Reporting (ReportTest.java)**
  
  | **Test Case No.** | **Description**        |
  | ----------------- | ---------------------- |
  | **29**            | Filter by married      |
  | **30**            | Filter by not married  |
  | **31**            | Filter by 2 room flats |

### Setup Testing Environment

Navigate to the root diectory of the project

**Make lib directory**

```bash
mkdir -p lib
```

**Go the lib directory**

```
cd lib
```

**Download and Install JUnit 5**

```bash
curl -O https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.13.0-M2/junit-platform-console-standalone-1.13.0-M2.jar
```

### Run Tests

Compile all the test files

```bash
javac -d out -cp "lib/junit-platform-console-standalone-1.13.0-M2.jar:out:src" $(find test -name "*.java") 
```

Run all the tests

```bash
java -jar lib/junit-platform-console-standalone-1.13.0-M2.jar \
execute \
--class-path out \
--scan-class-path
```

## **Dev Scripts**

```bash
- run.sh – Compiles and runs the main app
  
- run_tests.sh – Compiles and executes all unit tests
  
- generate_java_doc.sh – Generates Javadoc output to html/
  
- setup_testing.sh - Sets up the testing environment by downloading JUnit 5 
```

##   **CSV Data**

```
- data/ApplicantList.csv
- data/OfficerList.csv
- data/ManagerList.csv
- data/ProjectList.csv

These contain persistent data that is loaded and saved during runtime.
```

##  **Contributors**

1. [Thangaraju Sibiraj](https://github.com/t-sibiraj)
2. [Dinh Quang Anh](https://github.com/The-Magni)
3. [Tok Xi Quan](https://github.com/UnfairWraith)
4. Zhang Yuerong
