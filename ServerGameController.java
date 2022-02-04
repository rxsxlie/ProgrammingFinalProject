package ss.Scrabble;

import java.util.ArrayList;

public class ServerGameController implements Runnable {

//    This will own one game and operate it.

    private Game game;
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
            System.exit(0);
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

    private void playTurn(ClientHandler player) {
        notifyTurns(player.getName());
        waitOnPlayer();

//        Let the player know it is his turn
//        Wait for a response
//        PLay that move
    }

    private void gameOver() {
//        Let both the players know the game is over
    }

    private void startGame() {

        this.game = new Game(clientHandlers);
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

    public boolean makeMove(String name, String m) {

        String semiCommand = m.split(Protocol.UNIT_SEPARATOR+"")[0];
        if (semiCommand.equals("SWAP")) {
            boolean correct = this.game.swap(name, Protocol.getRest(m));
            return correct;
        }
        if (semiCommand.equals("SKIP")) {
            this.game.skip(name);
        }
        if (this.game.validMove(m)) {
            this.game.makeMove(name, m);
            played = true;
            informMove(name, m);
        }

        return this.game.validMove(m);

    }

    private void informMove(String name, String move) {
        for (ClientHandler c: clientHandlers) {
            c.informMove(name, move);
        }
    }
}
