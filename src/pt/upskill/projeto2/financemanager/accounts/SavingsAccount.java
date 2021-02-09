package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.categories.Category;

public class SavingsAccount extends Account{
    public static Category savingsCategory;
    private final double interestRate = BanksConstants.savingsInterestRate();

    public SavingsAccount(long id, String name) {
        super(id, name);
    }

    public static Category getSavingsCategory() {
        return savingsCategory;
    }

    public static void setSavingsCategory(Category savingsCategory) {
        SavingsAccount.savingsCategory = savingsCategory;
    }

    @Override
    public double getInterestRate() {
        return interestRate;
    }

    @Override
    public double estimatedAverageBalance() {
        return super.currentBalance();
    }
}
