package pattni.sahil.data;

// Helper class to store word and its frequency
public class WordEntry {
    String word;
    int frequency;
    public WordEntry(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    // Getters
    public String getWord() {
        return word;
    }

    public int getFrequency() {
        return frequency;
    }
}
