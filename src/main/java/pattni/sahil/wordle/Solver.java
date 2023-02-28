package pattni.sahil.wordle;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class Solver {
    private String startWord;

    // Correctly guessed letters
    Hashtable<Integer, Character> correct;
    // Letters that are not in the word
    ArrayList<Character> incorrect;
    // Letters that are in the word but in the wrong position
    // Key is the letter, value is an array of positions they are not in
    Hashtable<Character, ArrayList<Integer>> wrongPosition;

    public Solver(String startWord) {
        this.startWord = startWord;

        // Initialize data structures
        correct = new Hashtable<>();
        incorrect = new ArrayList<>();
        wrongPosition = new Hashtable<>();
    }

    public Solver() {
        // Default start word is "soare"
        this("soare");
    }


    public boolean solve() {
        /*
         * TODO: Write this method
         */
        for (int i = 0; i < 5; i++) {
            continue;
        }
        return false;
    }

    private void step() {
        /*
         * TODO: Write this method
         */

    }

    private void autoStep() {
        /*
         * Same as step, but should not ask
         * for user input.
         */
    }

    public String requestGuess(String defaultGuess) {
        /*
         * Request a guess from the user.
         *
         * @return The user's guess
         */

        while (true) {
            // Take input from user
            Scanner scanner = new Scanner(System.in);
            System.out.printf("Make a guess or press enter to use default guess (%s): ", defaultGuess);
            String guess = scanner.nextLine().strip();

            // If no input, use default guess
            if (guess.length() == 0)
                return defaultGuess;

            // If not 5 letters, ask again
            if (guess.length() != 5) {
                System.out.println("Guess must be 5 letters long, please try again.");
                continue;
            }

            return guess.toLowerCase();
        }
    }

    public void provideFeedback(String feedback) {
        /*
         * Provide feedback to the user.
         * Feedback should be 5 letters long.
         * Each letter should be either:
         *   - `c` for correct
         *   - `i` for incorrect
         *   - `w` for wrong position
         *
         * @return The user's feedback
         */

        // TODO: Modify data structures based on feedback
    }


}
