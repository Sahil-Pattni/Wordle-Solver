package pattni.sahil.automator;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
        // close pop-up
        driver.findElement(By.className("game-icon")).click();
    }

    public void enterGuess(String guess, int row) {
        String xPath = String.format("//*[@id=\"wordle-app-game\"]/div[1]/div/div[1]/div[%d]/div", row);
        WebElement e = driver.findElement(By.xpath(xPath));
        // Make guess
        e.sendKeys(guess);
        // Press enter
        e.sendKeys(Keys.RETURN);
    }

    public String getFeedback(int row) {
        StringBuilder feedback = new StringBuilder();
        String xPath = String.format("//*[@id=\"wordle-app-game\"]/div[1]/div/div[%d]", row);
        WebElement e = driver.findElement(By.xpath(xPath));
        System.out.printf("RESP.TEXT: %s%n", e.getText());
        for (int i = 1; i <= 5; i++) {
            String subPath = String.format("%s/div[%d]/div",xPath, i);
            System.out.println("SUBPATH: " + subPath);
            WebElement letter = e.findElement(By.xpath(subPath));
            // Get value of `data-state` attribute
            String stater = letter.getAttribute("aria-label");
            System.out.println("ARIA-LABEL: " + stater);
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

}
