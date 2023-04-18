package pattni.sahil.wordle;

import pattni.sahil.automator.WordleAgent;
import pattni.sahil.data.Dataset;
import pattni.sahil.data.WordEntry;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Solver {
    /*
     * This is the main class that solves the Wordle game.
     * Holds the dataset and the Selenium agent.
     */
    private final String startWord;

    // Correctly guessed letters <index, letter>
    Map<Integer, Character> correct;
    // Letters that are not in the word
    List<Character> incorrect;
    // Letters that are in the word but in the wrong position
    // Key is the letter, value is an array of positions they are not in
    Map<Character, ArrayList<Integer>> wrongPosition;

    // Dataset
    Dataset dataset;
    // Selenium agent
    WordleAgent agent;

    public Solver(String startWord, String datasetPath) {
        this.startWord = startWord;

        // Initialize data structures
        dataset = new Dataset(datasetPath);

        // Remove start word from dataset (if exists)
        dataset.removeWord(startWord);

        // Initialize data structures
        correct = new Hashtable<>();
        incorrect = new ArrayList<>();
        wrongPosition = new Hashtable<>();

        // Initialize Selenium agent
        agent = new WordleAgent();
    }

    public Solver() {
        // Default start word is "soare"
        this("soare", "lib/unigram_freq.csv");
    }


    public boolean solve() {
        /*
         * Solve the Wordle game. Make guesses and process feedback.
         */
        // First guess
        boolean solved;
        try {
            solved = step(startWord, 1);
        } catch (IncorrectWordException e) {
            // `soare` should be accepted
            throw new RuntimeException(e);
        }
        if (solved) return true;

        // TODO: Handle `not a word` cases

        // Subsequent guesses
        for (int i = 2; i <= 6; i++) {
            try {
                solved = step(dataset.nextWord(), i);
            } catch (IncorrectWordException e) {
                // undo guess
                agent.undoGuess(i);
                // retry with next word
                i--;
            }
            if (solved) {
                agent.share();
                return true;
            }
        }
        return false;
    }

    private boolean step(String word, int row) throws IncorrectWordException {
        /*
         * Make a guess and process the feedback.
         *
         * @param word The word to guess
         * @param row The row to enter the guess in
         *
         * @return True if the word was guessed correctly, false otherwise.
         */
        // Make guess
        agent.enterGuess(word, row);

        // Check if guess is valid
        if (!agent.isAccepted(row))
            throw new IncorrectWordException(word);

        // Get feedback
        String feedback = agent.getFeedback(row);

        if (feedback.equals("11111"))
            return true;
        else {
            // Process feedback
            processFeedback(feedback, word);
            return false;
        }
    }

    private void processFeedback(String feedback, String word) {
        /*
         * Provide feedback to the user.
         * Feedback should be 5 letters long.
         * Each letter should be either:
         *   - `1` for correct
         *   - `0` for incorrect
         *   - `?` for wrong position
         *
         * @param feedback The user's feedback
         * @param word The word that was guessed
         *
         * @return The user's feedback
         */

        for (int i = 0; i < feedback.length(); i++) {
            char letter = word.charAt(i);
            switch (feedback.charAt(i)) {
                case '1' -> correct.put(i, letter);
                case '0' -> incorrect.add(letter);
                case '?' -> {
                    // Add index to list of wrong positions for that letter
                    if (wrongPosition.containsKey(letter))
                        wrongPosition.get(letter).add(i);
                    else {
                        ArrayList<Integer> positions = new ArrayList<>();
                        positions.add(i);
                        wrongPosition.put(letter, positions);
                    }
                }
            }
        }

        purgeDataset();
    }

    private void purgeDataset() {
        /*
         * Removes all words from the dataset that cannot
         * be used, based on the game's feedback.
         */

        // --- STEP 1: Remove words that don't match the correct letters --- //
        String correctRegex = buildCorrectRegex();
        dataset.regexFilter(correctRegex);

        // --- STEP 2: Remove words that contain incorrect letters --- //
        StringBuilder incorrectRegex = new StringBuilder();
        // builds: `\b[^xyz\s\d]{5}\b` where x,y,z,etc. are the incorrect letters
        incorrectRegex.append("\\b[^");
        for (char letter : incorrect) {
            // For letters that are not in the incorrect list
            if (!wrongPosition.containsKey(letter))
                incorrectRegex.append(letter);
        }
        incorrectRegex.append("]{5}\\b");
        dataset.regexFilter(incorrectRegex.toString());

        // --- STEP 3: Remove words that contain letters in the wrong position --- //
        String wrongPositionRegex = buildWrongPositionRegex();
        if (wrongPositionRegex != null)
            dataset.regexFilter(wrongPositionRegex);

        if (!dataset.isEmpty()){
            // Copy the dataset
            ArrayList<WordEntry> words = new ArrayList<>(dataset.getWordFrequencyList());

            // Remove the words containing letters that are in the wrong position
            for (char letter : wrongPosition.keySet()) {
                ArrayList<Integer> positions = wrongPosition.get(letter);
                for (WordEntry word : words) {
                    // If the word contains the letter
                    if (word.getWord().contains(String.valueOf(letter))) {
                        // Check if the letter is in the wrong position
                        boolean wrongPosition = false;
                        for (int position : positions) {
                            if (word.getWord().charAt(position) == letter) {
                                wrongPosition = true;
                                break;
                            }
                        }
                        // If the letter is in the wrong position, remove the word
                        if (wrongPosition)
                            dataset.removeWord(word.getWord());
                    }
                }
            }
        }
    }

    private String buildCorrectRegex() {
        /*
         * Builds a regex that matches all words that contain the correct letters.
         */
        StringBuilder regex = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            String letter = correct.get(i) == null ? "[a-z]" : String.valueOf(correct.get(i));
            regex.append(letter);
        }
        return regex.toString();
    }

    private String buildWrongPositionRegex() {
        /*
         * Builds a regex that matches all words that contain the letters
         * that are in the wrong position.
         */
        // String of all the letters that are in the wrong position
        StringBuilder letters = new StringBuilder();
        for (char letter : wrongPosition.keySet())
            letters.append(letter);

        return letters.length() > 0 ? String.format("\\b(?=[a-z]*[%s])[a-z]{5}\\b", letters) : null;
    }

    public void close() {
        agent.close();
    }


}
