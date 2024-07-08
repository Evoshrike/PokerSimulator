import java.util.Arrays;
import java.util.LinkedList;

public class TotalHand {

    private Player player;
    private int handIndex;

    private int groupIndex1;
    private int groupIndex2;
    private final LinkedList<Card> cards;

    private int TopCardIndex;

    private int tc2;

    private int tc3;


    public TotalHand(LinkedList<Card> cards, Player p) {
        this.cards = cards;
        this.player = p;
    }


    public int[] countValues() {
        int[] values = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (Card c : cards) {
            int index = c.getCardValue() - 2;
            values[index]++;
        }
        return values;
    }
    // Copied implementation but with argument to pass the cards (for flush check)
    public int[] countValues(LinkedList<Card> cardsList) {
        int[] values = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (Card c : cardsList) {
            int index = c.getCardValue() - 2;
            values[index]++;
        }
        return values;
    }

    public int pairCheck(int not) {
        int[] values = countValues();
        for (int i = 14; i > 1; i--) {
            if (values[i - 2] > 1 && i != not) return i;

        }
        return 0;
    }

    public Player getPlayer() {
        return player;
    }

    public int[] twoPairCheck() {
        int[] pairs = new int[]{0, 0};
        int[] values = countValues();

        for (int i = 14; i > 1; i--) {
            if (values[i - 2] > 1) {
                if (pairs[0] == 0){
                    pairs[0] = i;
                } else {
                    pairs[1] = i;
                    return pairs;
                    }
                }
            }
        return new int[]{0, 0};
        }

    public int tripCheck() {
        int[] values = countValues();
        for (int i = 14; i > 1; i--) {
            if (values[i - 2] > 2) {
                return i;
            }
        }
        return 0;
    }
    public int quadCheck(){
        int[] values = countValues();
        for (int i = 14; i > 1; i--) {
            if (values[i - 2] > 3) {
                return i;
            }
        }
        return 0;
    }
    public int[] fullHouseCheck(){
        int trip = tripCheck();
        int pair = pairCheck(trip);
        if (trip != 0 && pair != 0){
            return new int[]{trip, pair};
        }
        else return new int[]{0, 0};
    }
    public int straightCheck(){
        // n keeps count of the number of consecutive cards. Resets if it reaches a gap with n < 5.
        // top keeps count of the top value of the sequence of consecutive cards being evaluated.
        int[] values = countValues();
        int n = 0;
        int top = 0;
        for (int i=14; i > 1; i--){
            if (values [i-2] > 0 ){
                n++;
                if (n == 1) top = i;
                if (n > 4){
                    return top;

                }
            } else if (values[i - 2] == 0){
                n = 0;
            }
        }
        // Special case for Ace-2-3-4-5 straight
        if (n == 4 && values[12] > 0) return 5;
        return 0;
    }



