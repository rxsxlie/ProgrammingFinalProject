package ss.Scrabble.Server;

import ss.Scrabble.Protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//TODO: go through the list of erros and handle them

public class GameServer implements Runnable{
    private ArrayList<ClientHandler> clientHandlers;
    private ServerGameController gameController;
    private ServerSocket serverSocket;
    private final int port = 8080;

    public GameServer() {
        this.clientHandlers = new ArrayList<>();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print(String m) {
        System.out.println(m);
    }

    public void main() {
//        TODO maybe
    }

    @Override
    public void run() {
        while(true) {
            try {
//            This blocks
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, this);
                new Thread(handler).start();
                clientHandlers.add(handler);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void exit() {
        for(ClientHandler h : clientHandlers)
            h.signalClose();
    }
    public static void main(String[] args) {
        GameServer server = new GameServer();
        Thread t = new Thread(server);
        t.start();
        server.main();
//        TODO: graceful exit
    }

    public void disconnectPlayer(ClientHandler clientHandler) {
        synchronized (clientHandlers) {
            clientHandlers.remove(clientHandler);
        }
        System.out.println("Removed a client");
        String message = this.gameController.end("DISCONNECT");
        for (ClientHandler c: clientHandlers) {
            c.sendToClient(message);
        }
        clientHandlers = new ArrayList<>();

    }
//  There is at least one client waiting
    public synchronized void gameExpected() {

        ArrayList<ClientHandler> queue = new ArrayList<>();
        for (ClientHandler clientHandler: clientHandlers){
            if (clientHandler.isExpectingGame()){
                queue.add(clientHandler);
            }
        }
        if(queue.size() == 2) {
            this.startGame(queue);
        }

    }
    public synchronized int getAmountOfWaitingPlayers() {
        ArrayList<ClientHandler> queue = new ArrayList<>();
        for (ClientHandler clientHandler: clientHandlers){
            if (clientHandler.isExpectingGame()){
                queue.add(clientHandler);
            }
        }
        return queue.size();
    }
    private synchronized void  startGame(ArrayList<ClientHandler> queue) {

        for (ClientHandler clientHandler: clientHandlers){
            clientHandler.setExpectingGame(false);
        }
        this.gameController = new ServerGameController(queue);
        Thread gameThread = new Thread(this.gameController);
        gameThread.start();
    }

    public Protocol.Error makeMove(String name, String m) {

        return this.gameController.makeMove(name, m);
    }
}
