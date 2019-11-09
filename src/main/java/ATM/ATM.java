package ATM;

import ATM.accounts.Account;
import ATM.accounts.Checking;
import ATM.accounts.Investment;
import ATM.accounts.Savings;
import ATM.menus.MainMenu;
import ATM.menus.NewUserMenu;
import ATM.services.AccountServices;
import ATM.services.TransactionServices;
import ATM.services.TransferServices;
import ATM.services.UserServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class ATM {

    private User currentUser;

    private DB userDB;          // 0: ID 1: Last Name 2: First Name 3: cardNum 4: PW
    private DB transactionDB;   // 0: credit/debit 1: accountID 2: amount (signed) 3: timeStamp 4: description
    private DB accountDB;       // 0: accountID 1: ownerID 2: balance 3: type 4: risk/interest/null (type-dependent)

    private UserServices userServices;
    private TransactionServices transactionServices;
    private AccountServices accountServices;


    public ATM(String userDBName, String accountDBName, String transactionDBName) {
        this.currentUser = null;
        try {
            this.userDB = new DB(userDBName, 5);
            this.transactionDB = new DB(transactionDBName, 5);
            this.accountDB = new DB(accountDBName, 6);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.userServices = new UserServices(userDB, this);
        this.transactionServices = new TransactionServices(transactionDB, this);
        this.accountServices = new AccountServices(accountDB, this);
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public DB getUserDB() {
        return this.userDB;
    }

    public DB getTransactionDB() {
        return this.transactionDB;
    }

    public DB getAccountDB() {
        return this.accountDB;
    }

    public UserServices getUserServices() {
        return userServices;
    }

    public TransactionServices getTransactionServices() {
        return transactionServices;
    }

    public AccountServices getAccountServices() {
        return accountServices;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }



    // load database info from disk
    public void loadDBs() {
//        // find accounts, create instances
//        ArrayList<String[]> accountsInfo = getAccountInfoByUser(this.currentUser);
//        ArrayList<accounts.Account> accounts = new ArrayList<>();
//        for (String[] acctInfo : accountsInfo) {
//            accounts.add(new accounts.Account(...));
//        }
//        //
    }


    public void authenticate() {
        //Read ATM.User's card
        Console.println("Card Number:");
        int cardNum = Console.getInteger();

        // find user in ATM.DB
        String[] userInfo = userServices.getUserInfoByCardNum(cardNum);
        if (userInfo == null){
            this.authenticate();
        }
        // check PW
        String password = Console.getInput("Enter Password: ");
        if(password.equals(userInfo[4])) {
            // 0: ID 1: Last Name 2: First Name 3: cardNum 4: PW
            currentUser = new User(userInfo[2], userInfo[1], userInfo[4], Integer.parseInt(userInfo[0]), Integer.parseInt(userInfo[3]));
        } else {
            this.authenticate();
        }
    }

    // log in user - don't return until you do
    public void getUser() {
        String header = "Welcome to ZipCode National Bank";
        int input = Console.getInput(header, new String[] {"Insert Card", "Open an Account"});

        switch (input) {
            case 1:
                this.authenticate();
                if (this.currentUser == null) {
                    return;
                }
                break;
            case 2:
                new NewUserMenu(this).displayMenu();
                break;
        }
    }


//
//    public void addAccount(ArrayList<Account> usrAccounts, Double deposit) {
//        String header = "Choose accounts.Account Type:";
//        String input = Console.getInput(header, new String[] {"Checking", "Savings", "Investment", "Back to Main Menu" });
//        Account newAccount;
//        Transaction transaction;
//
//
//        switch (input) {
//            case "1":
//                newAccount = new Checking(deposit, this.currentUser.getUserID(), (int)(Math.random()*1000), Account.Status.valueOf("OPEN"));
//                accountServices.saveAccountToDB(newAccount);
//                usrAccounts.add(newAccount);
//
//                transaction = new Transaction(deposit, new Date(), newAccount.getAcctNum(), "Opened account", true);
//                transactionServices.saveTransactionToDB(transaction);
//                break;
//            case "2":
//                Double interestRate = .01 * (1 + Math.floor(deposit/1000));
//                Console.println(String.format("Your interest rate: %.2f", interestRate)+"%%");
//                newAccount = new Savings(deposit, this.currentUser.getUserID(), (int)(Math.random()*1000), interestRate, Account.Status.valueOf("OPEN"));
//                accountServices.saveAccountToDB(newAccount);
//                usrAccounts.add(newAccount);
//
//                transaction = new Transaction(deposit, new Date(), newAccount.getAcctNum(), "Opened account", true);
//                transactionServices.saveTransactionToDB(transaction);
//                break;
//            case "3":
//                Console.print("On a scale of 1-10, enter your risk tolerance ");
//                int riskInput = Console.getInteger(10);
//                Double risk = riskInput * .01;
//                newAccount = new Investment(deposit, this.currentUser.getUserID(), (int)(Math.random()*1000), risk, Account.Status.valueOf("OPEN"));
//                accountServices.saveAccountToDB(newAccount);
//                usrAccounts.add(newAccount);
//
//                transaction = new Transaction(deposit, new Date(), newAccount.getAcctNum(), "Opened account", true);
//                transactionServices.saveTransactionToDB(transaction);
//                break;
//            case "4":
//                break;
//        }
//
//
//    }

//    public void accountMenu(Account account) {
//        String header = account.getClass().getName() + " accounts.Account #" + account.getAcctNum().toString() + "  Balance: $" + String.format("%,.2f", account.getBalance());
//        if (account instanceof Savings) {
//            header += "  Interest Rate: " + String.format("%.2f", ((Savings) account).getInterestRate())+"%%";
//        } else if (account instanceof Investment) {
//            header += "  Risk: " + String.format("%d", Math.round(100*((Investment) account).getRisk()))+"/10";
//        }
//        String input = Console.getInput(header, new String[] {"View ATM.Transaction History", "Deposit", "Withdrawal", "Close accounts.Account", "Transfer", "Back to ATM.Main ATM.interfaces.Menu" });
//
//        Double deposit;
//        Transaction transaction;
//        switch (input) {
//            case "1":
//                Console.outputTransactionsWithHeader("Transaction History", transactionServices.getTransactionsForAccount(account));
//                break;
//            case "2":
//                deposit = Console.getCurrency("Deposit amount: ");
//                account.deposit(deposit);
//                accountServices.saveAccountToDB(account);
//                transaction = new Transaction(deposit, new Date(), account.getAcctNum(), "ATM deposit", true);
//                transactionServices.saveTransactionToDB(transaction);
//                break;
//            case "3":
//                deposit = Console.getCurrency("Withdrawal amount: ");
//                if (deposit <= account.getBalance()) {
//                    account.deposit(-1 * deposit);
//                    accountServices.saveAccountToDB(account);
//                    transaction = new Transaction(deposit, new Date(), account.getAcctNum(), "ATM withdrawal", false);
//                    transactionServices.saveTransactionToDB(transaction);
//                } else {
//                    Console.println("Insufficient funds");
//                    Console.getInput("\nPress Enter");
//                }
//                break;
//            case "4":
//
//                if (account.getBalance() == 0) {
//
//                    accountServices.deleteAccountFromDB(account);
//                    transaction = new Transaction(0.0, new Date(), account.getAcctNum(), "Account Closed", false);
//                    transactionServices.saveTransactionToDB(transaction);
//                } else {
//                    Console.println("Account still contains funds. Withdraw or transfer all funds before closing.");
//                    Console.getInput("\nPress Enter");
//                }
//                break;
//            case "5":
//
//                Console.println("Number of account to transfer to");
//                int ActToTransferTo = Console.getInteger();
//                String[] actInfo = accountServices.getAccountInfoByID(ActToTransferTo);
//                // 0: accountID 1: ownerID 2: balance 3: type 4: risk/interest/null (type-dependent)
//                Account act = accountServices.getAccountByInfo(actInfo);
//                deposit = Console.getCurrency("Transfer amount");
//
//                if(deposit < account.getBalance()) {
//                    account.deposit(-1 * deposit);
//                    act.deposit(deposit);
//
//                    accountServices.saveAccountToDB(account);
//                    transaction = new Transaction(-1 * deposit, new Date(), account.getAcctNum(), "ATM Transfer", false);
//                    transactionServices.saveTransactionToDB(transaction);
//
//                    accountServices.saveAccountToDB(act);
//                    transaction = new Transaction(deposit, new Date(), act.getAcctNum(), "ATM Transfer", true);
//                    transactionServices.saveTransactionToDB(transaction);
//                } else {
//                    Console.println("Insufficient funds in account");
//                }
//
//                break;
//            case "6":
//                break;
//        }
//    }
//

    public void serviceLoop() {
        // authenticate a user (or die trying)
        // only returns null if the magic secret exit code is called

        this.transactionServices.linkServices();
        this.accountServices.linkServices();
        //this.userServices.linkServices();

        getUser();

        interestRateChange();
        applyInterest();
        applyReturns();

        new MainMenu(this).displayMenu();

        logOut();

        serviceLoop();
    }

    public void applyInterest() {
        ArrayList<Account> userAccounts = accountServices.getAccountsForUser(this.currentUser);
        for (Account account : userAccounts) {
            if (account instanceof Savings) {
                calcInterest(account);
            }
        }
    }

    public void interestRateChange() {
        ArrayList<Account> userAccounts = accountServices.getAccountsForUser(this.currentUser);
        Random random = new Random();

        for (Account account : userAccounts) {
            if (account instanceof Savings) {
                if (random.nextInt(5) >= 4) {
                    double newRate = ((Savings) account).getInterestRate() - .05 + .01 * random.nextInt(11);
                    ((Savings) account).setInterestRate(newRate);
                    accountServices.saveAccountToDB(account);
                    Transaction transaction = new Transaction(Double.parseDouble(String.format("%.2f",account.getBalance())), new Date(), account.getAcctNum(), String.format("Interest rate changed to 0%.2f",newRate), true);
                    transactionServices.saveTransactionToDB(transaction);
                }
            }
        }
    }

    public void calcInterest(Account account) {
        Double interest = ((Savings) account).getInterestRate() * account.getBalance()/100;
        account.deposit(interest);
        accountServices.saveAccountToDB(account);
        Transaction transaction = new Transaction(Double.parseDouble(String.format("%.2f",interest)), new Date(), account.getAcctNum(), "Interest earned", true);
        transactionServices.saveTransactionToDB(transaction);
    }

    public void applyReturns() {
        ArrayList<Account> userAccounts = accountServices.getAccountsForUser(this.currentUser);
        for (Account account : userAccounts) {
            if (account instanceof Investment) {
                calcReturns(account);
            }
        }
    }

    public void calcReturns(Account account) {
        Double multiplier = ((Investment) account).getRisk() * (2 * Math.random() - .8);
        Double earnings =  Math.round((multiplier * account.getBalance()*100d))/100d;
        account.deposit(earnings);
        accountServices.saveAccountToDB(account);
        Boolean isCredit = (earnings > 0);
        Transaction transaction = new Transaction(Double.parseDouble(String.format("%.2f",earnings)), new Date(), account.getAcctNum(), "Investment returns", isCredit);
        transactionServices.saveTransactionToDB(transaction);
    }


    // log out user
    public void logOut() {
        //saveDBs();
        this.currentUser = null;
    }


    /*  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    * DB interaction methods for the ATM
    *
    * We should create a storage class or generic methods in the ATM.DB class or something in the interface, but...
     */ ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    public int getUserCount() {
//        return this.userDB.length();
//    }
//
//    //find accounts by owner id (to then be used by constructor)
//    public int[] getAccountRowsByUser (User user) {
//        int [] recordRowNums;
//        recordRowNums = this.accountDB.findPartialRowMultiple(new String[] {user.getUserID().toString()}, new int[] {1});
//
//        return recordRowNums;
//    }
//
//    // get string representation of one account
//    public String[] getAccountInfoByRow (int rowNum) {
//        return this.accountDB.readRow(rowNum);
//    }
//
//    // account instance from info (pre-existing account)
//    public Account getAccountByInfo (String[] info) {
//        if (info[3].equals("accounts.Checking")) {
//            return new Checking(Double.parseDouble(info[2]), Integer.parseInt(info[1]), Integer.parseInt(info[0]));
//        } else if (info[3].equals("accounts.Savings")) {
//            return new Savings(Double.parseDouble(info[2]), Integer.parseInt(info[1]), Integer.parseInt(info[0]), Double.parseDouble(info[4]));
//        } else if (info[3].equals("accounts.Investment")) {
//            return new Investment(Double.parseDouble(info[2]), Integer.parseInt(info[1]), Integer.parseInt(info[0]), Double.parseDouble(info[4]));
//        }
//        return null;
//    }
//
//    // AL of accounts for a user
//    public ArrayList<Account> getAccountsForUser(User user) {
//        int[] rows = getAccountRowsByUser(user);
//        ArrayList<Account> accounts = new ArrayList<>();
//        for (int row : rows) {
//            accounts.add(getAccountByInfo(getAccountInfoByRow(row)));
//        }
//        return accounts;
//    }
//
//    public int getMaxUserNumber() {
//        ArrayList<String[]> userInfo = new ArrayList<>();
//        userInfo = this.userDB.readAllRows();
//        int maxID = 0;
//        for (String[] user : userInfo) {
//            if (Integer.parseInt(user[0]) > maxID) {
//                maxID = Integer.parseInt(user[0]);
//            }
//        }
//        return maxID;
//    }
//
//    public int getMaxAccountNumber() {
//        ArrayList<String[]> accountInfo = new ArrayList<>();
//        accountInfo = this.accountDB.readAllRows();
//        int maxID = 0;
//        for (String[] account : accountInfo) {
//            if (Integer.parseInt(account[0]) > maxID) {
//                maxID = Integer.parseInt(account[0]);
//            }
//        }
//        return maxID;
//    }
//
//    //find user row by id
//    public Integer getUserRowByID (Integer ID) {
//        return this.userDB.findPartialRow(new String[]{ID.toString()}, new int[]{0});
//    }
//
//    //find user info by id (helper for constructor)
//    public String [] getUserInfoByID (Integer ID) {
//        int rowNumOfUser = this.userDB.findPartialRow(new String[] {ID.toString()}, new int[] {0});
//        return this.userDB.readRow(rowNumOfUser);
//    }
//
//    //find user info by card number (helper for constructor)
//    public String [] getUserInfoByCardNum (Integer cardNum) {
//        int rowNumOfUser = this.userDB.findPartialRow(new String[] {cardNum.toString()}, new int[] {3});
//        return this.userDB.readRow(rowNumOfUser);
//    }
//
//    //find account row by id
//    public Integer getAccountRowByID (Integer ID) {
//        return this.accountDB.findPartialRow(new String[]{ID.toString()}, new int[]{0});
//    }
//
//    //find account info by id (helper for constructor)
//    public String [] getAccountInfoByID (Integer ID) {
//        int rowNumOfAccount = this.accountDB.findPartialRow(new String[] {ID.toString()}, new int[] {0});
//        return this.accountDB.readRow(rowNumOfAccount);
//    }
//
//    public void saveUserToDB(User user) {
//        String[] stringRepOfUser = user.toStringArray();
//        int userID = user.getUserID();
//        int rowNum = getUserRowByID(userID);
//        if (rowNum == -1) { // user isn't in ATM.DB yet
//            this.userDB.addRow(stringRepOfUser);
//        } else { // update a found row
//            this.userDB.replaceRow(rowNum, stringRepOfUser);
//        }
//    }
//
//    public void saveAccountToDB(Account account) {
//        String[] stringRepOfAccount = account.toStringArray();
//        int accountNum = account.getAcctNum();
//        int rowNum = getAccountRowByID(accountNum);
//        if (rowNum == -1) { // account isn't in ATM.DB yet
//            this.accountDB.addRow(stringRepOfAccount);
//        } else { // update a found row
//            this.accountDB.replaceRow(rowNum, stringRepOfAccount);
//        }
//    }
//
//    public void deleteAccountFromDB(Account account) {
//        String[] stringRepOfAccount = account.toStringArray();
//        int accountNum = account.getAcctNum();
//        int rowNum = getAccountRowByID(accountNum);
//        if (rowNum == -1) { // account isn't in ATM.DB yet
//            this.accountDB.addRow(stringRepOfAccount);
//            return;
//        } else { // update a found row
//            this.accountDB.deleteRow(rowNum);
//        }
//    }
//
//    public int[] getTransactionRowsByUser (User user) {
//        int[] accountRows =  getAccountRowsByUser(user);
//        ArrayList<Integer> accountNums = new ArrayList<>();
//        for (int row : accountRows) {
//            accountNums.add(Integer.parseInt(getAccountInfoByRow(row)[0]));
//        }
//
//        ArrayList<Integer> rows = new ArrayList<>();
////        int [] recordRowNums = null;
////        for (int accountNum : accountNums) {
////            recordRowNums = this.transactionDB.findPartialRowMultiple(new String[]{Integer.toString(accountNum)}, new int[]{1});
////
////        }
//        ArrayList<String[]> transData = transactionDB.readAllRows();
//
//        for (int i = 0; i < transData.size(); i++) {
//            for (int acctNum : accountNums) {
//                if ((int) Integer.parseInt(transData.get(i)[1]) == acctNum) {
//                    rows.add(i);
//                }
//            }
//        }
//
//        int[] results = new int[rows.size()];
//        for (int i = 0; i < rows.size(); i++) {
//            results[i] = rows.get(i);
//        }
//
//        return results;
//    }
//
//    public int[] getTransactionRowsByAccount (Account account) {
//        return this.transactionDB.findPartialRowMultiple(new String[]{Integer.toString(account.getAcctNum())}, new int[]{1});
//    }
//
//    // get string array representation of one transaction
//    public String[] getTransactionInfoByRow (int rowNum) {
//        return this.transactionDB.readRow(rowNum);
//    }
//
//    public ArrayList<Transaction> getTransactionsForUser(User user) {
//        return getTransactionsForRows(getTransactionRowsByUser(user));
//    }
//
//    public ArrayList<Transaction> getTransactionsForAccount(Account account) {
//        return getTransactionsForRows(getTransactionRowsByAccount(account));
//    }
//
//    public ArrayList<Transaction> getTransactionsForRows(int[] rows) {
//        ArrayList<Transaction> transactions = new ArrayList<>();
//        String[] info = new String[5];
//        for (int row : rows) {
//            info = getTransactionInfoByRow(row);
//            try {
//                transactions.add(new Transaction(
//                        Double.parseDouble(info[2]),
//                        new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(info[3]),
//                        Integer.parseInt(info[1]),
//                        info[4],
//                        info[0].equals("credit")));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return transactions;
//    }
//
//
//
//    public void savePendingTransactionsToDB(ArrayList<Transaction> pendingTransactions) {
//        for (Transaction transaction : pendingTransactions) {
//            this.transactionDB.addRow(transaction.toStringArray());
//        }
//    }
//
//    public void saveTransactionToDB(Transaction transaction) {
//        this.transactionDB.addRow(transaction.toStringArray());
//    }

    /*  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
     * End DB interaction methods for the ATM
     */ ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
