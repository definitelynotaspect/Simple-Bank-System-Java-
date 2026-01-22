import java.util.*;

// CLASS: Account (Encapsulated)
class Account {
    private String bankName;
    private String accountName;
    private String accountNumber;
    private double balance;
    private String pin;

    // Constructor
    public Account(String bankName, String accountName, String accountNumber, double balance, String pin) {
        this.bankName = bankName;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.pin = pin;
    }

    // Getters
    public String getBankName() {
        return bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public boolean verifyPin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public void setPin(String newPin) {
        this.pin = newPin;
    }

    // Deposit method
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.printf("PHP %.2f deposited successfully.%n", amount);
        } else {
            System.out.println("[X] Deposit amount must be greater than zero!");
        }
    }

    // Withdraw method with ATM or Bank option
    public void withdraw(double amount, boolean isATM) {
        double fee = isATM ? 15.0 : 0.0;

        if (amount <= 0) {
            System.out.println("[X] Invalid withdrawal amount!");
        } else if (amount + fee > balance) {
            System.out.println("[X] Insufficient funds!");
        } else {
            balance -= (amount + fee);
            if (isATM) {
                System.out.printf("[OK] PHP %.2f withdrawn via ATM. PHP %.2f fee applied.%n", amount, fee);
            } else {
                System.out.printf("[OK] PHP %.2f withdrawn from %s (no fee).%n", amount, bankName);
            }
        }
    }

    public void displayDetails() {
        System.out.println("\nACCOUNT DETAILS");
        System.out.println("Bank Name: " + bankName);
        System.out.println("Account Name: " + accountName);
        System.out.println("Account Number: " + accountNumber);
        System.out.printf("Current Balance: PHP %.2f%n", balance);
    }
}

// CLASS: Bank System (manages multiple accounts)
class BankSystemCore {
    private ArrayList<Account> accounts = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    private final String[] UNIVERSAL_COMMERCIAL_BANKS = {
        "BDO Unibank, Inc.",
        "Metropolitan Bank and Trust Company (Metrobank)",
        "Bank of the Philippine Islands (BPI)",
        "Land Bank of the Philippines (LANDBANK)",
        "Philippine National Bank (PNB)",
        "Security Bank Corporation",
        "Union Bank of the Philippines",
        "China Banking Corporation (China Bank)",
        "Rizal Commercial Banking Corporation (RCBC)",
        "EastWest Banking Corporation (EastWest Bank)",
        "Asia United Bank (AUB)",
        "Robinsons Bank Corporation",
        "Bank of Commerce (BankCom)",
        "Maybank Philippines, Inc.",
        "Citibank Philippines",
        "HSBC Philippines",
        "Standard Chartered Bank Philippines",
        "ANZ Philippines",
        "Deutsche Bank Philippines"
    };

    public void createAccount() {
        System.out.println("\n=== CREATE NEW ACCOUNT ===");

        // Choose bank
        System.out.println("Choose your bank:");
        for (int i = 0; i < UNIVERSAL_COMMERCIAL_BANKS.length; i++) {
            System.out.printf("%d. %s%n", i + 1, UNIVERSAL_COMMERCIAL_BANKS[i]);
        }

        int bankChoice;
        do {
            System.out.print("Enter your choice (1-" + UNIVERSAL_COMMERCIAL_BANKS.length + "): ");
            bankChoice = sc.nextInt();
            sc.nextLine();
        } while (bankChoice < 1 || bankChoice > UNIVERSAL_COMMERCIAL_BANKS.length);

        String bankName = UNIVERSAL_COMMERCIAL_BANKS[bankChoice - 1];

        System.out.print("Enter account name: ");
        String name = sc.nextLine();

        // Account number validation: must be 10–12 digits
        String accNum;
        while (true) {
            System.out.print("Set account number (10–12 digits): ");
            accNum = sc.nextLine();
            if (accNum.matches("\\d{10,12}")) {
                break;
            } else {
                System.out.println("[X] Invalid account number. Please enter 10 to 12 digits only.");
            }
        }

        //  PIN validation: must be 4–6 digits
        String pin;
        while (true) {
            System.out.print("Set PIN (4–6 digits): ");
            pin = sc.nextLine();
            if (pin.matches("\\d{4,6}")) {
                break;
            } else {
                System.out.println("[X] Invalid PIN. Please enter 4 to 6 digits only.");
            }
        }

        System.out.print("Enter initial deposit: PHP ");
        double initial = sc.nextDouble();
        sc.nextLine();

        accounts.add(new Account(bankName, name, accNum, initial, pin));
        System.out.println("\n[OK] Account created successfully at " + bankName + "!\n");
    }

    public Account login() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Enter account name: ");
        String name = sc.nextLine();

        System.out.print("Enter account number: ");
        String accNum = sc.nextLine();

        System.out.print("Enter PIN: ");
        String pin = sc.nextLine();

