package pattni.sahil.automator;

public interface Agent {
    void enterGuess(String guess, int row);
    boolean isAccepted(int row);
    String getFeedback(int row);
    void undoGuess(int row);
    void share();
    void close();
}
