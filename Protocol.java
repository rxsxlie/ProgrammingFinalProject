package ss.Scrabble;

public class Protocol {

//    TODO: add these to the returns
    private static final char MESSAGE_SEPARATOR = '\u001e';
    private static final char UNIT_SEPARATOR = '\u001f';


    public static String announce(String name) {
        return "ANNOUNCE " + name;
    }

    public static String welcome(String name) {
        return "WELCOME " + name;
    }

    public static String requestGame(int numPlayers) {
        return "REQUESTGAME " + numPlayers;
    }

    public static String inforQueue(int inQueue, int numPlayers) {
        return "INFORMQUEUE " + inQueue +  " " +numPlayers;
    }

    public static String startGame(String... names) {
        String nms = "";
        for(String n : names){
            nms += n;
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

    public static String newTiles(String letters) {
        return "NEWLETTER " + letters;
    }

    public static String infomMoveWord(String name, String pos, String word) {
        return "INFORMMOVE " + name + " " + "WORD" + " " + pos + " " + word;
    }

    public static String infomMoveSwap(String name, String word) {
        return "INFORMMOVE " + name + " SWAP " + word.length();
    }

    public static String gameOver(String[] names, int[] scores, String cause) {
        return "INFORMMOVE " + cause + " " + names[0] + " " + scores[0] + " " + names[1] + " " + scores[1];
    }


    public static String error(Error e) {
//        TODO: make this with the protocol
        return "ERROR";
    }

    public static String parseCommand(String m) {
        return m.split(" ")[0];
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
        E015("Game already started");

        private final String description;

        Error(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }
    }

}
