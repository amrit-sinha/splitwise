import java.util.List;
public class Expense {
    User paidBy;
    double amount;
    String type;
    List<User> usersInvolved;
    List<Double> shares;

    public Expense(User paidBy, double amount, String type, List<User> usersInvolved, List<Double> shares){
        this.paidBy = paidBy;
        this.amount = amount;
        this.type = type;
        this.usersInvolved = usersInvolved;
        this.shares = shares;
    }
}
