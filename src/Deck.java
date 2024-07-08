import java.util.LinkedList;
import java.util.Random;

public class Deck {
    private final LinkedList<Card> cards;

    public Deck(){
        this.cards = new LinkedList<>();
        for (char num: Card.numList){
            for (char suit: Card.suitList){
                this.cards.add(new Card(num, suit));

            }
        }

    }
    public Card draw(){
        Random random = new Random();
        int randomIndex = random.nextInt(this.cards.size());
        Card c = this.cards.get(randomIndex);
        this.cards.remove(c);
        return c;
    }
}