        for (Account acc : accounts) {
            if (acc.getAccountName().equalsIgnoreCase(name)
                    && acc.getAccountNumber().equals(accNum)
                    && acc.verifyPin(pin)) {
                System.out.println("\n[OK] Login successful! Welcome, " + acc.getAccountName() + " (" + acc.getBankName() + ")");
                return acc;
            }
        }
        System.out.println("[X] Invalid account name, number, or PIN.");
        return null;
    }

    public void forgotPin() {
        System.out.println("\n=== FORGOT PIN ===");
        System.out.print("Enter account name: ");
        String name = sc.nextLine();

        System.out.print("Enter account number: ");
        String accNum = sc.nextLine();

        for (Account acc : accounts) {
            if (acc.getAccountName().equalsIgnoreCase(name)
                    && acc.getAccountNumber().equals(accNum)) {

                // PIN reset with validation
                String newPin;
                while (true) {
                    System.out.print("Enter new PIN (4–6 digits): ");
                    newPin = sc.nextLine();
                    if (newPin.matches("\\d{4,6}")) {
                        break;
                    } else {
                        System.out.println("[X] Invalid PIN format. Please enter 4 to 6 digits only.");
                    }
                }

                acc.setPin(newPin);
                System.out.println("[OK] PIN successfully reset!");
                return;
            }
        }

        System.out.println("[X] Account not found. Make sure the name and number are correct.");
    }

    public void transfer(Account sender) {
        System.out.print("Enter recipient account number: ");
        String recipientNum = sc.nextLine();
        Account receiver = null;

        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(recipientNum)) {
                receiver = acc;
                break;
            }
        }

        if (receiver == null) {
            System.out.println("[X] Recipient account not found.");
            return;
        }

        System.out.print("Enter amount to transfer: PHP ");
        double amount = sc.nextDouble();
        sc.nextLine();

        if (amount <= 0) {
            System.out.println("[X] Invalid amount.");
            return;
        }

        // Apply PHP 10 fee for different banks
        double fee = sender.getBankName().equals(receiver.getBankName()) ? 0.0 : 10.0;
        double totalDeduction = amount + fee;

        if (sender.getBalance() < totalDeduction) {
            System.out.printf("[X] Insufficient funds. You need PHP %.2f including transfer fee.%n", totalDeduction);
            return;
        }

        sender.withdraw(totalDeduction, false);
        receiver.deposit(amount);

        if (fee == 0.0) {
            System.out.printf("[OK] PHP %.2f transferred to %s (same bank, no fee).%n",
                    amount, receiver.getAccountName());
        } else {
            System.out.printf("[OK] PHP %.2f transferred to %s (PHP %.2f interbank fee applied).%n",
                    amount, receiver.getAccountName(), fee);
        }
    }

    public void showAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        System.out.println("\n=== ALL REGISTERED ACCOUNTS ===");
        for (Account acc : accounts) {
            acc.displayDetails();
            System.out.println("---------------------------");
        }
    }
}

// MAIN CLASS
public class BankSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankSystemCore bankCore = new BankSystemCore();
        Account currentUser = null;
        int choice;

        do {
            System.out.println("\n===== SIMPLE BANK SYSTEM (PH) =====");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Forgot PIN");
            System.out.println("4. Show All Accounts");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    bankCore.createAccount();
                    break;
                case 2:
                    currentUser = bankCore.login();
                    if (currentUser != null) {
                        int userChoice;
                        do {
                            System.out.println("\n--- Account Menu ---");
                            System.out.println("1. Deposit");
                            System.out.println("2. Withdraw");
                            System.out.println("3. Balance Inquiry");
                            System.out.println("4. Transfer");
                            System.out.println("5. Logout");
                            System.out.print("Enter your choice: ");
                            userChoice = sc.nextInt();
                            sc.nextLine();

                            switch (userChoice) {
                                case 1:
                                    System.out.print("Enter amount to deposit: PHP ");
                                    double dep = sc.nextDouble();
                                    sc.nextLine();
                                    currentUser.deposit(dep);
                                    break;
                                case 2:
                                    System.out.println("\nSelect withdrawal type:");
                                    System.out.println("1. Withdraw from your bank (PHP 0 fee)");
                                    System.out.println("2. ATM withdrawal (PHP 15 fee)");
                                    System.out.print("Enter your choice: ");
                                    int withdrawType = sc.nextInt();
                                    sc.nextLine();

                                    System.out.print("Enter amount to withdraw: PHP ");
                                    double wd = sc.nextDouble();
                                    sc.nextLine();

                                    boolean isATM = (withdrawType == 2);
                                    currentUser.withdraw(wd, isATM);
                                    break;
                                case 3:
                                    currentUser.displayDetails();
                                    break;
                                case 4:
                                    bankCore.transfer(currentUser);
                                    break;
                                case 5:
                                    System.out.println("Logging out...");
                                    break;
                                default:
                                    System.out.println("[X] Invalid option.");
                            }
                        } while (userChoice != 5);
                    }
                    break;
                case 3:
                    bankCore.forgotPin();
                    break;
                case 4:
                    bankCore.showAllAccounts();
                    break;
                case 5:
                    System.out.println("Thank you for using the Simple Bank System!");
                    break;
                default:
                    System.out.println("[X] Invalid option.");
            }
        } while (choice != 5);

        sc.close();
    }
}
