import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Round {
    private LinkedList<Player> players;

    private Deck deck;

    private int blindIndex;

    private LinkedList<Player> winners;

    private int stillInCount;

    private boolean finished;
    private LinkedList<Card> flop;
    private int bet;

    private int pot;

    private boolean sidepotted;

    private LinkedList<RoundOfBetting> roundHistory;

    public Round(LinkedList<Player> players, int blindIndex){
        this.players = players;
        this.bet = 0;
        this.pot = 0;
        this.deck = new Deck();
        this.sidepotted = false;
        this.stillInCount = players.size();
        this.finished = false;
        this.roundHistory = new LinkedList<>();
        this.flop = new LinkedList<>();
        this.blindIndex = blindIndex;
    }
    public void doRound() {
        Player smallBlind = players.get(blindIndex);
        for (Player p: players){
            p.setC1(deck.draw());
            p.setC2(deck.draw());
        }
        Player bigBlind = players.get((blindIndex + 1) % players.size());
        RoundOfBetting preFlop = new RoundOfBetting(players, 0, null, 0, stillInCount);
        preFlop.commenceRound(this);
        this.update(preFlop);
        if (!finished) {
            flop.add(deck.draw());
            flop.add(deck.draw());
            flop.add(deck.draw());
            RoundOfBetting postFlop = new RoundOfBetting(players, bet, flop, pot, stillInCount);
            postFlop.commenceRound(this);
            this.update(postFlop);
        }
        if (!finished) {
            flop.add(deck.draw());
            RoundOfBetting postFlop2 = new RoundOfBetting(players, bet, flop, pot, stillInCount);
            postFlop2.commenceRound(this);
            this.update(postFlop2);
        }
        if (!finished) {
            flop.add(deck.draw());
            RoundOfBetting finalRound = new RoundOfBetting(players, bet, flop, pot, stillInCount);
            finalRound.commenceRound(this);
            this.update(finalRound);
        }
        if (!finished) {
            LinkedList<Player> remainingPlayers = new LinkedList<>();

            for (Player p : players) {
                if (!p.isFolded()) {
                    remainingPlayers.add(p);
                }
            }
            LinkedList<TotalHand> finalHands = new LinkedList<>();
            for (Player p : remainingPlayers) {
                LinkedList<Card> cs = new LinkedList<>();
                cs.add(p.getC1());
                cs.add(p.getC2());
                cs.addAll(flop);
                finalHands.add(new TotalHand(cs, p));
            }
            for (TotalHand h : finalHands) h.evaluateHand();
            finalHands.sort(new HandComparator());
            TotalHand winningHand = finalHands.getFirst();
            winners = new LinkedList<>();
            HandComparator c = new HandComparator();
            for (TotalHand h : finalHands) {
                if (c.compare(winningHand, h) == 0) winners.add(h.getPlayer());
            }
        }
        this.finished = true;
        int winnings = pot / winners.size();
        for (Player w : winners) w.giveMoney(winnings);
    }

    private void update(RoundOfBetting r){
        this.pot = r.getPot();
        this.bet = r.getBet();
        roundHistory.add(r);
        this.stillInCount = r.getInCount();
    }

    public LinkedList<Player> getWinners() {
        return winners;
    }

    public void setWinners(LinkedList<Player> winners) {
        this.winners = winners;
    }

    public int getPot() {
        return pot;
    }
}
