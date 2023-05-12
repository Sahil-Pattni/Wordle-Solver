package pattni.sahil.automator;

import java.util.HashMap;
import java.util.Map;

public class MockWordleAgent implements Agent {
    private String correctWord;

    private Map<Integer, String> guesses;

    public MockWordleAgent() {
        guesses = new HashMap<>();
    }

    public void setCorrectWord(String correctWord) {
        this.correctWord = correctWord;
    }


    @Override
    public void enterGuess(String guess, int row) {
        guesses.put(row, guess);
    }

    @Override
    public boolean isAccepted(int row) {
        return true;
    }

    @Override
    public String getFeedback(int row) {
        StringBuilder feedback = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            char correctChar = correctWord.charAt(i);
            char guessChar = guesses.get(row).charAt(i);

            if (correctChar == guessChar)
                feedback.append("1");
            else if (correctWord.contains(String.valueOf(guessChar)))
                feedback.append("?");
            else
                feedback.append("0");
        }

        return feedback.toString();
    }

    @Override
    public void undoGuess(int row) { }

    @Override
    public void share() { }

    @Override
    public void close() { }
}
