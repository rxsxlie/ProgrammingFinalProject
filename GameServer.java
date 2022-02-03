package ss.Scrabble;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GameServer implements Runnable{
    private ArrayList<ClientHandler> clientHandlers;
    private Game game;
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
                System.out.println("Accepted a client");
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
//            TODO: announce to the rest

    }
}
