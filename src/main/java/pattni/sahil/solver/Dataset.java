package pattni.sahil.solver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

// Main class to store dataset sorted by frequency
public class Dataset {
    ArrayList<WordEntry> wordFrequencyList;
    public Dataset(String filepath) {
        wordFrequencyList = new ArrayList<>();
        populateDataset(filepath);
    }

    private void populateDataset(String filepath) {
        // Read CSV and populate wordFrequencyList
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i++ == 0) continue; // Skip header
                String[] values = line.split(",");
                // Populate wordFrequency
                wordFrequencyList.add(
                        new WordEntry(
                                values[0].strip(),
                                Integer.parseInt(values[1])
                        )
                );
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Sort wordFrequencyList by frequency in descending order
        wordFrequencyList.sort((a, b) -> b.frequency - a.frequency);
    }

    public ArrayList<WordEntry> getWordFrequencyList() {
        return wordFrequencyList;
    }

    public void removeWord(String word) {
        wordFrequencyList.removeIf(entry -> entry.word.equals(word));
    }
}
