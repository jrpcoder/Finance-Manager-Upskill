package pt.upskill.projeto2.financemanager;

import pt.upskill.projeto2.financemanager.accounts.Account;
import pt.upskill.projeto2.financemanager.accounts.formats.FileAccountFormat;
import pt.upskill.projeto2.financemanager.categories.Category;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PersonalFinanceManager {
    //list with account from account_info folder
    private List<Account> listAccounts = new ArrayList<>();
    //list with accounts from statements folder
    private List<Account> listAccountsBank = new ArrayList<>();
    private final File categoriesFile = new File("account_info/categories");
    private List<Category> categoryList = new ArrayList<>();

    public List<Account> getListAccounts() {
        return listAccounts;
    }

    public File getCategoriesFile() {
        return categoriesFile;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public List<Account> getListAccountsBank() {
        return listAccountsBank;
    }

    public void setListAccounts() {
        File[] accountInfoFiles = new File("account_info").listFiles();
        for(File file : accountInfoFiles) {
            if(!file.getName().equals("categories")) {
                try {
                    listAccounts.add(Account.newAccount(file));
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setListAccountsBank() {
        File[] accountStatementsFiles = new File("statements").listFiles();
        for(File file : accountStatementsFiles) {
            try {
                Account accountBankStatement = Account.newAccount(file);
                listAccountsBank.add(accountBankStatement);
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        addAccountFromStatements();
    }

    //if there is a bank statement from an account not saved yet, then add that new account
    public void addAccountFromStatements(){
        boolean hasAccount = false;
        for(Account accountBank : listAccountsBank) {
            for(Account account : listAccounts) {
                if(accountBank.getId() == account.getId()) {
                    hasAccount = true;
                }
            }
            if(!hasAccount) {
                getListAccounts().add(accountBank);
            }
            hasAccount = false;
        }
    }

    public void setCategoryList() {
        try(Scanner scanner = new Scanner(categoriesFile)) {
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(!line.isEmpty()) {
                    String[] tokens = line.split(";");
                    Category category = new Category(tokens[0]);
                    for(int i = 1; i < tokens.length; i++) {
                        category.addDescription(tokens[i]);
                    }
                    categoryList.add(category);
                }
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveAccountData(List<Account> accounts) {
        for(Account account : accounts) {
            try(PrintWriter fileWriter = new PrintWriter("account_info/" + account.getId() + ".csv")) {
                FileAccountFormat formatedAccount = new FileAccountFormat();
                fileWriter.println(formatedAccount.format(account));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveCategories(List<Category> categories) {
        try(PrintWriter fileWriter = new PrintWriter("account_info/categories")){
            for(Category category : categories) {
                String toPrint = category.getName() + ";";
                for(String tag : category.getTagsList()) {
                    toPrint = toPrint + (tag + ";");
                }
                fileWriter.println(toPrint);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public double totalBalance() {
        double totalBalance = 0.0;
        for(Account account : listAccounts) {
            totalBalance = totalBalance + account.currentBalance();
        }
        return totalBalance;
    }

    public double yearlyInterest(Account account) {
        return account.estimatedAverageBalance() * account.getInterestRate();
    }
}
