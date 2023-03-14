package pattni.sahil.automator;
import org.openqa.selenium.*;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WordleAgent {
    // Agent to run Wordle via Selenium
    private final WebDriver driver = new SafariDriver();

    public WordleAgent() {
        // Change browser window size
        Dimension d = new Dimension(360, 640);
        driver.manage().window().setSize(d);

        String website = "https://www.nytimes.com/games/wordle/index.html";
        driver.get(website);
        // wait for page to load
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        // close pop-up
        driver.findElement(By.className("game-icon")).click();
    }

    public void enterGuess(String guess, int row) {
        // Wait for page to load
        if (row == 1) sleep(500);

        try {
            String xPath = String.format("//*[@id=\"wordle-app-game\"]/div[1]/div/div[1]/div[%d]/div", row);
            if (row == 6)
                xPath = "//*[@id=\"wordle-app-game\"]/div[1]/div/div[6]/div[1]";

            WebElement e = driver.findElement(By.xpath(xPath));
            // Make guess
            e.sendKeys(guess);
            // Press enter
            e.sendKeys(Keys.RETURN);
            // Sleep for 2 seconds
            // Keep this >= 1500
            sleep(2000);
        } catch (Exception e) {
            System.out.printf("ERROR: getting row #%d%n", row);
            e.printStackTrace();
        }
    }

    public boolean isAccepted(int row) {
        String xPath = String.format("//*[@id=\"wordle-app-game\"]/div[1]/div/div[%d]/div[1]/div", row);
        WebElement e = driver.findElement(By.xpath(xPath));

        // Get value of `data-state` attribute
        String stater = e.getAttribute("data-state");
        return !stater.equals("tbd");
    }

    public void undoGuess(int row) {
        try {
            String xPath = String.format("//*[@id=\"wordle-app-game\"]/div[1]/div/div[1]/div[%d]/div", row);
            if (row == 6)
                xPath = "//*[@id=\"wordle-app-game\"]/div[1]/div/div[6]/div[1]";

            WebElement e = driver.findElement(By.xpath(xPath));
            // Press backspace 5 times
            for (int i = 0; i < 5; i++)
                e.sendKeys(Keys.BACK_SPACE);
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

    public void share() {
        // Clicks on the share button
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Find element `AuthCTA-module_shareButton__XXXXX` where XXXXX is a random string
        WebElement shareButton = wait.until(
                d -> d.findElement(By.cssSelector("button[class*='AuthCTA-module_shareButton__']"))

        );
        System.out.println("Share button found");
        shareButton.click();
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
