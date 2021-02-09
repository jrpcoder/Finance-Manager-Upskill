package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.date.Date;
import pt.upskill.projeto2.financemanager.date.Month;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DraftAccount extends Account {
    private final double interestRate = BanksConstants.normalInterestRate();
    public DraftAccount(long id, String name) {
        super(id, name);
    }

    @Override
    public double getInterestRate() {
        return interestRate;
    }

    @Override
    public double estimatedAverageBalance() {
        List<StatementLine> movementsForCurrentYear = new ArrayList<>();
        List<StatementLine> movementsAccount = super.getListMovements();
        Date currentDate = super.getEndDate();
        double estimatedBalance = 0.0;
        if(currentDate != null) {
            boolean hasMovementBeforeCurrentYear = false;
            double balanceToTestFromLastYearMovement = 0.0;
            for(int i = movementsAccount.size() - 1; i >= 0; i--) {
                if(movementsAccount.get(i).getDate().getYear() == currentDate.getYear()){
                    movementsForCurrentYear.add(movementsAccount.get(i));
                }
                if(movementsAccount.get(i).getDate().getYear() == currentDate.getYear() - 1){
                    Date dateFirstmovementYear = movementsForCurrentYear.get(movementsForCurrentYear.size() - 1).getDate();
                    if(!(dateFirstmovementYear.getMonth() == Month.JANUARY && dateFirstmovementYear.getDay() == 1)) {
                        balanceToTestFromLastYearMovement = movementsForCurrentYear.get(i).getAvailableBalance();
                        hasMovementBeforeCurrentYear = true;
                        break;
                    }
                }
            }

            Collections.reverse(movementsForCurrentYear);
            Date testDate = new Date(1, 1, currentDate.getYear());
            double totalBalances = 0.0;
            int totalDays = testDate.diffInDays(currentDate);
            double balanceToTest = balanceToTestFromLastYearMovement;
            if(hasMovementBeforeCurrentYear) {
                balanceToTest = balanceToTestFromLastYearMovement;
            } else {
                balanceToTest = movementsForCurrentYear.get(0).getAvailableBalance();
            }

            for(StatementLine movement : movementsForCurrentYear) {
                totalBalances = totalBalances + (testDate.diffInDays(movement.getDate()) * balanceToTest);
                testDate = movement.getDate();
                balanceToTest = movement.getAvailableBalance();
            }

            estimatedBalance = totalBalances / totalDays;
        }
        return estimatedBalance;
    }

}
