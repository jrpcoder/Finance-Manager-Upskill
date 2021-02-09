package pt.upskill.projeto2.financemanager.gui;

import pt.upskill.projeto2.financemanager.PersonalFinanceManager;
import pt.upskill.projeto2.financemanager.accounts.Account;
import pt.upskill.projeto2.financemanager.accounts.SavingsAccount;
import pt.upskill.projeto2.financemanager.accounts.StatementLine;
import pt.upskill.projeto2.financemanager.accounts.formats.FileAccountFormat;
import pt.upskill.projeto2.financemanager.accounts.formats.LongStatementFormat;
import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.date.Date;
import pt.upskill.projeto2.financemanager.filters.AccountIdSelector;
import pt.upskill.projeto2.financemanager.filters.Filter;
import pt.upskill.projeto2.financemanager.filters.NoCategorySelector;
import pt.upskill.projeto2.utils.Menu;

import java.util.Collection;
import java.util.List;

/**
 * @author upSkill 2020
 * <p>
 * ...
 */

public class PersonalFinanceManagerUserInterface {

    public PersonalFinanceManagerUserInterface(
            PersonalFinanceManager personalFinanceManager) {
        this.personalFinanceManager = personalFinanceManager;
    }

    private static final String OPT_GLOBAL_POSITION = "Posição Global";
    private static final String OPT_ACCOUNT_STATEMENT = "Movimentos Conta";
    private static final String OPT_CATEGORIES = "Categorias";
    private static final String OPT_ANALISE = "Análise";
    private static final String OPT_EXIT = "Sair";

    private static final String OPT_LIST_CATEGORIES = "Listar categorias";
    private static final String OPT_UNCATEGORIZED_MOVEMENTS = "Listar movimentos não categorizados por conta";
    private static final String OPT_ADD_CATEGORY = "Adicionar categoria";
    private static final String OPT_CATEGORIZE = "Adicionar descrição para categorizar";

    private static final String OPT_MONTHLY_SUMMARY = "Evolução global por mês";
    private static final String OPT_PREDICTION_PER_CATEGORY = "Previsão gastos totais do mês por categoria";
    private static final String OPT_ANNUAL_INTEREST = "Previsão juros anuais";

    private static final String[] OPTIONS_ANALYSIS = {OPT_MONTHLY_SUMMARY, OPT_PREDICTION_PER_CATEGORY, OPT_ANNUAL_INTEREST};
    private static final String[] OPTIONS_CATEGORIES = {OPT_LIST_CATEGORIES, OPT_UNCATEGORIZED_MOVEMENTS, OPT_ADD_CATEGORY, OPT_CATEGORIZE};
    private static final String[] OPTIONS = {OPT_GLOBAL_POSITION,
            OPT_ACCOUNT_STATEMENT, OPT_CATEGORIES, OPT_ANALISE, OPT_EXIT};

    private PersonalFinanceManager personalFinanceManager;

    public Account getSelectedAccount(String givenID) {
        Filter filterAccountsPerID = new Filter(new AccountIdSelector(Long.parseLong(givenID)));
        List<Account> selectedAccount = (List) filterAccountsPerID.apply(personalFinanceManager.getListAccounts());
        if(selectedAccount.size() > 0) {
            return selectedAccount.get(0);
        } else {
            return null;
        }
    }

    public void askToSave() {
        boolean toSave = Menu.yesOrNoInput("Pretende gravar antes de sair?");
        if(toSave) {
            personalFinanceManager.saveAccountData(personalFinanceManager.getListAccounts());
            personalFinanceManager.saveCategories(personalFinanceManager.getCategoryList());
        }
    }

