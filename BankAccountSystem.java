import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Bank Account Management System
 * Features: Account creation, deposit, withdrawal, balance checking
 * with input validation and error handling.
 */
public class BankAccountSystem {

    // ─────────────────────────────────────────────
    //  BankAccount class
    // ─────────────────────────────────────────────
    static class BankAccount {
        private static int nextAccountNumber = 1001;

        private final int    accountNumber;
        private final String ownerName;
        private       double balance;
        private final List<String> transactionHistory;

        /** Creates a new account with an opening deposit (minimum ₹500). */
        BankAccount(String ownerName, double initialDeposit) {
            if (ownerName == null || ownerName.isBlank()) {
                throw new IllegalArgumentException("Owner name cannot be empty.");
            }
            if (initialDeposit < 500) {
                throw new IllegalArgumentException(
                    "Minimum opening deposit is \u20B9500. Provided:"+"\u20B9" + initialDeposit);
            }
            this.accountNumber      = nextAccountNumber++;
            this.ownerName          = ownerName.trim();
            this.balance            = initialDeposit;
            this.transactionHistory = new ArrayList<>();
            transactionHistory.add(
                String.format("Account opened | Opening deposit: +₹%.2f", initialDeposit));
        }

        // ── Getters ──────────────────────────────
        int    getAccountNumber() { return accountNumber; }
        String getOwnerName()    { return ownerName; }
        double getBalance()      { return balance; }

        // ── Operations ───────────────────────────

        void deposit(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException(
                    "Deposit amount must be positive. Provided: ₹" + amount);
            }
            balance += amount;
            transactionHistory.add(
                String.format("Deposit        | +₹%10.2f  | Balance: ₹%.2f", amount, balance));
        }

