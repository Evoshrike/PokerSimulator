import java.util.LinkedList;
import java.util.Scanner;
public class Game {
    private LinkedList<Player> players;

    private LinkedList<Player> remainingPlayers;

    private int buyIn;

    private int blindIndex;
    public Game(){
        players = new LinkedList<>();
        remainingPlayers = new LinkedList<>();
        blindIndex = 0;
    }
    public void runOnTerminal(){
        Scanner s = new Scanner(System.in);
        System.out.print("Enter the buy in (unit pence): ");
        this.buyIn = s.nextInt();
        System.out.print("Enter the number of players: ");
        int noPlayers = s.nextInt();

        for (int i=0; i< noPlayers; i++){
            System.out.print("Enter the name of the next player ");
            String name = s.next();
            Player p = new Player(buyIn, name);
            players.add(p);
            remainingPlayers.add(p);

        }
        boolean playing = true;
        while (playing){
            for (Player p: remainingPlayers){
                if (p.getRemainingMoney() == 0) {
                    System.out.println(p.getName() + ", you are out. Press b to re-buy in, or anything else to exit the game");
                    String choice = s.next();
                    if (choice == "b") {
                        System.out.print("Enter the amount you would like to re-buy in by ");
                        int reBuy = s.nextInt();
                        p.giveMoney(reBuy);
                    } else remainingPlayers.remove(p);
                }
            Round r = new Round(remainingPlayers, blindIndex);
            r.doRound();
            LinkedList<Player> winners = r.getWinners();
            if (winners.size() == 1){
                String output = winners.getFirst().getName();
                output += (" won " + r.getPot() + "!");
                System.out.println(output);

            }
            System.out.println("Type 's' to stop, or anything else to keep going");
            String next = s.next();
            if (next == "s") playing = false;

                }
            blindIndex++;
            }


        System.out.println("Final totals:");
        for (Player p: players){
            System.out.println(p.getName() + ": " + (p.getRemainingMoney() - buyIn));
        }
    }
}
