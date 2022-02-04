package ss.Scrabble;

import java.util.ArrayList;
import java.util.List;

public class Protocol {

//    TODO: add these to the returns
//    private static final char MESSAGE_SEPARATOR = '\u001e';
    public static final char UNIT_SEPARATOR = ' ';
    public static final char MESSAGE_SEPARATOR = ' ';



    public static String announce(String name) {
        return "ANNOUNCE " + name;
    }

    public static String welcome(String name) {
        return "WELCOME " + name;
    }

    public static String requestGame(int numPlayers) {
        return "REQUESTGAME " + numPlayers;
    }

    public static String informQueue(int inQueue, int numPlayers) {
        return "INFORMQUEUE " + inQueue +  " " +numPlayers;
    }

    public static String startGame(ArrayList<String> cs) {
        String nms = "";
        for(String n : cs){
            nms += n + MESSAGE_SEPARATOR;
        }
        return "STARTGAME " + nms;
    }

    public static String notifyTurn(String name, int turn) {
        return "NOTIFYTURN " + turn + " " + name;
    }

    public static String makeMoveWord(String pos, String orientation, String word) {
        return "MAKEMOVE WORD " + pos + " " + orientation + " " + word;
    }

    public static String makeMoveSwap(String letters) {
        return "MAKEMOVE SWAP " + letters;
    }

    public static String skip() {
        return "MAKEMOVE" + UNIT_SEPARATOR + "SKIP";
    }

    public static String newTiles(String letters) {
        return "NEWTILES " + letters;
    }

    public static String infomMove(String name, String move) {
        return "INFORMMOVE " + name + " " + move + MESSAGE_SEPARATOR;
    }

    public static String gameOver(String n1, String n2, String s1, String s2, String cause) {
        return "INFORMMOVE" + UNIT_SEPARATOR + cause + UNIT_SEPARATOR + n1 + UNIT_SEPARATOR + s1 + UNIT_SEPARATOR + n2 + UNIT_SEPARATOR + s2;
    }


    public static String error(Error e) {
//        TODO: make this with the protocol
        return "ERROR";
    }

    public static String parseCommand(String m) {
        return m.split(" ")[0];
    }

    public static String[] parseAll(String m) {
        return m.split(MESSAGE_SEPARATOR+"");
    }

    public static String getRest(String m) {
        String[] ms = m.split(" ");
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < ms.length; i++) {
            if (i == 0) {
                continue;
            }
            res.append(ms[i]).append(" ");
        }
        return res.toString();
    }




    public enum Error {
        E001("Name already taken"),
        E002("Unknown command"),
        E003("Invalid argument"),
        E004("Invalid coordinate"),
        E005("Word does not fit on board"),
        E006("Unknown word"),
        E007("Too few letters left in tilebag to swap"),
        E008("You do not have the required tiles"),
        E009("It is not your turn"),
        E010("Invalid request: wrong number of players | X"),
        E011("Word not connected to other tiles"),
        E012("Client has already announced themselves"),
        E013("Client has not yet announced themselves"),
        E014("Client is not in a game"),
        E015("Game already started"),
//        For internal use only
        NoError("No error");

        private final String description;

        Error(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }
    }

}