    public int[] suitCount(){
        int ind = 0;
        int[] suits = new int[]{0, 0, 0, 0};
        for (Card c: cards){
            ind = c.getSuitIndex();
            suits[ind] ++;
        }
        return suits;
    }
    public LinkedList<Card> flushCheck(){
        LinkedList<Card> flushHand = new LinkedList<>();
        int suitIndex = 0;
        int[] suits = suitCount();
        for (int i=0; i<4; i++){
            if (suits[i] >= 5){
                suitIndex = i;
            }
        }
        if (suitIndex == 0) return new LinkedList<>();
        else {
            for (Card c: cards){
                if (c.getSuitIndex() == suitIndex){
                    flushHand.add(c);
                }
            }
        }
        return flushHand;

    }
    public int StraightFlushCheck(){
        LinkedList<Card> flushCards = flushCheck();
        // n keeps count of the number of consecutive cards. Resets if it reaches a gap with n < 5.
        // top keeps count of the top value of the sequence of consecutive cards being evaluated.
        int[] values = countValues(flushCards);
        int n = 0;
        int top = 0;
        for (int i=14; i > 1; i--){
            if (values [i-2] > 0 ){
                n++;
                if (n == 1) top = i;
                if (n > 4){
                    return top;

                }
            } else if (values[i - 2] == 0){
                n = 0;
            }
        }
        // Special case for Ace-2-3-4-5 straight
        if (n == 4 && values[12] > 0) return 5;
        return 0;
    }
    public void evaluateHand(){
        int straightFlushIndex = StraightFlushCheck();
        int quadIndex = quadCheck();
        int[] fullHouse = fullHouseCheck();
        LinkedList<Card> flushCards = flushCheck();
        int straightIndex = straightCheck();
        int tripIndex = tripCheck();
        int[] twoPairs = twoPairCheck();
        int pair = pairCheck(0);
        if (straightFlushIndex > 0) {
            handIndex = 14;
            TopCardIndex = straightFlushIndex;
        } else if (quadIndex > 0){
            handIndex = 13;
            groupIndex1 = quadIndex;
            TopCardIndex = 0;
            for (Card c: cards){
                if (c.getCardValue() != quadIndex && c.getCardValue() > TopCardIndex){
                    TopCardIndex = c.getCardValue();
                }
            }

        } else if (fullHouse[0] != 0){
            handIndex = 12;
            groupIndex1 = fullHouse[0];
            groupIndex2 = fullHouse[1];
        }
        else if (!flushCards.isEmpty()){
            handIndex = 11;

        }
        else if (straightIndex != 0){
            handIndex = 10;
            TopCardIndex = straightIndex;
        }
        else if (tripIndex > 0){
            handIndex = 9;
            groupIndex1 = tripIndex;
            for (Card c: cards) {
                if (c.getCardValue() != tripIndex && c.getCardValue() > TopCardIndex) {
                    TopCardIndex = c.getCardValue();
                }
            }
            for (Card c: cards) {
                if (c.getCardValue() != tripIndex && c.getCardValue() > tc2 && c.getCardValue() != TopCardIndex) {
                    tc2 = c.getCardValue();
                }
            }
        }
        else if (twoPairs[0] > 0){
            handIndex = 8;
            groupIndex1 = twoPairs[0];
            groupIndex2 = twoPairs[1];
            for (Card c: cards){
                int v = c.getCardValue();
                if (v != groupIndex1 && v != groupIndex2 && v > TopCardIndex) TopCardIndex = v;
            }
        }
        else if (pair > 0){
            handIndex = 7;
            groupIndex1 = pair;
            for (Card c: cards){
                int v = c.getCardValue();
                if (v != groupIndex1 && v > TopCardIndex) TopCardIndex = v;
                }
            for (Card c: cards){
                int v = c.getCardValue();
                if (v != groupIndex1 && v != TopCardIndex && v > tc2) tc2 = v;
            }
            for (Card c: cards){
                int v = c.getCardValue();
                if (v != groupIndex1 && v != TopCardIndex && v != tc2 && v > tc3) tc3 = v;
            }

        }
    }
    public static int highCardVs(LinkedList<Card> cards_1, LinkedList<Card> cards_2){
        int[] cards1 = new int[7];
        int[] cards2 = new int[7];
        for(int i=0; i<cards_1.size(); i++){
            cards1[i] = cards_1.get(i).getCardValue();
            cards2[i] = cards_2.get(i).getCardValue();
        }
        Arrays.sort(cards1);
        Arrays.sort(cards2);
        for (int a=6; a> 1; a--){
            if (cards1[a] != cards2[a]) return Integer.compare(cards1[a], cards2[a]);
        }
        return 0;

    }
    public int compareTo(TotalHand h){
        h.evaluateHand();
        this.evaluateHand();
        if (h.handIndex != handIndex) return Integer.compare(handIndex, h.handIndex);
        else if (h.groupIndex1 != groupIndex1) return Integer.compare(groupIndex1, h.groupIndex1);
        else if (h.groupIndex2 != groupIndex2) return Integer.compare(groupIndex2, h.groupIndex2);
        else if (h.TopCardIndex != TopCardIndex) return Integer.compare(TopCardIndex, h.TopCardIndex);
        else if (handIndex == 10){
            LinkedList<Card> cards1 = flushCheck();
            LinkedList<Card> cards2 = h.flushCheck();
            return highCardVs(cards1, cards2);
        }
        else return highCardVs(cards, h.cards);
    }
    public int getHandIndex() {
        return this.handIndex;
    }

    public int getGroupIndex1() {
        return groupIndex1;
    }

    public int getTopCardIndex() {
        return TopCardIndex;
    }

    public int getGroupIndex2() {
        return groupIndex2;
    }

    public LinkedList<Card> getCards() {
        return cards;
    }
    private int[] sortedVals(LinkedList<Card> cs){
        int size = cs.size();
        int[] result = new int[size];
        for (int i=0; i<size; i++){
            result[i] = cs.get(i).getCardValue();
        }
        Arrays.sort(result);
        return result;
    }

    public int getTc3() {
        return tc3;
    }

    public int getTc2() {
        return tc2;
    }
}





































