package pattni.sahil.wordle;

import me.tongfei.progressbar.ProgressBar;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pattni.sahil.automator.MockWordleAgent;
import pattni.sahil.data.Dataset;

import java.util.Map;
import java.util.TreeMap;

public class WordleAgentTest {
    private MockWordleAgent agent;
    private static Dataset dataset;

    @BeforeAll
    static void setUpBeforeClass() {
        dataset = new Dataset("lib/unigram_freq.csv");
    }

    @Test
    void test() {
        // Number of words to test
        int N_TESTS = 100;
        // Proportion of correct words to guess to pass test
        double THRESHOLD = 0.7;



        Map<Integer, Integer> guesses = new TreeMap<>();

        try (ProgressBar pb = new ProgressBar("Guessing", N_TESTS)) {
            for (int i = 0; i < N_TESTS; i++) {
                agent = new MockWordleAgent();

                // Set correct word to random word
                String correctWord = dataset.randomWord();
                agent.setCorrectWord(correctWord);

                Solver solver = new Solver("soare", "lib/unigram_freq.csv", agent);
                // Store the number of guesses it took to solve the word
                int steps = solver.solve();
                guesses.put(steps, guesses.getOrDefault(steps, 0) + 1);

                pb.step();
            }
        }

        // Counts
        for (Map.Entry<Integer, Integer> entry : guesses.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        assert (double) (N_TESTS - guesses.get(-1)/N_TESTS) >= THRESHOLD;
    }
}
