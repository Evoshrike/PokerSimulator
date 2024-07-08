import java.util.HashMap;

public class Card {
    private final char number;
    private final char suit;

    public static final char[] suitList = new char[]{'d', 'h', 's', 'c'};
    // Note X means 10 for numbers
    public static final char[] numList = new char[]{'A', '2', '3', '4', '5', '6', '7', '8', '9', 'X', 'J', 'K', 'Q'};
    private static final HashMap<String, Integer> cardValues = new HashMap<>() {{
        put("A", 14);
        put("2", 2);
        put("3", 3);
        put("4", 4);
        put("5", 5);
        put("6", 6);
        put("7", 7);
        put("8", 8);
        put("9", 9);
        put("X", 10);
        put("J", 11);
        put("Q", 12);
        put("K", 13);
    }};
    public Card(char num, char suit) {
        if (Utils.testCharMembership(numList, num)) {
            this.number = num;
        } else throw new IllegalArgumentException();
        if (Utils.testCharMembership(suitList, suit)) {
            this.suit = suit;
        } else throw new IllegalArgumentException();
    }
    public int getNumber() {
        return number;
    }

    // Getter for suit
    public char getSuit() {
        return suit;
    }
    public int Compare(Card c) {
        int c_value = cardValues.get(String.valueOf(c.getSuit()));
        int this_value = cardValues.get(String.valueOf(this.suit));
        return Integer.compare(this_value, c_value);

    }
    // This is from ChatGPT, and is ugly.
    @Override
    public String toString() {
        String cardNumber = "";
        String cardSuit = "";

        // Determine card number description
        cardNumber = switch (number) {
            case 'A' -> "Ace";
            case '2' -> "Two";
            case '3' -> "Three";
            case '4' -> "Four";
            case '5' -> "Five";
            case '6' -> "Six";
            case '7' -> "Seven";
            case '8' -> "Eight";
            case '9' -> "Nine";
            case 'X' -> "Ten";
            case 'J' -> "Jack";
            case 'Q' -> "Queen";
            case 'K' -> "King";
            default ->
                // Handle unexpected case
                    "Unknown";
        };

        // Determine card suit description
        cardSuit = switch (suit) {
            case 'd' -> "Diamonds";
            case 'h' -> "Hearts";
            case 's' -> "Spades";
            case 'c' -> "Clubs";
            default ->
                // Handle unexpected case
                    "Unknown";
        };

        return cardNumber + " of " + cardSuit;
    }
    public int getCardValue(){
        return cardValues.get(String.valueOf(number));
    }
    public int getSuitIndex(){
        return switch (getSuit()) {
            case 'd' -> 0;
            case 'h' -> 1;
            case 'c' -> 2;
            case 's' -> 3;


            default -> throw new IllegalStateException("Unexpected value: " + getSuit());
        };
    }


}
