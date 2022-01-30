package ss.Scrabble;

import java.io.IOException;

public class Scrabble {

    public static void main(String[] args) throws IOException {
        Player[] players = new Player[args.length];
        for(int i = 0; i < args.length; i++){
            players[i] = new Player(args[i]);
        }
        Game game = new Game(players);
        game.play();
    }
}
