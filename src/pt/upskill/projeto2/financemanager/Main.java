package pt.upskill.projeto2.financemanager;

import pt.upskill.projeto2.financemanager.accounts.Account;
import pt.upskill.projeto2.financemanager.accounts.SavingsAccount;
import pt.upskill.projeto2.financemanager.accounts.StatementLine;
import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.gui.PersonalFinanceManagerUserInterface;

import java.io.File;
import java.util.List;

/**
 * @author upSkill 2020
 * <p>
 * ...
 */

public class Main {

    public static void main(String[] args) {

        PersonalFinanceManager personalFinanceManager = new PersonalFinanceManager();
        personalFinanceManager.setCategoryList();
        List<Category> categoryList = personalFinanceManager.getCategoryList();
        for(Category category : categoryList) {
            if(category.getName().equals("SAVINGS")) {
                SavingsAccount.setSavingsCategory(category);
            }
        }

        personalFinanceManager.setListAccounts();
        personalFinanceManager.setListAccountsBank();

        //auto categorize account's movements
        for(Account account : personalFinanceManager.getListAccounts()) {
            account.autoCategorizeStatements(personalFinanceManager.getCategoryList());
        }

        PersonalFinanceManagerUserInterface gui = new PersonalFinanceManagerUserInterface(
                personalFinanceManager);
        gui.execute();

    }

}
