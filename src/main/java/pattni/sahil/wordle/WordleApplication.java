package pattni.sahil.wordle;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WordleApplication {

    public static void main(String[] args) {
        Solver solver = null;
        int result = -1;
        try {
            solver = new Solver();
            result = solver.solve();

            System.out.println("Solved: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (solver != null && (result == -1 || result == 0))
                solver.close();
        }
    }

}