    public void execute() {
        String nl = System.lineSeparator();
        String selection = Menu.requestSelection("Finance Manager", OPTIONS);
        if(selection != null) {
            switch (selection) {
                case OPT_GLOBAL_POSITION:
                    System.out.println("-------------------------------------------------");
                    System.out.println("Global Position");
                    System.out.println("Account Number  Balance");
                    for (Account account : personalFinanceManager.getListAccounts()) {
                        String status = account.getId() + "   " + account.currentBalance();
                        System.out.println(status);
                    }
                    System.out.println(nl);
                    System.out.println("Total Balance:  " + personalFinanceManager.totalBalance());
                    execute();
                    break;
                case OPT_ACCOUNT_STATEMENT:
                    String[] accounts = new String[personalFinanceManager.getListAccounts().size()];
                    for(int i = 0; i < accounts.length; i++) {
                        accounts[i] = "AccountID: " + personalFinanceManager.getListAccounts().get(i).getId();
                    }
                    String accountSelected = Menu.requestSelection("Escolha uma conta", accounts);
                    if(accountSelected != null) {
                        String[] tokens = accountSelected.split(" ");
                        Account selectedAccount = getSelectedAccount(tokens[1].trim());
                        String formatedAccount = new FileAccountFormat().format(selectedAccount);
                        System.out.println(nl);
                        System.out.println("-------------------------------------------------");
                        System.out.println(formatedAccount);
                    }
                    execute();
                    break;
                case OPT_CATEGORIES:
                    String categoriesSelection = Menu.requestSelection("Finance Manager - Categories", OPTIONS_CATEGORIES);
                    if(categoriesSelection != null) {
                        switch (categoriesSelection) {
                            case OPT_LIST_CATEGORIES:
                                System.out.println("-------------------------------------------------");
                                System.out.println("Available Categories");
                                for (Category category : personalFinanceManager.getCategoryList()) {
                                    System.out.println("");
                                    System.out.println("Category: " + category.getName());
                                    System.out.println("Tags:");
                                    for (String tag : category.getTagsList()) {
                                        System.out.println("- " + tag);
                                    }
                                }
                                break;
                            case OPT_UNCATEGORIZED_MOVEMENTS:
                                System.out.println("-------------------------------------------------");
                                System.out.println("Uncategorized Transactions per Account");
                                for (Account account : personalFinanceManager.getListAccounts()) {
                                    System.out.println("");
                                    System.out.println("Account Number: " + account.getId());
                                    Filter filterMovementsPerCategory = new Filter(new NoCategorySelector());
                                    List<StatementLine> selectedMovements = (List) filterMovementsPerCategory.apply(account.getListMovements());
                                    if (selectedMovements.size() == 0) {
                                        System.out.println("No uncategorized transactions");
                                    } else {
                                        for (StatementLine movement : selectedMovements) {
                                            String formatedMovement = new LongStatementFormat().format(movement);
                                            System.out.println(formatedMovement);
                                        }
                                    }
                                }
                                System.out.println("");
                                System.out.println("To categorize transactions please add the description within the 'Adicionar descrição' Menu option.");
                                System.out.println("");
                                break;
                            case OPT_ADD_CATEGORY:
                                String categoryName;
                                boolean hasCategory;
                                boolean repeatInput;
                                do {
                                    categoryName = Menu.requestInput("Indique o nome da nova categoria");
                                    hasCategory = false;
                                    repeatInput = false;
                                    if(categoryName == null) {
                                        break;
                                    } else if(categoryName.equals("")) {
                                        System.out.println("Invalid input");
                                        repeatInput = true;
                                    } else {
                                        for (Category category : personalFinanceManager.getCategoryList()) {
                                            if (category.getName().equals(categoryName)) {
                                                hasCategory = true;
                                                System.out.println("");
                                                System.out.println("The category already exists. Try again please.");
                                            }
                                        }
                                    }
                                } while (hasCategory || repeatInput);
                                if(categoryName != null) {
                                    Category newCategory = new Category(categoryName);
                                    personalFinanceManager.getCategoryList().add(newCategory);
                                    if(categoryName.equals("SAVINGS")) {
                                        SavingsAccount.setSavingsCategory(newCategory);
                                    }
                                    for (Account account : personalFinanceManager.getListAccounts()) {
                                        account.autoCategorizeStatements(personalFinanceManager.getCategoryList());
                                    }
                                }
                                break;
                            case OPT_CATEGORIZE:
                                String[] categories = new String[personalFinanceManager.getCategoryList().size()];
                                for (int i = 0; i < categories.length; i++) {
                                    categories[i] = personalFinanceManager.getCategoryList().get(i).getName();
                                }
                                String categorySelected = Menu.requestSelection("Escolha uma categoria", categories);
                                boolean alreadyExists;
                                boolean repeatDescription;
                                String description;
                                if(categorySelected != null) {
                                    do {
                                        description = Menu.requestInput("Indique a nova descrição para categorizar");
                                        alreadyExists = false;
                                        repeatDescription = false;
                                        if(description == null) {
                                            break;
                                        } else if(description.equals("")) {
                                            System.out.println("Invalid input");
                                            repeatDescription = true;
                                        } else {
                                            for (Category category : personalFinanceManager.getCategoryList()) {
                                                if (category.hasDescription(description)) {
                                                    alreadyExists = true;
                                                    System.out.println("-------------------------------------------------");
                                                    System.out.println("That description is already defined in a category. Please try again.");
                                                }
                                            }
                                        }
                                    } while (alreadyExists || repeatDescription);
                                    if(description != null) {
                                        for (Category category : personalFinanceManager.getCategoryList()) {
                                            if (category.getName().equals(categorySelected)) {
                                                category.addDescription(description);
                                            }
                                        }
                                        for (Account account : personalFinanceManager.getListAccounts()) {
                                            account.autoCategorizeStatements(personalFinanceManager.getCategoryList());
                                        }
                                        System.out.println("-------------------------------------------------");
                                        System.out.println("The Transactions with the given description were categorized.");
                                    }
                                    break;
                                }
                        }

                    }
                    execute();
                    break;
                case OPT_ANALISE:
                    String analysisSelection = Menu.requestSelection("Finance Manager", OPTIONS_ANALYSIS);
                    if(analysisSelection != null) {
                        switch (analysisSelection) {
                            case OPT_MONTHLY_SUMMARY:
                                System.out.println("-------------------------------------------------");
                                System.out.println("Totais dos débitos para o mês currente");
                                Date currentDate2 = new Date();
                                for(Account account : personalFinanceManager.getListAccounts()) {
                                    System.out.println("");
                                    System.out.println("AccountID: " + account.getId());
                                    double totalDraft = account.totalForMonth(currentDate2.getMonth().ordinal(), currentDate2.getYear());
                                    System.out.println(totalDraft + " €");
                                }
                                break;
                            case OPT_PREDICTION_PER_CATEGORY:

                                Date currentDate = new Date();
                                String[] accountsToSelect = new String[personalFinanceManager.getListAccounts().size()];
                                for(int i = 0; i < accountsToSelect.length; i++) {
                                    accountsToSelect[i] = "AccountID: " + personalFinanceManager.getListAccounts().get(i).getId();
                                }
                                String accountSelected2 = Menu.requestSelection("Escolha uma conta", accountsToSelect);
                                if(accountSelected2 != null) {
                                    String[] tokens = accountSelected2.split(" ");
                                    Account selectedAccount = getSelectedAccount(tokens[1].trim());
                                    String[] categories = new String[personalFinanceManager.getCategoryList().size()];
                                    for (int i = 0; i < categories.length; i++) {
                                        categories[i] = personalFinanceManager.getCategoryList().get(i).getName();
                                    }
                                    String categorySelected = Menu.requestSelection("Escolha uma categoria", categories);
                                    if(categorySelected != null) {
                                        Category selectedCategory = null;
                                        for(Category category : personalFinanceManager.getCategoryList()) {
                                            if(category.getName().equals(categorySelected)) {
                                                selectedCategory = category;
                                            }
                                        }
                                        double totalDraftMonth = selectedAccount.totalDraftsForCategorySince(selectedCategory, new Date(1, currentDate.getMonth(), currentDate.getYear()));
                                        int daysPassedCurrentMonth = currentDate.getDay();
                                        double estimatedTotalDraftMonth = (totalDraftMonth / daysPassedCurrentMonth) * Date.lastDayOf(currentDate.getMonth(), currentDate.getYear());
                                        System.out.println("-------------------------------------------------");
                                        System.out.println("Previsão do gasto mensal corrente por categoria");
                                        System.out.println("");
                                        System.out.println("AccountID: " + selectedAccount.getId());
                                        System.out.println("Categoria: " + selectedCategory.getName());
                                        System.out.println("Gasto actual mensal absoluto: " + totalDraftMonth);
                                        System.out.println("Gasto estimado mensal: " + estimatedTotalDraftMonth);
                                    }
                                }
                                break;
                            case OPT_ANNUAL_INTEREST:
                                System.out.println("-------------------------------------------------");
                                System.out.println("Annual Interest estimate");
                                for (Account account : personalFinanceManager.getListAccounts()) {
                                    System.out.println("Account Number: " + account.getId());
                                    System.out.println(personalFinanceManager.yearlyInterest(account));
                                }
                                break;
                        }
                        execute();
                    }
                    break;
                case OPT_EXIT:
                    askToSave();
                    break;
            }
        } else {
            askToSave();
        }
    }
}
