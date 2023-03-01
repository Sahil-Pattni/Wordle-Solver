package pattni.sahil.automator;
import org.openqa.selenium.*;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;

public class WordleAgent {
    // Agent to run Wordle via Selenium
    private final WebDriver driver = new SafariDriver();

    public WordleAgent() {
        String website = "https://www.nytimes.com/games/wordle/index.html";
        driver.get(website);
        // wait for page to load
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        // Change browser window size
        Dimension d = new Dimension(500, 700);
        driver.manage().window().setSize(d);
        // close pop-up
        driver.findElement(By.className("game-icon")).click();
    }

    public void enterGuess(String guess, int row) {
        try {
            sleep(1000);
            String xPath = String.format("//*[@id=\"wordle-app-game\"]/div[1]/div/div[1]/div[%d]/div", row);
            if (row == 6)
                xPath = "//*[@id=\"wordle-app-game\"]/div[1]/div/div[6]/div[1]";

            WebElement e = driver.findElement(By.xpath(xPath));
            // Make guess
            e.sendKeys(guess);
            // Press enter
            e.sendKeys(Keys.RETURN);
            // Sleep for 2 seconds
            sleep(2000);
        } catch (Exception e) {
            System.out.printf("ERROR: getting row #%d%n", row);
            e.printStackTrace();
        }

    }

    public String getFeedback(int row) {
        StringBuilder feedback = new StringBuilder();
        String xPath = String.format("//*[@id=\"wordle-app-game\"]/div[1]/div/div[%d]", row);
        WebElement e = driver.findElement(By.xpath(xPath));


        for (int i = 1; i <= 5; i++) {
            String subPath = String.format("%s/div[%d]/div",xPath, i);
            WebElement letter = e.findElement(By.xpath(subPath));
            // Get value of `data-state` attribute
            String stater = letter.getAttribute("aria-label");
            System.out.printf("FEEDBACK_REC: %s%n", stater);
            String[] state = stater.split(" ");
            switch (state[1]) {
                // Correct
                case "correct" -> feedback.append("1");
                // Wrong
                case "absent" -> feedback.append("0");
                // Wrong position
                case "present" -> feedback.append("?");
            }
        }
        return feedback.toString();
    }

    public void close() {
        driver.close();
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
