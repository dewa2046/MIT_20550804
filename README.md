# MIT_20550804
Automated Teller Machine (ATM) Monitoring Dashboard System

Name 				        - D.C.Pushpakumara
Registration Number - 2020/MIT/080
Index Number 		    - 20550804
Project Title 		  - Automated Teller Machine (ATM) Monitoring Dashboard System
GitHub URL 			    - https://github.com/dewa2046/MIT_20550804.git

# ATM Monitoring Dashboard System

## Overview

The main goal of this project is to design and develop a web based ATM/CDM/CRM monitoring dashboard for Bank of Ceylon to provide real-time monitoring of machine status and performance, with the aim of ensuring that the machines are operational and delivering a good user experience.

This project consists of two main components:
1. ATM Monitoring Dashboard: A web-based application to monitor the status of ATMs in real-time.
2. ATM Simulator: A Java application simulating ATM errors and behaviours for testing the monitoring system.


* The front-end is built using HTML, CSS, and JavaScript, while the back-end comprises Java RESTful Web Services. 
* The web services are consumed by the frontend using AJAX for real-time communication. 
* The back-end services connect to an Oracle database for storing ATM-related and user related data.
* The web services are deployed using Apache Tomcat. 
* The project was developed in NetBeans IDE.

## Features

### ATM Monitoring Dashboard
* Real-time ATM Status Monitoring: Displays current status of each ATM.
* ATM Data Visualization: Graphs and charts to show online/offline ATM status, fault wise ATM lists,cash level, etc.
* Notify the alerts to the branch users about critical issues such as Network failures,Cassette faults, Pin pad faults, Card reader faults.
* Provide ATM downtime reports using historical data in order to identify the problems related to the ATM network.
* AJAX Integration: Ensures real-time data updates without page reloads.
* Java RESTful Web Service: Back-end services for handling requests from the frontend and interacting with the database.
* Oracle Database: Stores ATM data like ATM status logs, user related data, and configurations.

### ATM Simulator
- ATM Error Scenarios: simulate/reproduce ATM errors and behaviours.
- Log Monitoring: Logs the activity of the simulator to verify its integration with the monitoring system.

## Installation and Setup

### Prerequisites
- Java JDK (version 8 or higher)
- NetBeans IDE (or any IDE that supports Java)
- Apache Tomcat (version 9 or higher)
- Oracle Database (setup with the required tables for ATM data)

### Installation

1. Setting Up the Back-end (Java Web Service)

* Open the project in NetBeans IDE.
* Ensure you have Apache Tomcat installed and configured in NetBeans.
* Create the necessary tables in Oracle Database using the provided SQL scripts (Database\SQLScripts).
* Create a folder named webservices in Apache Tomcat root folder and copy the ATMDashBoardBackEnd folder.
* Configure database connection details in DBConnect.java (ATMDashBoardBackEnd\src\java\BackEnd).
* Deploy the web service to Apache Tomcat from NetBeans.

2. Setting Up the Frontend (HTML, CSS, JavaScript)

* Create a folder named webapps in Apache Tomcat root folder and copy the ATMDashBoard folder. 
* The frontend will make AJAX calls to the back-end deployed on Apache Tomcat.

3. Setting Up the ATM Simulator

* Run the ATMSimulator.exe in ATM Simulator EXE folder.
* The simulator will generate errors and behaviours that the monitoring dashboard will display.

4. Running the ATM Monitoring Dashboard

* Access the web application through a browser at http://localhost:8080/ATMDashBoard/Login.html.






