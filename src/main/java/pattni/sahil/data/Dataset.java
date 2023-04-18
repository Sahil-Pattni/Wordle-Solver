package pattni.sahil.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dataset {
    /*
     * The dataset of words and their frequencies.
     */
    List<WordEntry> wordFrequencyList;

    public Dataset(String filepath) {
        wordFrequencyList = new ArrayList<>();
        populateDataset(filepath);
    }

    private void populateDataset(String filepath) {
        /*
         * Populate the dataset from a CSV file.
         *
         * @param filepath: The path to the CSV file.
         */
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
                                values[0].strip().toLowerCase(),
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

    public List<WordEntry> getWordFrequencyList() {
        return wordFrequencyList;
    }

    public void removeWord(String word) {
        /* Remove a word from the dataset. */
        wordFrequencyList.removeIf(entry -> entry.word.equals(word));
    }

    public boolean isEmpty() {
        /* Return true if the dataset is empty. */
        return wordFrequencyList.size() == 0;
    }

    public void regexFilter(String regex) {
        /*
         * Remove all words that do not match the regex.
         * This is used to filter out words that are not in the dictionary.
         *
         * @param regex: The regex to filter the words with.
         */
        wordFrequencyList.removeIf(entry -> !entry.word.matches(regex));
    }

    public int size() {
        /* Return the number of remaining words in the dataset. */
        return wordFrequencyList.size();
    }

    public String nextWord() {
        if (isEmpty())
            throw new RuntimeException("Dataset is empty");
        return wordFrequencyList.remove(0).word;
    }
}
