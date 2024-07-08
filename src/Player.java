import java.util.LinkedList;
import java.util.Scanner;
public class Player {
    private Card c1;
    private Card c2;
    private int bet;
    private int remainingMoney;
    private boolean isALlIn = false;
    private boolean folded = false;

    private String name;

    public Player(int balance, String name) {
        this.bet = 0;
        this.remainingMoney = balance;
        this.name = name;
    }
    public Card getC1() {
        return c1;
    }

    // Getter for c2
    public Card getC2() {
        return c2;
    }

    // Getter for bet
    public int getBet() {
        return bet;
    }

    // Getter for totalMoney
    public int getRemainingMoney() {
        return remainingMoney;
    }

    public int goAllIn(){
        bet += remainingMoney;
        remainingMoney = 0;
        isALlIn = true;
        return bet;
    }
    public boolean isALlIn(){
        return isALlIn;
    }
    public boolean isFolded(){
        return folded;
    }
    public void call(RoundOfBetting round) throws InsufficientFundsException {
        int amt = round.getBet() - this.bet;
        if (amt > remainingMoney){
            int need = amt - remainingMoney;
            throw new InsufficientFundsException("c");
        } else {
            this.bet = round.getBet();
            remainingMoney -= amt;
            if (remainingMoney == 0) isALlIn = true;
            round.setPot(round.getPot() + amt);
        }

    }
    public void giveMoney(int amt){
        remainingMoney += amt;
    }
    public void raise(RoundOfBetting round, int amt) throws InsufficientFundsException {
        int total_raise = amt + round.getBet() - this.bet;
        if (total_raise > remainingMoney){
            int need = total_raise - remainingMoney;
            throw new InsufficientFundsException("r");
        } else {
            remainingMoney -= total_raise;
            this.bet += total_raise;
            round.setBet(round.getBet() + amt);
            round.setPot(round.getPot() + total_raise);


        }
    }
    public void fold(RoundOfBetting round){
        this.folded = true;
        round.decrementInCount();

    }
    public Choice getChoice(LinkedList<Card> flop){
        System.out.println("It's your turn, " + getName());
        Scanner scanner = new Scanner(System.in);
        if (flop != null) {
            String flopString = flop.toString();
            System.out.print("The flop is ");
            System.out.print(flopString);
        }
        if (c1 != null) {
            String hand = c1.toString() + ", " + c2.toString();
            System.out.println("You have: " + hand);
        }
        System.out.println("Enter r for raise, c for call, f for fold and a for all in");
        System.out.print("Enter your choice: ");
        char choice = scanner.next().charAt(0);
        if (choice == 'r'){
            System.out.println("Your balance is " + Integer.toString(remainingMoney));
            System.out.print("Enter your raise amount: ");
            int raise = scanner.nextInt();
            return new Choice('r', raise);
        } else return new Choice(choice);
    }

    public String getName() {
        return name;
    }

    public void setC1(Card c1) {
        this.c1 = c1;
    }

    public void setC2(Card c2) {
        this.c2 = c2;
    }
}
