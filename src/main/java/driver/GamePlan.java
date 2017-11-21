package driver;

import java.util.ArrayList;

public class GamePlan {

    private ArrayList<GamePlan> plan;
    private int enemyMove;
    private int yourNextMove;
    private GameBoard board;

    public float getScore() {
        return score;
    }

    private void setScore(float score) {
        this.score = score;
    }

    private float score;

    public GamePlan(int move, GameBoard board) {
        plan = new ArrayList<>();
        this.enemyMove = move;
        this.board = board;
    }

    public GamePlan() {
        super();
    }

    public GamePlan myClone() {
        GamePlan result = new GamePlan();
        result.setEnemyMove(this.enemyMove);
        result.setYourNextMove(this.yourNextMove);
        result.setBoard(this.board.clone());
        result.setPlan(new ArrayList<GamePlan>(this.getPlan()));
        result.setScore(this.score);
        return result;
    }

    public float findPlan(boolean enemyStart) {
        if (board.checkLose()) {
            score = 0;
            return 0; //lost game
        }
        if (board.getFreeFields() == 0) {
            score = (float) 0.5;
            return (float) 0.5; //tie game
        }
        if (board.getFreeFields() == 1) {
            yourNextMove = board.getMoves().get(0);
            board.makeMove("x", yourNextMove);
            if (board.checkWin()) {
                score = 1;
                return 1;    //game won
            } else {
                score = (float) 0.5;
                return (float) 0.5;    //tie game
            }
        }

        float bestScore = -1;
        int bestMove = 0;
        ArrayList<GamePlan> bestPlan = null;


        for (Integer myMove : board.getMoves()) {        //all possible my moves
            GameBoard tBoard = this.board.clone();
            if (!enemyStart) {
                tBoard.makeMove("x", myMove);
                if (tBoard.checkWin()) {
                    score = 1;
                    yourNextMove = myMove;
                    return 1;
                }
            }

            ArrayList<GamePlan> tempPlan = new ArrayList<>();
            float currentScore = 0;
            for (Integer enemyMove : tBoard.getMoves()) {    //all enemy possible moves
                GameBoard tempBoard = tBoard.clone();
                tempBoard.makeMove("o", enemyMove);
                GamePlan newPlan = new GamePlan(enemyMove, tempBoard);
                currentScore += newPlan.findPlan(false);
                tempPlan.add(newPlan);
            }
            currentScore = currentScore / tBoard.getMoves().size();

            if (currentScore >= bestScore) {
                bestMove = myMove;
                bestScore = currentScore;
                bestPlan = tempPlan;
            }
        }

        score = bestScore;
        plan = bestPlan;
        yourNextMove = bestMove;
        return bestScore;
    }

    public ArrayList<GamePlan> getPlan() {
        return plan;
    }

    private void setPlan(ArrayList<GamePlan> plan) {
        this.plan = plan;
    }

    public int getEnemyMove() {
        return enemyMove;
    }

    private void setEnemyMove(int enemyMove) {
        this.enemyMove = enemyMove;
    }

    public int getYourNextMove() {
        return yourNextMove;
    }

    public void setYourNextMove(int yourNextMove) {
        this.yourNextMove = yourNextMove;
    }

    public GameBoard getBoard() {
        return board;
    }

    private void setBoard(GameBoard board) {
        this.board = board;
    }


}