        void withdraw(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException(
                    "Withdrawal amount must be positive. Provided: ₹" + amount);
            }
            if (amount > balance) {
                throw new IllegalStateException(
                    String.format(
                        "Insufficient funds. Requested: ₹%.2f | Available: ₹%.2f",
                        amount, balance));
            }
            balance -= amount;
            transactionHistory.add(
                String.format("Withdrawal     | -₹%10.2f  | Balance: ₹%.2f", amount, balance));
        }

        void printStatement() {
            System.out.println("\n╔══════════════════════════════════════════════╗");
            System.out.printf ("║  Account #%-5d | Owner: %-19s║%n",
                               accountNumber, ownerName);
            System.out.printf ("║  Current Balance: ₹%-25.2f║%n", balance);
            System.out.println("╠══════════════════════════════════════════════╣");
            System.out.println("║  Transaction History                         ║");
            System.out.println("╠══════════════════════════════════════════════╣");
            for (String record : transactionHistory) {
                System.out.printf("║  %-44s║%n", record);
            }
            System.out.println("╚══════════════════════════════════════════════╝");
        }

        @Override
        public String toString() {
            return String.format("Account #%d | Owner: %-20s | Balance: ₹%.2f",
                                 accountNumber, ownerName, balance);
        }
    }


    // ─────────────────────────────────────────────
    //  Bank class  (manages multiple accounts)
    // ─────────────────────────────────────────────
    static class Bank {
        private final String         bankName;
        private final List<BankAccount> accounts = new ArrayList<>();

        Bank(String bankName) { this.bankName = bankName; }

        /** Create and register a new account; returns it. */
        BankAccount createAccount(String ownerName, double initialDeposit) {
            BankAccount account = new BankAccount(ownerName, initialDeposit);
            accounts.add(account);
            return account;
        }

        /** Find an account by account number; returns null if not found. */
        BankAccount findAccount(int accountNumber) {
            for (BankAccount acc : accounts) {
                if (acc.getAccountNumber() == accountNumber) return acc;
            }
            return null;
        }

        void listAllAccounts() {
            if (accounts.isEmpty()) {
                System.out.println("  No accounts found.");
                return;
            }
            System.out.println("\n  ── All Accounts in " + bankName + " ──");
            for (BankAccount acc : accounts) {
                System.out.println("  " + acc);
            }
        }

        int accountCount() { return accounts.size(); }
        String getName()   { return bankName; }
    }


    // ─────────────────────────────────────────────
    //  Utility helpers
    // ─────────────────────────────────────────────

    /** Safely reads a positive double from stdin. Returns -1 on invalid input. */
    static double readAmount(Scanner sc, String prompt) {
        System.out.print(prompt);
        String raw = sc.nextLine().trim();
        try {
            double value = Double.parseDouble(raw);
            if (value <= 0) {
                System.out.println("  ✗ Amount must be greater than zero.");
                return -1;
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("  ✗ Invalid number: \"" + raw + "\"");
            return -1;
        }
    }

    /** Safely reads an integer from stdin. Returns -1 on invalid input. */
    static int readInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        String raw = sc.nextLine().trim();
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            System.out.println("  ✗ Invalid number: \"" + raw + "\"");
            return -1;
        }
    }

    /** Prints the main menu and returns a clean, upper-case choice string. */
    static String showMenu(Scanner sc) {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║    BANK ACCOUNT MANAGEMENT       ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. Create New Account           ║");
        System.out.println("║  2. Deposit                      ║");
        System.out.println("║  3. Withdraw                     ║");
        System.out.println("║  4. Check Balance                ║");
        System.out.println("║  5. View Statement               ║");
        System.out.println("║  6. List All Accounts            ║");
        System.out.println("║  7. Exit                         ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.print("  Enter choice (1-7): ");
        return sc.nextLine().trim();
    }


    // ─────────────────────────────────────────────
    //  Main entry point
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
        Scanner sc   = new Scanner(System.in);
        Bank    bank = new Bank("JavaBank");

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   Welcome to " + bank.getName() + " System!    ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            String choice = showMenu(sc);

            switch (choice) {

                // ── 1. Create Account ─────────────
                case "1": {
                    System.out.print("  Enter account holder's name: ");
                    String name = sc.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("  ✗ Name cannot be empty.");
                        break;
                    }
                    double deposit = readAmount(sc,
                        "  Enter opening deposit (min ₹500): ₹");
                    if (deposit < 0) break;
                    try {
                        BankAccount acc = bank.createAccount(name, deposit);
                        System.out.printf(
                            "  ✓ Account created! Account number: %d%n",
                            acc.getAccountNumber());
                    } catch (IllegalArgumentException e) {
                        System.out.println("  ✗ " + e.getMessage());
                    }
                    break;
                }

                // ── 2. Deposit ────────────────────
                case "2": {
                    int accNo = readInt(sc, "  Enter account number: ");
                    if (accNo < 0) break;
                    BankAccount acc = bank.findAccount(accNo);
                    if (acc == null) {
                        System.out.println("  ✗ Account #" + accNo + " not found.");
                        break;
                    }
                    double amount = readAmount(sc, "  Enter deposit amount: ₹");
                    if (amount < 0) break;
                    try {
                        acc.deposit(amount);
                        System.out.printf(
                            "  ✓ ₹%.2f deposited. New balance: ₹%.2f%n",
                            amount, acc.getBalance());
                    } catch (IllegalArgumentException e) {
                        System.out.println("  ✗ " + e.getMessage());
                    }
                    break;
                }

                // ── 3. Withdraw ───────────────────
                case "3": {
                    int accNo = readInt(sc, "  Enter account number: ");
                    if (accNo < 0) break;
                    BankAccount acc = bank.findAccount(accNo);
                    if (acc == null) {
                        System.out.println("  ✗ Account #" + accNo + " not found.");
                        break;
                    }
                    double amount = readAmount(sc, "  Enter withdrawal amount: ₹");
                    if (amount < 0) break;
                    try {
                        acc.withdraw(amount);
                        System.out.printf(
                            "  ✓ ₹%.2f withdrawn. Remaining balance: ₹%.2f%n",
                            amount, acc.getBalance());
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("  ✗ " + e.getMessage());
                    }
                    break;
                }

                // ── 4. Check Balance ──────────────
                case "4": {
                    int accNo = readInt(sc, "  Enter account number: ");
                    if (accNo < 0) break;
                    BankAccount acc = bank.findAccount(accNo);
                    if (acc == null) {
                        System.out.println("  ✗ Account #" + accNo + " not found.");
                    } else {
                        System.out.printf(
                            "  Account #%d (%s) | Balance: ₹%.2f%n",
                            acc.getAccountNumber(), acc.getOwnerName(), acc.getBalance());
                    }
                    break;
                }

                // ── 5. View Statement ─────────────
                case "5": {
                    int accNo = readInt(sc, "  Enter account number: ");
                    if (accNo < 0) break;
                    BankAccount acc = bank.findAccount(accNo);
                    if (acc == null) {
                        System.out.println("  ✗ Account #" + accNo + " not found.");
                    } else {
                        acc.printStatement();
                    }
                    break;
                }

                // ── 6. List All Accounts ──────────
                case "6":
                    bank.listAllAccounts();
                    break;

                // ── 7. Exit ───────────────────────
                case "7":
                    System.out.println("\n  Thank you for using " + bank.getName()
                                       + ". Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("  ✗ Invalid choice. Please enter 1–7.");
            }
        }
        sc.close();
    }
}
