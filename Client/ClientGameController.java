package ss.Scrabble.Client;


import ss.Scrabble.Protocol;

import java.util.ArrayList;

public class ClientGameController {

    private ClientGame game;
    private String name;


    public ClientGameController(String player1, String player2, String name) {
        this.name = name;
        ArrayList<String> a = new ArrayList<>();
        a.add(player1);
        a.add(player2);
        game = new ClientGame(a);
        game.start();
    }

    public void tileUpdate(String letters) {
        this.game.updateRack(name, letters);
    }

    public boolean checkValidMove(String move) {
        Protocol.Error e = this.game.validMove(name, move);
        return e.equals(Protocol.Error.NoError);
    }

    public void makeMove(String move) {
        this.game.setMove(move);
    }






//    TODO

//    Incoming names
//    Incoming rack changes
//    Incoming moves
//    Print board
//    Print rack

}
