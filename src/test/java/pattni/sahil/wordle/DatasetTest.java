package pattni.sahil.wordle;

import org.springframework.boot.test.context.SpringBootTest;
import pattni.sahil.data.Dataset;
import pattni.sahil.data.WordEntry;

import java.util.ArrayList;

// Test Imports
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@SpringBootTest
public class DatasetTest {
    private static Dataset dataset;
    @BeforeAll
    public static void setup() {
        dataset = new Dataset("lib/unigram_freq.csv");
    }

    @Test
    public void testDatasetImport() {
        ArrayList<WordEntry> a = dataset.getWordFrequencyList();
        assertEquals(39933, a.size());
    }

    @Test
    public void testSort() {
        ArrayList<WordEntry> a = dataset.getWordFrequencyList();

        int prev = Integer.MAX_VALUE;
        for (WordEntry entry : a) {
            int freq = entry.getFrequency();
            assert(freq <= prev);
            prev = freq;
        }
    }

    @Test
    public void testRemove() {
        dataset.removeWord("money");
        ArrayList<WordEntry> a = dataset.getWordFrequencyList();
        assertEquals(39932, a.size());

        for (WordEntry entry : a) {
            assert(!entry.getWord().equals("money"));
        }
    }
}
