package ss.Scrabble.Client;
import ss.Scrabble.Protocol;

import java.io.*;
import java.net.Socket;


public class Client implements Runnable{

    BufferedReader userReader = new BufferedReader(new InputStreamReader(System.in));
    private String name;
    private Socket socket;
    private BufferedReader serverReader;
    private BufferedWriter serverWriter;
    private boolean ourTurn = false;
    private ClientGameController gameController;

    @Override
    public void run() {
        try {
            this.serverWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.serverReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true) {
            String message = null;
            try {
                message = serverReader.readLine();
            } catch (IOException e) {
                System.out.println("The server is dead");
                System.exit(1);
            }
            if (message != null) {
                handleServerMessage(message);
            }
        }
    }

    private void handleServerMessage(String message) {
        String command = Protocol.parseCommand(message);
        String[] all = Protocol.parseAll(message);
//        System.out.println(message);

        switch (command) {
            case "WELCOME":
                System.out.println("Server welcomed us!");
                break;

            case "INFORMQUEUE":
                System.out.println("There are " + all[1] + " players waiting in the server.");
                break;

            case "STARTGAME":

                this.gameController = new ClientGameController(all[1], all[2], name);
                break;

            case "NEWTILES":
                this.gameController.tileUpdate(all[1]);
                break;

            case "NOTIFYTURN":

                if (this.name.equals(all[2])) {
                    this.ourTurn = "1".equals(all[1]);
                    handleTurn();
                }
                break;

            case "INFORMMOVE":
                if (all[1].equals("DISCONNECT")) {
                    System.out.println("The other player disconnected, the client will now shut down");
                    System.out.println("The scores are  " + all[2]+": " + all[3]+"  " + all[4]+": " + all[5]);
                    System.exit(0);
                    break;
                }
                if (all[1].equals("WIN")) {
                    System.out.println("The game has a winner!");
                    System.out.println("The scores are  " + all[2]+": " + all[3]+"  " + all[4]+": " + all[5]);
                    System.exit(0);
                    break;

                }
                if (all[2].equals("SWAP")) {
                    this.gameController.remove(all[3]);
                    System.out.println("Swapped " + all[2]);
                } else if (all[2].equals("SKIP")) {
                    System.out.println(all[1] + " skipped playing!");
                } else {
                    System.out.println("Move was made by " + all[1] + " who played: "+ all[5]);
                    this.gameController.makeMove(all[3] +" "+ all[4] +" "+ all[5]);
                    System.out.println(this.gameController.printBoard());
                    System.out.println(this.gameController.printRack());
                }
        };

    }

    private void handleTurn() {
        if (this.ourTurn) {
            String move = getUserInput("Please enter a move, options: [to play example -> H8 H AWORD, to skip -> SKIP, to swap -> SWAP {letters to swap}]");
            String[] s = move.split(" ");
            if (s[0].equals("SWAP")) {
                sendMessageToServer(Protocol.makeMoveSwap(s[1]));
            } else if (s[0].equals("SKIP")) {
                sendMessageToServer(Protocol.skip());

            }
            else {
                if (!this.gameController.checkValidMove(s[0] + " " + s[1] + " " + s[2])){
                    System.err.println("Wrong move");
                    handleTurn();
                }
                sendMessageToServer(Protocol.makeMoveWord(s[0], s[1], s[2]));
            }
        }
        this.ourTurn = false;
    }

    private String getUserInput(String m){
        System.out.println(m);
        try {
            return userReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void userLoop() {
        sendMessageToServer(Protocol.announce(this.name));
//        We should get a game
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String wannaPlay = getUserInput("Do you want to play a game? If so, type yes: ");
        assert wannaPlay != null;
        if (wannaPlay.equals("yes")) {
            sendMessageToServer(Protocol.requestGame(2));

        } else {
            System.out.println("Oke, bye :(");
            System.exit(0);
        }


    }



    public String getName() {
        return name;
    }

    private void sendMessageToServer(String m) {
        try {
            this.serverWriter.write(m);
            this.serverWriter.newLine();
            this.serverWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        this.name = name;
    }



    private String getIP() {
        return getUserInput("Please tell me the IP of the server:");
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    //    TODO handle the IO execption
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        System.out.println("Client started");
        String name = client.getUserInput("Please tell me your name:");
        client.setName(name);
        System.out.println("Connecting to server...");
        String ip = client.getIP();
        int port = 8080;
        Socket s = new Socket(ip, port);
        client.setSocket(s);
        System.out.println("Connected");
        Thread t = new Thread(client);
        t.start();
        client.userLoop();
    }
}
