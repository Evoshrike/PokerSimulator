public class Choice {
    private final char action;

    private final int raise;

    public Choice(char a, int amt){
        this.action = a;
        this.raise = amt;
    }
    public Choice(char a){
        this.action = a;
        this.raise = 0;
    }
    public char getChoice(){
        return this.action;

    }
    public int getRaise(){
        return this.raise;
    }
}

