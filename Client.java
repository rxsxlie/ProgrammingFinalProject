package ss.Scrabble;
import java.io.*;
import java.net.Socket;

public class Client implements Runnable{

    BufferedReader userReader = new BufferedReader(new InputStreamReader(System.in));
    private String name;
    private Socket socket;
    private BufferedReader serverReader;
    private BufferedWriter serverWriter;

    @Override
    public void run() {
        while(true) {
            String message = null;
            try {
                message = userReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (message != null) {
                handleServerMessage(message);
            }
        }
    }

    private void handleServerMessage(String message) {
//        TODO
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
        makeObjects();
        sendMessageToServer(Protocol.announce(this.name));


    }

    private void makeObjects() {
        try {
            this.serverWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.serverReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
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

    private String getIP() {
        return getUserInput("Please tell me the IP of the server:");
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
