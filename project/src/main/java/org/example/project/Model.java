package org.example.project;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class consolidates all model classes: Category, Transaction, Expense, and related interfaces
 */
public class Model {

    // ===================== Category Class =====================
    public static class Category implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;

        public Category(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Category{name='" + name + "'}";
        }
    }

    // ===================== Transaction Class =====================
    public static class Transaction implements Serializable {
        private static final long serialVersionUID = 1L;
        private String date;
        private double amount;

        public Transaction(String date, double amount) {
            this.date = date;
            this.amount = amount;
        }

        public String getDate() {
            return date;
        }

        public double getAmount() {
            return amount;
        }

        @Override
        public String toString() {
            return "Transaction{date='" + date + "', amount=" + amount + "}";
        }
    }

    // ===================== Budget Class =====================
    public static class Budget implements Budgetable {
        private double budgetLimit;

        public Budget(double budgetLimit) {
            this.budgetLimit = budgetLimit;
        }

        @Override
        public void displayBudget() {
            System.out.println("General Budget Limit: $" + budgetLimit);
        }
    }

    // ===================== Expense Class =====================
    public static class Expense implements Serializable {
        private static final long serialVersionUID = 1L;
        private Category category;
        private Transaction[] transactions;
        private double budgetLimit;
        private LocalDate expenseDate;

        public Expense(Category category, Transaction[] transactions, double budgetLimit, LocalDate expenseDate) {
            this.category = category;
            this.transactions = transactions;
            this.budgetLimit = budgetLimit;
            this.expenseDate = expenseDate;
        }

        public Category getCategory() {
            return category;
        }

        public double calculateTotalExpenses() {
            return Arrays.stream(transactions).mapToDouble(Transaction::getAmount).sum();
        }

        public LocalDate getExpenseDate() {
            return expenseDate;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Category: ").append(category.getName()).append("\n");
            sb.append("Expense Date: ").append(expenseDate).append("\n");
            sb.append("Transactions:\n");
            sb.append(String.format("%-15s %-10s\n", "Date", "Amount ($)"));
            sb.append("-".repeat(25)).append("\n");
            for (Transaction transaction : transactions) {
                sb.append(String.format("%-15s %-10.2f\n", transaction.getDate(), transaction.getAmount()));
            }
            sb.append("-".repeat(25)).append("\n");
            sb.append("Total Expenses: $").append(String.format("%.2f", calculateTotalExpenses())).append("\n");
            return sb.toString();
        }
    }

    // ===================== Transaction Summary Class =====================
    public static class TransactionSummary implements TransactionCalculable {
        private Transaction[] transactions;

        public TransactionSummary(Transaction[] transactions) {
            this.transactions = transactions;
        }

        @Override
        public double calculateTotalTransactions() {
            double total = 0;
            for (Transaction t : transactions) {
                total += t.getAmount();
            }
            return total;
        }
    }

    // ===================== Interfaces =====================
    public interface Budgetable {
        void displayBudget();
    }

    public interface TransactionCalculable {
        double calculateTotalTransactions();
    }
}

// ===================== Exception Classes =====================
class DuplicateCategoryException extends Exception {
    public DuplicateCategoryException(String message) {
        super(message);
    }
}

// ===================== API Manager Class =====================
class ApiManager {
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    public static double getConversionRate(String fromCurrency, String toCurrency) throws Exception {
        URL url = new URL(API_URL + fromCurrency);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse.getJSONObject("rates").getDouble(toCurrency);
    }
}