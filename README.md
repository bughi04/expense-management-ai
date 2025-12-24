# Expense Management System with AI Currency Prediction

[![Java](https://img.shields.io/badge/Java-22-orange.svg)](https://openjdk.java.net/)
[![JavaFX](https://img.shields.io/badge/JavaFX-22.0.1-blue.svg)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-3.13.0-red.svg)](https://maven.apache.org/)
[![SQLite](https://img.shields.io/badge/SQLite-3.47.2.0-green.svg)](https://www.sqlite.org/)

A full-stack JavaFX application for expense tracking featuring **machine learning-powered currency predictions**. Built to demonstrate proficiency in Java development, GUI design, database management, and implementing ML algorithms from scratch.
> **Note**: This project was developed with AI assistance as a learning and development tool, alongside traditional programming methods.

## Key Features

### Core Functionality
- **Multi-Currency Support** - Track expenses in USD, EUR, GBP, JPY, AUD, and RON with automatic conversion
- **Category Management** - Organize expenses with custom categories
- **Date Tracking** - Separate transaction and expense date recording
- **Financial Reports** - Category breakdowns with spending percentages

### AI/ML Component
- **Currency Prediction System** - 7-day exchange rate forecasts using linear regression
- **Custom ML Implementation** - Built from scratch without external ML libraries
- **Historical Data Simulation** - Generates realistic 30-day market patterns
- **Trading Recommendations** - Actionable insights based on predicted trends

## Technical Stack

- **Language**: Java 22
- **UI Framework**: JavaFX 22.0.1
- **Database**: SQLite 3.47.2.0
- **Build Tool**: Maven 3.13.0
- **Architecture**: MVC pattern with modular design
- **APIs**: Integration with exchange rate APIs

## Quick Start

**Prerequisites**: Java 22+, Maven 3.8+

```bash
# Clone the repository
git clone https://github.com/bughi04/expense-management-ai.git
cd expense-management-ai

# Run the application
mvn clean javafx:run
```

The application will automatically create the local SQLite database on first launch.

## Machine Learning Implementation

The prediction system demonstrates:
- **Linear Regression Algorithm** - Implemented from scratch
- **Statistical Analysis** - Mean calculation and variance handling
- **Time Series Forecasting** - Trend analysis on historical data
- **Asynchronous Processing** - Non-blocking UI updates using JavaFX Tasks

## Skills Demonstrated

- Object-oriented programming in Java
- JavaFX desktop application development
- SQL database design and management
- RESTful API integration
- Machine learning algorithm implementation
- Concurrent programming with JavaFX Tasks
- Maven project management
- MVC architectural pattern

## Future Enhancements

- Export reports to PDF/CSV
- Data visualization with charts
- Budget alerts and notifications
- Cloud database synchronization
- Mobile companion app
