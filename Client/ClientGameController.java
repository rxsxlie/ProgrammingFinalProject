package ss.Scrabble.Client;


import ss.Scrabble.Game.Player;
import ss.Scrabble.Game.Tile;
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
        String toRemove = this.game.setMove(move);
        remove(toRemove);
    }

    public String printBoard() {
        return this.game.board.toString();
    }

    public String printRack() {
        return this.game.getPlayerByName(name).getRack().toString();
    }

    public void remove(String s) {
        char[] ss = s.toCharArray();
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        for (char sss: ss) {
            tiles.add(new Tile(sss, 1));
        }
        Player p = this.game.getPlayerByName(name);
        p.getRack().removeTiles(tiles);
    }
}
