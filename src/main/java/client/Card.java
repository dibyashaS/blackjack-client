package client;

public enum Card
{
    TWO_OF_CLUBS("2C",2,false),//updated with additional parameters
    THREE_OF_CLUBS("3C",3,false),
    FOUR_OF_CLUBS("4C",4,false),
    FIVE_OF_CLUBS("5C",5,false),
    SIX_OF_CLUBS("6C",6,false),
    SEVEN_OF_CLUBS("7C",7,false),
    EIGHT_OF_CLUBS("8C",8,false),
    NINE_OF_CLUBS("9C",9,false),
    TEN_OF_CLUBS("10C",10,false),
    JACK_OF_CLUBS("JC",10,false),
    QUEEN_OF_CLUBS("QC",10,false),
    KING_OF_CLUBS("KC",10,false),
    ACE_OF_CLUBS("AC",11,true),
    TWO_OF_DIAMONDS("2D",2,false),
    THREE_OF_DIAMONDS("3D",3,false),
    FOUR_OF_DIAMONDS("4D",4,false),
    FIVE_OF_DIAMONDS("5D",5,false),
    SIX_OF_DIAMONDS("6D",6,false),
    SEVEN_OF_DIAMONDS("7D",7,false),
    EIGHT_OF_DIAMONDS("8D",8,false),
    NINE_OF_DIAMONDS("9D",9,false),
    TEN_OF_DIAMONDS("10D",10,false),
    JACK_OF_DIAMONDS("JD",10,false),
    QUEEN_OF_DIAMONDS("QD",10,false),
    KING_OF_DIAMONDS("KD",10,false),
    ACE_OF_DIAMONDS("AD",11,true),
    TWO_OF_HEARTS("2H",2,false),
    THREE_OF_HEARTS("3H",3,false),
    FOUR_OF_HEARTS("4H",4,false),
    FIVE_OF_HEARTS("5H",5,false),
    SIX_OF_HEARTS("6H",6,false),
    SEVEN_OF_HEARTS("7H",7,false),
    EIGHT_OF_HEARTS("8H",8,false),
    NINE_OF_HEARTS("9H",9,false),
    TEN_OF_HEARTS("10H",10,false),
    JACK_OF_HEARTS("JH",10,false),
    QUEEN_OF_HEARTS("QH",10,false),
    KING_OF_HEARTS("KH",10,false),
    ACE_OF_HEARTS("AH",11,true),
    TWO_OF_SPADES("2S",2,false),
    THREE_OF_SPADES("3S",3,false),
    FOUR_OF_SPADES("4S",4,false),
    FIVE_OF_SPADES("5S",5,false),
    SIX_OF_SPADES("6S",6,false),
    SEVEN_OF_SPADES("7S",7,false),
    EIGHT_OF_SPADES("8S",8,false),
    NINE_OF_SPADES("9S",9,false),
    TEN_OF_SPADES("10S",10,false),
    JACK_OF_SPADES("JS",10,false),
    QUEEN_OF_SPADES("QS",10,false),
    KING_OF_SPADES("KS",10,false),
    ACE_OF_SPADES("AS",11,true);

    private String string;
    private final int value;
    private final boolean isAce;

    Card(String card, int value, boolean isAce) {
        this.string = card;
        this.value=value;
        this.isAce=isAce;
    }

    public static Card fromString(String card) {
        card = card.toUpperCase().replace(' ', '_');
        for (Card c : Card.values()) {
            if (c.string.equals(card)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid card: " + card);
    }

    public String toString() {
        return string;
    }
    public int getValue(){
        return value;
    }

    public boolean isAce(){
        return isAce;
    }

    public String getFilename() {
        return string + ".png";
    }
}
