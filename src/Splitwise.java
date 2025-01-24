import java.util.*;
public class Splitwise {
    private static Map<String, User> users = new HashMap<>();
    private static List<Expense> expenses = new ArrayList<>();
    private static Map<String, Map<String, Double>> balances = new HashMap<>();

    public static void addExpense(String paidBy, double amount, int noOfUsers, List<String> usersInvolved, String type, List<Double> shares) {
        if (type.equals("PERCENT")) {
            double totalPercent = shares.stream().mapToDouble(Double::doubleValue).sum();
            if (totalPercent != 100.0) {
                System.out.println("Error: Total percentage is not 100.");
                return;
            }
        } else if (type.equals("EXACT")) {
            double totalAmount = shares.stream().mapToDouble(Double::doubleValue).sum();
            if (totalAmount != amount) {
                System.out.println("Error: Total amount does not match the exact shares.");
                return;
            }
        }
        Expense expense = new Expense(paidBy, amount, type, usersInvolved, shares);
        expenses.add(expense);

        updateBalances(expense);
    }

    public static void updateBalances(Expense expense){
        String paidBy = expense.paidBy;
        double amount = expense.amount;
        List<String> usersInvolved = expense.usersInvolved;
        List<Double> shares = expense.shares;

        if(expense.type.equals("EQUAL")){
            double share = roundOff(amount / usersInvolved.size());
            for(String user: usersInvolved){
                if(!user.equals(paidBy)){
                    updateBalance(user, paidBy, share);
                }
            }
        } else if(expense.type.equals("EXACT")){
            for(int i = 0; i < usersInvolved.size(); ++i){
                String user = usersInvolved.get(i);
                double share = shares.get(i);
                updateBalance(user, paidBy, share);
            }
        } else{
            for(int i = 0; i < usersInvolved.size(); ++i){
                String user = usersInvolved.get(i);
                double share = roundOff((amount * shares.get(i)) / 100 );
                if(!user.equals(paidBy)){
                    updateBalance(user, paidBy, share);
                };
            }
        }
    }

    public static void updateBalance(String user1, String user2, double amount){
        balances.putIfAbsent(user1, new HashMap<>());
        balances.putIfAbsent(user2, new HashMap<>());

        double currentBalance = balances.get(user1).getOrDefault(user2, 0.0);
        balances.get(user1).put(user2, currentBalance + amount);

        currentBalance = balances.get(user2).getOrDefault(user1, 0.0);
        balances.get(user2).put(user1, currentBalance - amount);
    }

    public static void showBalances(){
        if(balances.isEmpty()){
            System.out.println("No balances found.");
            return;
        }

        for(String user1: balances.keySet()){
            for(String user2: balances.get(user1).keySet()){
                double balance = balances.get(user1).get(user2);
                if (balance > 0) {
                    System.out.println(users.get(user1).name + " owes " + users.get(user2).name + " = " + roundOff(balance));
                }
            }
        }
    }

    public static void showUserBalances(String userId){
        if(!balances.containsKey(userId)){
            System.out.println("No balances found.");
            return;
        }

        boolean hasBalances = false;
        for (String otherUser : balances.get(userId).keySet()) {
            double balance = balances.get(userId).get(otherUser);
            if (balance != 0) {
                hasBalances = true;
                if (balance > 0) {
                    System.out.println(users.get(userId).name + " owes " + users.get(otherUser).name + ": " + roundOff(balance));
                } else {
                    System.out.println(users.get(otherUser).name + " owes " + users.get(userId).name + ": " + roundOff(-balance));
                }
            }
        }

        if (!hasBalances) {
            System.out.println("No balances");
        }
    }

    public static double roundOff(double value) {
        return Math.round(value * 100.0) / 100.0;
    }


    public static void main(String[] args) {
        users.put("u1", new User("u1", "User1", "user1@example.com", "1234567890"));
        users.put("u2", new User("u2", "User2", "user2@example.com", "1234567890"));
        users.put("u3", new User("u3", "User3", "user3@example.com", "1234567890"));
        users.put("u4", new User("u4", "User4", "User4@example.com", "1234567890"));

        showBalances();
        showUserBalances("u1");
        addExpense("u1", 1000, 4, Arrays.asList("u1", "u2", "u3", "u4"), "EQUAL", null);
        showUserBalances("u4");
        showUserBalances("u1");
        addExpense("u1", 1250, 2, Arrays.asList("u2", "u3"), "EXACT", Arrays.asList(370.0, 880.0));
        showBalances();
        addExpense("u4", 1200, 4, Arrays.asList("u1", "u2", "u3", "u4"), "PERCENT", Arrays.asList(40.0, 20.0, 20.0, 20.0));
        showUserBalances("u1");
        showBalances();
    }
}