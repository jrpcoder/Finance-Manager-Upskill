package pt.upskill.projeto2.financemanager.accounts.formats;

public interface StatementLineFormat<T> {
    String format(T objectToFormat);
    String fields();
}
