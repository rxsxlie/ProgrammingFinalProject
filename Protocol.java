package ss.Scrabble;

import java.util.ArrayList;
import java.util.List;

public class Protocol {

//    private static final char MESSAGE_SEPARATOR = '\u001e';
//    private static final char MESSAGE_SEPARATOR = '\u001f';
    public static final char UNIT_SEPARATOR = ' ';
    public static final char MESSAGE_SEPARATOR = ' ';



    public static String announce(String name) {
        return "ANNOUNCE" + UNIT_SEPARATOR + name + MESSAGE_SEPARATOR;
    }

    public static String welcome(String name) {
        return "WELCOME" + UNIT_SEPARATOR + name+ MESSAGE_SEPARATOR;
    }

    public static String requestGame(int numPlayers) {
        return "REQUESTGAME" + UNIT_SEPARATOR + numPlayers+ MESSAGE_SEPARATOR;
    }

    public static String informQueue(int inQueue, int numPlayers) {
        return "INFORMQUEUE"+ UNIT_SEPARATOR + inQueue +  UNIT_SEPARATOR +numPlayers+ MESSAGE_SEPARATOR;
    }

    public static String startGame(ArrayList<String> cs) {
        String nms = "";
        for(String n : cs){
            nms += n + MESSAGE_SEPARATOR;
        }
        return "STARTGAME" +UNIT_SEPARATOR+ nms+ MESSAGE_SEPARATOR;
    }

    public static String notifyTurn(String name, int turn) {
        return "NOTIFYTURN" +UNIT_SEPARATOR+ turn + UNIT_SEPARATOR + name+ MESSAGE_SEPARATOR;
    }

    public static String makeMoveWord(String pos, String orientation, String word) {
        return "MAKEMOVE WORD" +UNIT_SEPARATOR + pos + UNIT_SEPARATOR + orientation + UNIT_SEPARATOR + word+ MESSAGE_SEPARATOR;
    }

    public static String makeMoveSwap(String letters) {
        return "MAKEMOVE SWAP"+UNIT_SEPARATOR + letters+ MESSAGE_SEPARATOR;
    }

    public static String skip() {
        return "MAKEMOVE" + UNIT_SEPARATOR + "SKIP"+ MESSAGE_SEPARATOR;
    }

    public static String newTiles(String letters) {
        return "NEWTILES" +UNIT_SEPARATOR + letters+ MESSAGE_SEPARATOR;
    }

    public static String infomMove(String name, String move) {
        return "INFORMMOVE"+UNIT_SEPARATOR + name + UNIT_SEPARATOR + move + MESSAGE_SEPARATOR;
    }

    public static String gameOver(String n1, String n2, String s1, String s2, String cause) {
        return "INFORMMOVE" + UNIT_SEPARATOR + cause + UNIT_SEPARATOR + n1 + UNIT_SEPARATOR + s1 + UNIT_SEPARATOR + n2 + UNIT_SEPARATOR + s2+ MESSAGE_SEPARATOR;
    }


    public static String error(Error e) {
        return "ERROR" + UNIT_SEPARATOR + e.description+ MESSAGE_SEPARATOR;
    }

    public static String parseCommand(String m) {
        return m.split(UNIT_SEPARATOR+"")[0];
    }

    public static String[] parseAll(String m) {
        return m.split(UNIT_SEPARATOR+"");
    }

    public static String getRest(String m) {
        String[] ms = m.split(UNIT_SEPARATOR+"");
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
