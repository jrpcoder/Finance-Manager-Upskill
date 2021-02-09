package pt.upskill.projeto2.financemanager.accounts;

import pt.upskill.projeto2.financemanager.categories.Category;
import pt.upskill.projeto2.financemanager.date.Date;
import pt.upskill.projeto2.financemanager.date.Month;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public abstract class Account {
    private String name;
    private long id;
    private Date startDate;
    private Date endDate;
    private String type;
    private List<StatementLine> listMovements = new ArrayList<>();

    public Account(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Account newAccount(File file) throws FileNotFoundException {
        List<String[]> fileContentList = new ArrayList();
        List<StatementLine> statementLineList = new ArrayList<>();
        Account newAccount;
        Scanner scanner = new Scanner(file);
        int counter = 0;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(!line.isEmpty()) {
                if(counter < 5) {
                    String[] tokens = line.split(";");
                    fileContentList.add(tokens);
                    counter++;
                } else {
                    statementLineList.add(StatementLine.newStatementLine(line));
                }
            }
        }

        String[] accountInfo = fileContentList.get(1);
        if(accountInfo[4].trim().equals("SavingsAccount")) {
            newAccount = new SavingsAccount(Long.parseLong(accountInfo[1].trim()), accountInfo[3].trim());
            newAccount.setType("SavingsAccount");
        } else {
            newAccount = new DraftAccount(Long.parseLong(accountInfo[1].trim()), accountInfo[3].trim());
            newAccount.setType("DraftAccount");
        }

        String[] startDateInfo = fileContentList.get(2)[1].split("-");
        newAccount.setStartDate(new Date(Integer.parseInt(startDateInfo[0]), Integer.parseInt(startDateInfo[1]), Integer.parseInt(startDateInfo[2])));

        String[] endDateInfo = fileContentList.get(3)[1].split("-");
        newAccount.setEndDate(new Date(Integer.parseInt(endDateInfo[0]), Integer.parseInt(endDateInfo[1]), Integer.parseInt(endDateInfo[2])));

        newAccount.getListMovements().addAll(statementLineList);
        scanner.close();
        return newAccount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String additionalInfo() {
        return "";
    }

    public double currentBalance() {
        if(listMovements.size() > 0) {
            return listMovements.get(listMovements.size() - 1).getAccountingBallance();
        } else {
            return 0.0;
        }
    }

    public abstract double estimatedAverageBalance();

    public List<StatementLine> getListMovements() {
        return listMovements;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public abstract double getInterestRate();

    public void addStatementLine(StatementLine description) {

        if(listMovements.isEmpty()) {
            listMovements.add(description);
            setStartDate(listMovements.get(0).getDate());
            setEndDate(listMovements.get(0).getDate());
        } else {
            listMovements.add(description);
            setEndDate(listMovements.get(listMovements.size() - 1).getDate());
        }
    }

    public void removeStatementLinesBefore(Date date) {
        List<StatementLine> toRemove = new ArrayList<>();
        for(StatementLine movement : listMovements) {
            if(movement.getValueDate().before(date)) {
                toRemove.add(movement);
            }
        }
        for(StatementLine movement : toRemove) {
            listMovements.remove(movement);
        }
    }

    public double totalDraftsForCategorySince(Category category, Date date) {
        double totalDraft = 0.0;
        for(StatementLine movement : listMovements) {
            if(movement.getCategory() == category && movement.getDate().after(date)) {
                totalDraft = totalDraft + movement.getDraft();
            }
        }
        return totalDraft;
    }

    public double totalForMonth(int month, int year) {
        double totalDrafts = 0;
        Month monthValue = Month.values()[month];
        for(StatementLine movement : listMovements) {
            if(movement.getDate().getMonth().equals(monthValue) && movement.getDate().getYear() == year) {
                totalDrafts = totalDrafts + movement.getDraft();
            }
        }
        return totalDrafts;
    }

    public void autoCategorizeStatements(List<Category> categories) {
        if(getType().equals("SavingsAccount") && SavingsAccount.getSavingsCategory() != null){
            for(StatementLine movement : listMovements) {
                movement.setCategory(SavingsAccount.getSavingsCategory());
            }
        } else {
            for(Category category : categories) {
                for(StatementLine movement : listMovements) {
                    if(category.hasDescription(movement.getDescription())) {
                        movement.setCategory(category);
                    }
                }
            }
        }
    }

    public void setName(String other) {
        this.name = other;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }
}
