package ss.Scrabble;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Dictionary {
    public Set<String> dictionary;

    public Dictionary() throws IOException {
        this.dictionary = getDictionary();
    }

    public Set<String> getDictionary() throws IOException {
        Set<String> result = new HashSet<String>();
        BufferedReader reader = new BufferedReader(new FileReader("ss/Scrabble/dictionary.txt"));
        String word = "";
        while((word = reader.readLine()) != null){
            result.add(word);
        }
        return result;
    }
}
