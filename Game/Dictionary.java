package ss.Scrabble.Game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Dictionary {
    public Set<String> dictionary;

    public Dictionary() {

    }

    public Set<String> getDictionary() {
        Set<String> result = new HashSet<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("src/ss/Scrabble/dictionary.txt"));
            String word = "";
            while((word = reader.readLine()) != null){
                result.add(word);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
