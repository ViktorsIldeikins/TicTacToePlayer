package driver;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {

    private static GameBoard board1;

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:/webdriver/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.navigate().to("https://playtictactoe.org/");
        driver.findElement(By.className("mute")).click();

        boolean enemyStarts;
        board1 = new GameBoard();
        GamePlan planMyStart = new GamePlan(0, board1.clone());
        GamePlan planEnemyStart = new GamePlan(0, board1.clone());
        planMyStart.getBoard().makeMove("x", 4);
        planMyStart.findPlan(true);
        planMyStart.setYourNextMove(4);
        planEnemyStart.findPlan(true);
        GamePlan currentPlan;


        for (int j = 0; j < 4; j++) {
            System.out.println("starting game" + (j + 1) + "  clearing board");
            board1.initBoard();
            board1.initMoves();
            board1.checkEnemyMove(driver);
            if (board1.getFreeFields() != 9) {
                currentPlan = planEnemyStart.myClone();
                enemyStarts = true;
            } else {
                enemyStarts = false;
                currentPlan = planMyStart.myClone();
            }

            if (!enemyStarts) {
                board1.makeMoveInBrowser(driver, currentPlan.getYourNextMove());
                Thread.sleep(500);
            }

            board1.initBoard();
            for (int i = 0; i < 6; i++) {
                System.out.print("move" + i + "  ");

                int enemyMove = findNextEnemyMove(driver);
                if ((board1.getFreeFields() == 0) || (board1.checkLose()) || (board1.checkWin())) {
                    break;
                }
                ArrayList<GamePlan> list = currentPlan.getPlan();
                System.out.println("enemy move=" + enemyMove);

                for (GamePlan plan : list) {
                    System.out.print("  " + plan.getEnemyMove());
                    if (plan.getEnemyMove() == enemyMove) {
                        currentPlan = plan;
                        System.out.println("found plan");
                        break;
                    }
                }

                System.out.println("making move with score=" + currentPlan.getScore());

                board1.makeMoveInBrowser(driver, currentPlan.getYourNextMove());

                Thread.sleep(500);
            }
            System.err.println("exiting move loop");

            System.out.println("finished game");
            try {
                Thread.sleep(1500);
                driver.findElement(By.className("restart")).click();
                Thread.sleep(500);
            } catch (Exception e2) {
                System.err.println("exception on restart");
            }
        }


        Thread.sleep(5000);
        driver.close();

    }


    private static int findNextEnemyMove(WebDriver driver) {
        GameBoard board = board1.clone();
        board1.checkEnemyMove(driver);
        for (int i = 0; i < 9; i++) {
            if (board1.getBoard()[i].equals("o") && board.getBoard()[i].equals("")) {
                return i;
            }
        }
        return -1;
    }


}
