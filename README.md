# Singapore TOTO Analyzer Application

The **Singapore TOTO Analyzer Application** is a web-based application that allows users to analyze and interact with data related to Singapore's TOTO lottery. This project consists of a **Python server** for data scraping and a **Spring Boot application** as the main web interface. 

The data is retrieved via API calls, and several features provide a comprehensive TOTO experience, including historical data analysis, number suggestions, location mapping, and earnings tracking.

---

## Features

### **Home Page**
- Displays the latest TOTO draw, refreshed daily by an automated service.
- Shows a countdown timer to the next TOTO draw.
- Includes a TOTO checker where users can input past numbers to check for winnings.

### **History Page**
- Lists all previous TOTO draws and their prize distributions.
- Covers data from **2008 to the present** (due to data unavailability beyond 2008).

### **Stats Page**
- Analyzes over **1900+ entries** of TOTO data.
- Provides:
  - **Pair Frequency Analysis**: Insights into which pairs of numbers appear most frequently.
  - **TOTO Number Suggestions**: Suggested numbers based on past pair frequency trends.

### **Location Page**
- Displays Singapore Pools outlet locations.
- Data scraped from the Singapore Pools website.
- GEO-coded using the **Google Geocode API** and displayed on a map using the **Google Maps API**.

### **User Page**
- Features a **ledger** to log past TOTO earnings and winnings.
- Allows users to create an account and log in or sign up (Security and Encryption Features Lacking).

---

## Architecture

### Backend
- **Python Server**: 
  - Scrapes the Singapore Pools website for the latest TOTO results and outlet data.
  - Hosted on **Fly.io** for free hosting and fast spin-up times during idle periods.
- **Spring Boot Application**:
  - Serves as the main application for rendering web pages and handling user interactions.
  - Communicates with the Python server via RESTful API calls.
  - Hosted on **Fly.io** for the same reasons as the Python server.

### Database & Caching
- **Redis Server**:
  - Used as a database due to the module limitations.
  - Hosted on **Railway**, leveraging their reliable Redis hosting solution.
- **Google Sheets**:
  - Due to module limiations, most of the data stored from the scrap will be stored in a google sheet
---

## How It Works

1. **Data Scraping**: 
   - The Python server scrapes the Singapore Pools website for TOTO results and outlet locations.
   - Data is processed and stored for easy access by python server.

2. **API Communication**:
   - The Spring Boot application retrieves scraped data from the Python server via RESTful API endpoints.
   - APIs are designed for efficiency to ensure fast data delivery to the Spring application.

3. **Front-End Features**:
   - Uses HTML, CSS, and **ALOT** of JavaScript for a responsive user experience.
   - Integrates the **Google Maps API** for location visualization.

4. **User Management**:
   - Basic authentication and authorization for account creation and login.
   - User-specific ledger to track TOTO earnings and winnings.

---

## Hosting and Deployment

- **Python Server**: Hosted on **Fly.io**.
- **Spring Boot Application**: Hosted on **Fly.io**.
- **Redis Server**: Hosted on **Railway** for fast and reliable caching.

---
