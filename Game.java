package ss.Scrabble;

public class Game {
    Board board;
    Player playerOne;
    Player playerTwo;

    public Game(Player playerOne, Player playerTwo){
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        board = new Board();
    }

    public void start(){
        board.setDefaultPremiumFields();
    }
}
