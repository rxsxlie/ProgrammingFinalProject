package ss.Scrabble;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;
    private GameServer server;
    private BufferedWriter writer;
    private BufferedReader reader;
    private String name;


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

    private void write(String m) {
        try {
            writer.write(m);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    Listen to the client and handle messages as they come in
    @Override
    public void run() {
        System.out.println("Run");
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

    public void signalClose() {
        write(Protocol.error(Protocol.Error.E001));
    }

    private void handleMessage(String m) {
        String command = Protocol.parseCommand(m);

        switch (command) {
            case "ANNOUNCE":
                this.name = m.split(" ")[1];
                this.server.print("Announced with name " + this.name);
                write(Protocol.welcome(name));
                break;

        }

    }
}
