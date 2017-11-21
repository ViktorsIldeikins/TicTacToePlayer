package driver;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GameBoard {

    private String[] board = new String[9];
    private int freeFields;
    private ArrayList<Integer> moves;

    public GameBoard() {
        super();
        initMoves();
        initBoard();
        freeFields = 9;
    }

    public GameBoard clone() {
        GameBoard result = new GameBoard();

        result.setFreeFields(this.getFreeFields());
        result.board = new String[9];

        System.arraycopy(this.board, 0, result.board, 0, 9);
        result.moves = new ArrayList<>();
        result.moves.addAll(this.moves);

        return result;
    }

    public void makeMove(String player, int place) {
        freeFields--;
        board[place] = player;
        moves.removeIf(move -> move == place);
    }


    public boolean checkLose() {
        return checkEndOfGame("o");
    }


    public boolean checkWin() {
        return checkEndOfGame("x");
    }

    private boolean checkEndOfGame(String symbol) {
        if (board[0].contains(symbol) && board[1].contains(symbol) && board[2].contains(symbol)) return true;
        if (board[3].contains(symbol) && board[4].contains(symbol) && board[5].contains(symbol)) return true;
        if (board[6].contains(symbol) && board[7].contains(symbol) && board[8].contains(symbol)) return true;

        if (board[0].contains(symbol) && board[3].contains(symbol) && board[6].contains(symbol)) return true;
        if (board[1].contains(symbol) && board[4].contains(symbol) && board[7].contains(symbol)) return true;
        if (board[2].contains(symbol) && board[5].contains(symbol) && board[8].contains(symbol)) return true;

        if (board[0].contains(symbol) && board[4].contains(symbol) && board[8].contains(symbol)) return true;
        if (board[6].contains(symbol) && board[4].contains(symbol) && board[2].contains(symbol)) return true;

        return false;
    }

    public void checkEnemyMove(WebDriver driver) {
        List<WebElement> fields = driver.findElements(By.className("square"));
        freeFields = 0;
        //	initMoves();
        moves = new ArrayList<>();
        System.out.println();
        for (int i = 0; i < 9; i++) {
            board[i] = getInnerClass(fields.get(i));
            if (board[i].equals("")) {
                System.out.print("_ ");
            } else {
                System.out.print(board[i] + " ");
            }
            if (((i + 1) % 3) == 0) System.out.println();
            if (board[i].equals("")) {
                freeFields++;
                moves.add(i);
            }
        }
        System.out.print("    number of free fields=" + freeFields);
        System.out.println();
    }

    private static String getInnerClass(WebElement element) {
        return element.findElement(By.tagName("div")).getAttribute("class");
    }

    public void makeMoveInBrowser(WebDriver driver) {
        int rand = myRand(freeFields - 1);
        driver.findElements(By.className("square")).get(moves.get(rand)).click();
    }

    public void makeMoveInBrowser(WebDriver driver, int move) {
        driver.findElements(By.className("square")).get(move).click();
    }

    private int myRand(int range) {
        return (int) Math.round(Math.random() * (range));
    }


    public void initBoard() {
        for (int i = 0; i < 9; i++) {
            board[i] = "";
        }
    }

    public void initMoves() {
        moves = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            moves.add(i);
        }
    }


    public String[] getBoard() {
        return board;
    }

    public void setBoard(String[] board) {
        this.board = board;
    }

    public int getFreeFields() {
        return freeFields;
    }

    public void setFreeFields(int freeFields) {
        this.freeFields = freeFields;
    }

    public ArrayList<Integer> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<Integer> moves) {
        this.moves = moves;
    }

}
