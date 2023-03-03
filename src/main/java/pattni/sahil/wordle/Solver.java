package pattni.sahil.wordle;

import pattni.sahil.automator.WordleAgent;
import pattni.sahil.data.Dataset;
import pattni.sahil.data.WordEntry;

import java.util.ArrayList;
import java.util.Hashtable;

public class Solver {
    private final String startWord;

    // Correctly guessed letters
    Hashtable<Integer, Character> correct;
    // Letters that are not in the word
    ArrayList<Character> incorrect;
    // Letters that are in the word but in the wrong position
    // Key is the letter, value is an array of positions they are not in
    Hashtable<Character, ArrayList<Integer>> wrongPosition;

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
        // Remove words that don't match the correct letters
        String correctRegex = buildCorrectRegex();
        dataset.regexFilter(correctRegex);

        // Remove words that contain incorrect letters
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

        // Remove words that don't contain letters in the wrong position
        String wrongPositionRegex = buildWrongPositionRegex();
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
        // Build regex for correct letters
        StringBuilder regex = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            String letter = correct.get(i) == null ? "[a-z]" : String.valueOf(correct.get(i));
            regex.append(letter);
        }
        return regex.toString();
    }

    private String buildWrongPositionRegex() {
        // String of all the letters that are in the wrong position
        StringBuilder letters = new StringBuilder();
        for (char letter : wrongPosition.keySet())
            letters.append(letter);

        return String.format("\\b(?=[a-z]*[%s])[a-z]{5}\\b", letters);
    }

    public void close() {
        agent.close();
    }


}
