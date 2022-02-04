package ss.Scrabble.Server;

import ss.Scrabble.Protocol;
import ss.Scrabble.Game.*;

import java.util.ArrayList;

public class ServerGameController implements Runnable {

//    This will own one game and operate it.

    private ServerGame game;
    private ArrayList<ClientHandler> clientHandlers;
    public final Object playedLock = new Object();
    public boolean played = false;

    public ServerGameController(ArrayList<ClientHandler> cs) {
        this.clientHandlers = cs;

    }

    private void gameLoop() {
        startGame();
        while(!this.game.isOver()) {
            for (ClientHandler player : clientHandlers) {
                playTurn(player);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        Game is over
        String message = end("WIN");
        for (ClientHandler c: clientHandlers) {
            c.sendToClient(message);
        }
    }


    private void notifyTurns(String name) {
        ArrayList<String> names = new ArrayList<>();
        for (ClientHandler c: clientHandlers) {
            names.add(c.getName());
        }
        for (ClientHandler c : this.clientHandlers) {
            c.notifyTurn(name, names);
        }
    }

    private void waitOnPlayer() {

        synchronized (playedLock) {
            while (!played) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private synchronized void playTurn(ClientHandler player) {
        notifyTurns(player.getName());
        this.played = false;
        waitOnPlayer();

//        Let the player know it is his turn
//        Wait for a response
//        PLay that move
    }

    private void gameOver() {
//        Let both the players know the game is over
    }

    private void startGame() {

        this.game = new ServerGame(clientHandlers);
        this.game.start();
        ArrayList<String> names = new ArrayList<>();
        for (ClientHandler c: clientHandlers) {
            names.add(c.getName());
        }
        for (ClientHandler c: clientHandlers) {
            c.startGameMessage(names);
        }
        for (ClientHandler c: clientHandlers) {
            Rack playerRack = this.game.getPlayerByName(c.getName()).getRack();
            c.newRack(playerRack);
        }
    }


    @Override
    public void run() {
        gameLoop();
    }

    public Protocol.Error makeMove(String name, String m) {

        for (Player player : this.game.players) {
            System.out.println(player.getRack().toString());
        }

        String semiCommand = m.split(Protocol.UNIT_SEPARATOR+"")[0];
        if (semiCommand.equals("SWAP")) {
            String letters = Protocol.parseAll(m)[1];
            String swapped = this.game.swapLetters(letters, this.game.getPlayerByName(name));
            for(ClientHandler c: clientHandlers) {
                if (c.getName().equals(name)) {
                    c.newTiles(swapped);
                }
            }
            informMove(name, m);
            played = true;
            return Protocol.Error.NoError;
        }
        if (semiCommand.equals("SKIP")) {
            informMove(name, m);
            played = true;
            return Protocol.Error.NoError;
        }

        Protocol.Error e = this.game.makeMove(name, m);
        char[] cs = this.game.getPlayerByName(name).toRemove.toCharArray();
        ArrayList<Tile> ts = new ArrayList<>();
        for (char c : cs) {
            ts.add(new Tile(c, 1));
        }
        this.game.getPlayerByName(name).getRack().removeTiles(ts);
        this.game.getPlayerByName(name).toRemove = "";
        if (e.equals(Protocol.Error.NoError)) {
            played = true;
            informMove(name, m);
            for(ClientHandler c: clientHandlers) {
                if (c.getName().equals(name)) {
                    c.newTiles(this.game.getPlayerByName(name).newLetters);
                    this.game.getPlayerByName(name).newLetters = "";
                }
            }
        }
        return e;

    }

    private void informMove(String name, String move) {
        for (ClientHandler c: clientHandlers) {
            c.informMove(name, move);
        }
    }

    public String end(String cause) {
        return Protocol.gameOver(this.game.players.get(0).getName(), this.game.players.get(1).getName(),
                this.game.players.get(0).getScore()+"", this.game.players.get(1).getScore()+"", cause);
    }
}
