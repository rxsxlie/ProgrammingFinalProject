package ss.Scrabble;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Dictionary {
    public Set<String> dictionary;

    public Dictionary(){
        this.dictionary = new HashSet<String>();
    }

    public Set<String> getDictionary() throws IOException {
        this.setDictionary();
        return this.dictionary;
    }

    public void setDictionary() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("ss/Scrabble/dictionary.txt"));
        String word = "";
        while((word = reader.readLine()) != null){
            this.dictionary.add(word);
        }
    }
}
