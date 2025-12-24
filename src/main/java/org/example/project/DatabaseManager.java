package org.example.project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Consolidated database management class for the expense management application
 */
public class DatabaseManager {

    private final String dbUrl = "jdbc:sqlite:expenses.db";

    public DatabaseManager() {
        initializeDatabase();
    }

    /**
     * Initialize the database with necessary tables if they don't exist
     */
    private void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(dbUrl);
             Statement statement = connection.createStatement()) {

            // Create categories table
            String createCategoriesTable = """
                    CREATE TABLE IF NOT EXISTS categories (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT UNIQUE NOT NULL
                    );
                    """;
            statement.execute(createCategoriesTable);

            // Create expenses table
            String createExpensesTable = """
                    CREATE TABLE IF NOT EXISTS expenses (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        category_id INTEGER NOT NULL,
                        amount REAL NOT NULL,
                        transaction_date TEXT NOT NULL,
                        expense_date TEXT NOT NULL,
                        currency TEXT NOT NULL,
                        FOREIGN KEY (category_id) REFERENCES categories (id)
                    );
                    """;
            statement.execute(createExpensesTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all categories from the database
     */
    public List<String> getCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(dbUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT name FROM categories ORDER BY name")) {

            while (resultSet.next()) {
                categories.add(resultSet.getString("name"));
            }
        }
        return categories;
    }

    /**
     * Get expenses for a specific category
     */
    public List<String> getExpensesByCategory(String categoryName) throws SQLException {
        List<String> expenses = new ArrayList<>();
        String query = """
                SELECT e.id, e.amount, e.transaction_date, e.expense_date, e.currency
                FROM expenses e
                JOIN categories c ON e.category_id = c.id
                WHERE c.name = ?
                ORDER BY e.id;
                """;

        try (Connection connection = DriverManager.getConnection(dbUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, categoryName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String expense = String.format(
                            "[ID %d] Amount: %.2f USD, Transaction Date: %s, Expense Date: %s, Original Currency: %s",
                            resultSet.getInt("id"),
                            resultSet.getDouble("amount"),
                            resultSet.getString("transaction_date"),
                            resultSet.getString("expense_date"),
                            resultSet.getString("currency")
                    );
                    expenses.add(expense);
                }
            }
        }
        return expenses;
    }

    /**
     * Get all expenses from the database
     */
    public List<String> getAllExpenses() throws SQLException {
        List<String> expenses = new ArrayList<>();
        String query = """
                SELECT e.id, c.name AS category, e.amount, e.transaction_date, e.expense_date, e.currency
                FROM expenses e
                JOIN categories c ON e.category_id = c.id
                ORDER BY e.id;
                """;

        try (Connection connection = DriverManager.getConnection(dbUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String expense = String.format(
                        "[ID %d] Category: %s, Amount: %.2f USD, Transaction Date: %s, Expense Date: %s, Original Currency: %s",
                        resultSet.getInt("id"),
                        resultSet.getString("category"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("transaction_date"),
                        resultSet.getString("expense_date"),
                        resultSet.getString("currency")
                );
                expenses.add(expense);
            }
        }
        return expenses;
    }

    /**
     * Add a new category to the database
     */
    public void addCategory(String categoryName) throws SQLException {
        String insertCategory = "INSERT INTO categories (name) VALUES (?);";

        try (Connection connection = DriverManager.getConnection(dbUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(insertCategory)) {
            preparedStatement.setString(1, categoryName);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Delete a category from the database
     * @param categoryName Name of the category to delete
     * @param deleteExpenses If true, also deletes all expenses in this category; if false, operation will fail if category has expenses
     * @return True if successful, false if category has expenses and deleteExpenses is false
     */
    public boolean deleteCategory(String categoryName, boolean deleteExpenses) throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl);
            connection.setAutoCommit(false); // Start transaction

            // Check if category has expenses
            if (!deleteExpenses) {
                String checkExpensesQuery = """
                    SELECT COUNT(*) as count FROM expenses e
                    JOIN categories c ON e.category_id = c.id
                    WHERE c.name = ?;
                    """;
                try (PreparedStatement checkStatement = connection.prepareStatement(checkExpensesQuery)) {
                    checkStatement.setString(1, categoryName);
                    try (ResultSet resultSet = checkStatement.executeQuery()) {
                        if (resultSet.next() && resultSet.getInt("count") > 0) {
                            // Category has expenses and we don't want to delete them
                            return false;
                        }
                    }
                }
            } else {
                // Delete all expenses for this category first
                String deleteExpensesQuery = """
                    DELETE FROM expenses 
                    WHERE category_id = (SELECT id FROM categories WHERE name = ?);
                    """;
                try (PreparedStatement deleteExpensesStmt = connection.prepareStatement(deleteExpensesQuery)) {
                    deleteExpensesStmt.setString(1, categoryName);
                    deleteExpensesStmt.executeUpdate();
                }
            }

            // Now delete the category
            String deleteCategoryQuery = "DELETE FROM categories WHERE name = ?;";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteCategoryQuery)) {
                deleteStatement.setString(1, categoryName);
                int rowsAffected = deleteStatement.executeUpdate();

                if (rowsAffected > 0) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Delete an expense by its ID
     */
    public boolean deleteExpense(int expenseId) throws SQLException {
        String deleteQuery = "DELETE FROM expenses WHERE id = ?;";

        try (Connection connection = DriverManager.getConnection(dbUrl);
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            statement.setInt(1, expenseId);
            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }
    }

    /**
     * Add a new expense to the database, converting to USD if necessary
     */
    public void addExpense(String categoryName, double amount, String transactionDate, String expenseDate, String currency) throws SQLException, Exception {
        if (!categoryExists(categoryName)) {
            throw new SQLException("Category does not exist.");
        }

        // Convert the amount to USD using the ApiManager
        double convertedAmount = amount;
        if (!currency.equals("USD")) {
            double conversionRate = ApiManager.getConversionRate(currency, "USD");
            convertedAmount = amount * conversionRate;
        }

        String insertExpense = """
            INSERT INTO expenses (category_id, amount, transaction_date, expense_date, currency)
            VALUES (
                (SELECT id FROM categories WHERE name = ?),
                ?, ?, ?, ?
            );
            """;

        try (Connection connection = DriverManager.getConnection(dbUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(insertExpense)) {

            preparedStatement.setString(1, categoryName);
            preparedStatement.setDouble(2, convertedAmount); // Store converted USD amount
            preparedStatement.setString(3, transactionDate);
            preparedStatement.setString(4, expenseDate);
            preparedStatement.setString(5, currency);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Check if a category exists in the database
     */
    private boolean categoryExists(String categoryName) throws SQLException {
        String query = "SELECT 1 FROM categories WHERE name = ?;";
        try (Connection connection = DriverManager.getConnection(dbUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, categoryName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    /**
     * Get total expense amount by category
     */
    public double getTotalExpensesByCategory(String categoryName) throws SQLException {
        String query = """
                SELECT SUM(amount) as total
                FROM expenses e
                JOIN categories c ON e.category_id = c.id
                WHERE c.name = ?;
                """;

        try (Connection connection = DriverManager.getConnection(dbUrl);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, categoryName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("total");
                }
                return 0.0;
            }
        }
    }

    /**
     * Get total expenses
     */
    public double getTotalExpenses() throws SQLException {
        String query = "SELECT SUM(amount) as total FROM expenses;";

        try (Connection connection = DriverManager.getConnection(dbUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getDouble("total");
            }
            return 0.0;
        }
    }
}