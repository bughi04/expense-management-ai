# Expense Management System with AI Currency Prediction

[![Java](https://img.shields.io/badge/Java-22-orange.svg)](https://openjdk.java.net/)
[![JavaFX](https://img.shields.io/badge/JavaFX-22.0.1-blue.svg)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-3.13.0-red.svg)](https://maven.apache.org/)
[![SQLite](https://img.shields.io/badge/SQLite-3.47.2.0-green.svg)](https://www.sqlite.org/)

A sophisticated JavaFX expense management application featuring **AI-powered currency prediction** through machine learning algorithms. Track expenses across multiple currencies while receiving intelligent forecasts about exchange rate movements.

## Key Features

- **AI Currency Predictions** - 7-day exchange rate forecasts using linear regression
- **Multi-Currency Support** - USD, EUR, GBP, JPY, AUD, RON with automatic conversion
- **Smart Analytics** - Category breakdowns and spending insights
- **Local SQLite Database** - Secure local data storage
- **Modern JavaFX Interface** - Intuitive and responsive UI

## AI System Highlights

The application implements **machine learning from scratch** (no external ML libraries):
- Linear regression algorithm for trend analysis
- 30-day historical data simulation with realistic market patterns
- 7-day future exchange rate predictions
- Trading recommendations based on predicted movements
- Asynchronous processing for responsive UI

## Quick Start

**Prerequisites:** Java 22+, Maven 3.8+, Internet connection

```bash
# Clone and run
git clone https://github.com/bughi04/expense-management-ai.git
cd expense-management-ai
mvn clean javafx:run
```

The app starts immediately with automatic database creation.

## Complete Documentation

For detailed technical information, setup instructions, AI algorithm explanations, and more:

**[View Complete Technical Documentation](docs/technical-documentation.pdf)**

The comprehensive documentation includes:
- Detailed setup and installation guide
- Complete AI algorithm explanation with mathematical foundations
- System architecture and design patterns
- Database schema and operations
- Troubleshooting guide
- Future enhancements roadmap
- Full source code explanations

## Technologies used

Java 22 • JavaFX 22.0.1 • SQLite • Maven • Machine Learning (Linear Regression)

## Contributing

Contributions welcome! Please see the [technical documentation](docs/technical-documentation.pdf) for development guidelines and architecture details.

---

**Star this repo if you find it useful!**
