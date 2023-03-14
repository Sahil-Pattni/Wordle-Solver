# Wordle Solver
This is a Java program that automates solving the New York Times Wordle puzzle using Selenium. 
Currently, the program only supports Safari browser, 
    but support for more browsers will be added in future updates.

![output.gif](lib%2Foutput.gif)


### Prerequisites
- Java 8 or above if you're compiling from source, else Java 17 or above.
- Safari browser
  - Ensure that the `Enable Remote Automation` option is enabled in Safari's Develop menu.
    - To do this, go to `Preferences > Advanced > Show Develop menu in menu bar`
    - Then, go to `Develop > Allow Remote Automation`

### Usage
- Clone the repository
- From the project root, run `mvn clean install spring-boot:repackage` to build the jar file.
- If the safari webdriver is not enabled, run `safaridriver --enable` to enable it.

- Run `java -jar target/0.0.2-SNAPSHOT.jar` to start the program.


### Acknowledgements
- The [Selenium](https://www.selenium.dev/) community for providing the
 [Java bindings](https://www.selenium.dev/selenium/docs/api/java/index.html) for the browser automation.
- Josh Wardle and the New York Times for the [Wordle](https://www.nytimes.com/puzzles/wordle) puzzle.