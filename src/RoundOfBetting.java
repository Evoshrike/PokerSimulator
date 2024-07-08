import java.util.LinkedList;
import java.util.Objects;

public class RoundOfBetting {
    public static final char[] choiceList = new char[]{'c', 'r', 'f', 'a'};
    private final LinkedList<Player> players;
    private int bet;
    private int pot;

    private int stillInCount;

    private int turnCount = 0;
    private LinkedList<Card> flop;


    public RoundOfBetting(LinkedList<Player> players, int bet, LinkedList<Card> flop, int pot, int inCount) {

        this.players = players;
        this.bet = bet;
        this.flop = flop;
        this.pot = pot;
        this.stillInCount = inCount;

    }

    public void turn(Player p, Choice c) {
        char choice = c.getChoice();
        int raise = c.getRaise();
        try {
            if (Utils.testCharMembership(choiceList, choice)) {
                if (choice == 'f') {
                    p.fold(this);
                    turnCount++;

                }
                if (choice == 'r') {
                    p.raise(this, raise);
                    turnCount = 0;
                }
                if (choice == 'c') {
                    p.call(this);
                    turnCount++;
                }
                if (choice == 'a') {
                    int new_bet = p.goAllIn();
                    if (new_bet > this.bet) {
                        turnCount = 0;
                        this.bet = new_bet;
                    } else turnCount++;
                }
            }
        } catch (InsufficientFundsException e) {
            if (Objects.equals(e.getMessage(), "c")) {
                p.goAllIn();
                turnCount++;
            } else if (Objects.equals(e.getMessage(), "r")) {
                p.goAllIn();
                turnCount = 0;
            } else System.out.println("Something weird happened");
        }
    }
    public void commenceRound(Round r){
        int maxTurns = players.size() - 1;
        Player curPlayer = players.getFirst();
        int i = 0;
        while (turnCount < maxTurns && stillInCount > 1){
            if (!curPlayer.isFolded()) {
                turn(curPlayer, curPlayer.getChoice(flop));
            }
            i++;
            if (i >= players.size()) i = i % players.size();
            curPlayer = players.get(i);

        }
        if (stillInCount < 2){
           for (Player p : players){
               if (!p.isFolded()){
                   LinkedList<Player> winner = new LinkedList<>();
                   winner.add(p);
                   r.setWinners(winner);

               }
           }
        }



    }
    public int getBet(){
        return this.bet;
    }
    public int getPot(){
        return this.pot;
    }
    public void setBet(int bet){
        this.bet = bet;

    }
    public void setPot(int pot){
        this.pot = pot;
    }
    public int getInCount(){
        return this.stillInCount;
    }
    public void decrementInCount(){
        this.stillInCount--;
    }


}
