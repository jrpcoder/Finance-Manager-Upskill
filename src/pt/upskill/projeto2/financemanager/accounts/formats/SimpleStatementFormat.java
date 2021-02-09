package pt.upskill.projeto2.financemanager.accounts.formats;

import pt.upskill.projeto2.financemanager.accounts.StatementLine;

public class SimpleStatementFormat implements StatementLineFormat<StatementLine>{
    public String format(StatementLine s1) {
        return s1.getDate() + " \t" + s1.getDescription() + " \t" + s1.getDraft() + " \t" + s1.getCredit() + " \t" + s1.getAvailableBalance();
    }

    public String fields() {
        return "Date \tDescription \tDraft \tCredit \tAvailable balance ";
    }
}
