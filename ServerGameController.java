package ss.Scrabble;

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
            System.out.println(this.game.board.toString());
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

        String semiCommand = m.split(Protocol.UNIT_SEPARATOR+"")[0];
        if (semiCommand.equals("SWAP")) {
            boolean correct = this.game.swap(name, Protocol.getRest(m));
//            
            return null;
        }
        if (semiCommand.equals("SKIP")) {
            this.game.skip(name);
        }

        Protocol.Error e = this.game.makeMove(name, m);
        if (e.equals(Protocol.Error.NoError)) {
            played = true;
            informMove(name, m);
            for(ClientHandler c: clientHandlers) {
                if (c.getName().equals(name)) {
                    c.newTiles(this.game.getPlayerByName(name).newLetters);
                }
            }
        } else {
//            TODO: handle errors here
        }
        return e;

    }

    private void informMove(String name, String move) {
        for (ClientHandler c: clientHandlers) {
            c.informMove(name, move);
        }
    }
}
