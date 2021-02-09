package pt.upskill.projeto2.financemanager.accounts.formats;

import pt.upskill.projeto2.financemanager.accounts.Account;
import pt.upskill.projeto2.financemanager.accounts.StatementLine;
import pt.upskill.projeto2.financemanager.date.Date;

public class FileAccountFormat implements Format<Account>{
    @Override
    public String format(Account a1) {
        String nl = System.getProperty("line.separator");
        String movements = "";
        for(StatementLine movement : a1.getListMovements()) {
            movements = movements + new LongStatementFormat().format(movement) + nl;
        }
        String formatedAccount = "Account Info - " + new Date().toString() + nl
                + "Account  ;" + a1.getId() + " ; EUR  ;" + a1.getName() +" ;"+ a1.getType() +" ;" + nl
                + "Start Date ;" + a1.getStartDate().toString() + nl
                + "End Date ;" + a1.getEndDate().toString() + nl
                + "Date ;Value Date ;Description ;Draft ;Credit ;Accounting balance ;Available balance" + nl
                + movements.replace("\t", ";");

        return formatedAccount;
    }
}
