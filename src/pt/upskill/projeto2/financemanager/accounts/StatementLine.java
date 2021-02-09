package pt.upskill.projeto2.financemanager.accounts;

import java.io.FileNotFoundException;
import java.lang.IllegalArgumentException;
import java.util.Arrays;
import java.util.Scanner;

import pt.upskill.projeto2.financemanager.accounts.formats.LongStatementFormat;
import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.date.Date;


public class StatementLine {

	private Date date;
	private Date valueDate;
	private String description;
	private double draft;
	private double credit;
	private double accountingBalance;
	private double availableBalance;
	private Category category;

	public StatementLine(Date date, Date valueDate, String description, double draft, double credit, double accountingBalance, double availableBalance, Category category) {
		if(date == null || valueDate == null || description == null || description.equals("") || credit < 0 || draft > 0) {
			throw new IllegalArgumentException();
		}
		this.date = date;
		this.valueDate = valueDate;
		this.description = description;
		this.draft = draft;
		this.credit = credit;
		this.accountingBalance = accountingBalance;
		this.availableBalance = availableBalance;
		this.category = category;
	}

	public Date getDate() {
		// TODO Auto-generated method stub
		return date;
	}

	public Date getValueDate() {
		// TODO Auto-generated method stub
		return valueDate;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	public double getCredit() {
		// TODO Auto-generated method stub
		return credit;
	}

	public double getDraft() {
		// TODO Auto-generated method stub
		return draft;
	}

	public double getAccountingBalance() {
		// TODO Auto-generated method stub
		return accountingBalance;
	}

	public double getAccountingBallance() {
		// TODO Auto-generated method stub
		return accountingBalance;
	}

	public double getAvailableBalance() {
		// TODO Auto-generated method stub
		return availableBalance;
	}

	public Category getCategory() {
		// TODO Auto-generated method stub
		return category;
	}

	public static StatementLine newStatementLine(String line) throws FileNotFoundException {
		// TODO Auto-generated method stub
		//System.out.println(line);
		String[] tokens = line.split(";");
		String[] dateTokens = tokens[0].split("-");
		//System.out.println(Arrays.toString(dateTokens));
		String[] valueDateTokens = tokens[1].split("-");
		//System.out.println(Arrays.toString(valueDateTokens));
		return new StatementLine(new Date(Integer.parseInt(dateTokens[0].trim()), Integer.parseInt(dateTokens[1].trim()), Integer.parseInt(dateTokens[2].trim())),
				new Date(Integer.parseInt(valueDateTokens[0].trim()), Integer.parseInt(valueDateTokens[1].trim()), Integer.parseInt(valueDateTokens[2].trim())),
				tokens[2].trim(),
				tokens[3].equals("") ? 0.0 : Double.parseDouble(tokens[3].trim()),
				tokens[4].equals("") ? 0.0 : Double.parseDouble(tokens[4].trim()),
				tokens[5].equals("") ? 0.0 : Double.parseDouble(tokens[5].trim()),
				tokens[6].equals("") ? 0.0 : Double.parseDouble(tokens[6].trim()), null);
	}

	public void setCategory(Category cat) {
		// TODO Auto-generated method stub
		this.category = cat;
	}



	@Override
	public String toString() {
		return getValueDate() + ";" + getDescription() + ";" + getDraft() + ";" + getCredit() + ";" + getAccountingBalance() + ";" + getAvailableBalance();
	}
}
