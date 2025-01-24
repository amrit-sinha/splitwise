import java.util.List;
public class Expense {
    String paidBy;
    double amount;
    String type;
    List<String> usersInvolved;
    List<Double> shares;

    public Expense(String paidBy, double amount, String type, List<String> usersInvolved, List<Double> shares){
        this.paidBy = paidBy;
        this.amount = amount;
        this.type = type;
        this.usersInvolved = usersInvolved;
        this.shares = shares;
    }
}
