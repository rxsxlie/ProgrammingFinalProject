package ss.Scrabble.Server;

import ss.Scrabble.Protocol;
import ss.Scrabble.Game.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    private Socket socket;
    private GameServer server;
    private BufferedWriter writer;
    private BufferedReader reader;
    private String name;
    private boolean expectingGame = false;


    public ClientHandler(Socket s, GameServer server) {
//        Socket is with the client
        this.socket = s;
        this.server = server;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToClient(String m) {
        try {
            writer.write(m);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
        }
    }

//    Listen to the client and handle messages as they come in
    @Override
    public void run() {
        while(true) {
            if(!this.socket.isConnected()){
                this.server.disconnectPlayer(this);
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            String message = null;
            try {
                message = reader.readLine();
            } catch (IOException e) {
                this.server.disconnectPlayer(this);
                try {
                    this.socket.close();
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (message != null) {
                handleMessage(message);
            }
        }
    }
    public String getName(){
        return this.name;
    }

    public void signalClose() {
        sendToClient(Protocol.error(Protocol.Error.E001));
    }

    public void startGameMessage(ArrayList<String> handlers) {

        sendToClient(Protocol.startGame(handlers));
    }

    private void handleMessage(String m) {
        String command = Protocol.parseCommand(m);
//        this.server.print(m);

        switch (command) {
            case "ANNOUNCE":
                this.name = m.split(" ")[1];
                this.server.print("New client: " + name);
                sendToClient(Protocol.welcome(name));
                break;

            case "REQUESTGAME":
//                TODO: parse game player count if we ever do that
                this.expectingGame = true;
                this.server.gameExpected();

                sendToClient(Protocol.informQueue(this.server.getAmountOfWaitingPlayers(), 2));
                break;

            case "MAKEMOVE":
                String move = Protocol.getRest(m);
                Protocol.Error e = this.server.makeMove(this.getName(), move);
//                TODO; handle invalid move
                break;

            case "ERROR":
                handleError(m);
                break;
        }
    }

    private void handleError(String message) {
    }

    public boolean isExpectingGame() {
        return expectingGame;
    }

    public void setExpectingGame(boolean expectingGame) {
        this.expectingGame = expectingGame;
    }

    public void newRack(Rack playerRack) {
        sendToClient(Protocol.newTiles(playerRack.getLetters()));
    }

    public void notifyTurn(String name, ArrayList<String> names) {
        ArrayList<String> localNames = names;
        localNames.remove(name);
        String otherName = localNames.get(0);
        sendToClient(Protocol.notifyTurn(otherName, 0));
        sendToClient(Protocol.notifyTurn(name, 1));


    }

    public void informMove(String name, String move) {
        sendToClient(Protocol.infomMove(name, move));
    }

    public void newTiles(String tiles) {
        sendToClient(Protocol.newTiles(tiles));
    }


}
