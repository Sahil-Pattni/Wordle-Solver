package pattni.sahil.wordle;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import pattni.sahil.automator.WordleAgent;

@SpringBootApplication
public class WordleApplication {

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WordleAgent agent = new WordleAgent();
        try {
            // Step 1
            sleep(500);
            agent.enterGuess("soare", 1);
            sleep(2500);
            String feedback = agent.getFeedback(1);
            System.out.printf("%n---------%nFEEDBACKRES:%s%n---------%n", feedback);

            sleep(500);
            agent.enterGuess("voila", 2);
            sleep(2500);
            feedback = agent.getFeedback(2);
            System.out.printf("%n---------%nFEEDBACKRES:%s%n---------%n", feedback);

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            sleep(4000);
            agent.close();
        }

    }

}
