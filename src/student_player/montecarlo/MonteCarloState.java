package student_player.montecarlo;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

import java.util.ArrayList;
import java.util.Random;

public class MonteCarloState {
    private PentagoBoardState boardState;
    private PentagoMove pentagoMove;
    private int playerNo;
    private int visitCount;
    private double winScore;

    MonteCarloState(MonteCarloState state) {
        this.pentagoMove = state.getPentagoMove();
        this.boardState = state.getBoardState();
        this.playerNo = state.getPlayerNo();
        this.visitCount = state.getVisitCount();
        this.winScore = state.getWinScore();
    }

    MonteCarloState(PentagoBoardState boardState, PentagoMove pentagoMove) {
        this.boardState = boardState;
        this.pentagoMove = pentagoMove;
    }


    ArrayList<MonteCarloState> getAllPossibleStates() {
        ArrayList<MonteCarloState> possibleStates = new ArrayList<>();
        ArrayList<PentagoMove> availableMoves = this.boardState.getAllLegalMoves();
        availableMoves.forEach(move -> {

            PentagoBoardState newBoard = (PentagoBoardState) boardState.clone();
            MonteCarloState newState = new MonteCarloState(newBoard, move);

            newState.setPlayerNo(newBoard.getOpponent());
            newState.getBoardState().processMove(move);

            possibleStates.add(newState);
        });
        return possibleStates;
    }

    void incrementVisit() {
        this.visitCount++;
    }

    void addScore(double score) {
        if (this.winScore != Integer.MIN_VALUE)
            this.winScore += score;
    }

    void performRandomMove() {
        Random rand = new Random();
        ArrayList<PentagoMove> legalMoves = this.boardState.getAllLegalMoves();
        PentagoMove randomMove = legalMoves.get(rand.nextInt(legalMoves.size()));
        this.boardState.processMove(randomMove);
        togglePlayer();
    }

    void togglePlayer() {
        this.playerNo = this.playerNo == PentagoBoardState.WHITE ? PentagoBoardState.BLACK : PentagoBoardState.WHITE;
    }

    PentagoBoardState getBoardState() {
        return boardState;
    }

    int getPlayerNo() {
        return playerNo;
    }

    int getOpponent() {
        return this.boardState.getOpponent();
    }

    int getVisitCount() {
        return visitCount;
    }

    PentagoMove getPentagoMove() {
        return this.pentagoMove;
    }

    double getWinScore() {
        return winScore;
    }

    void setPlayerNo(int playerNo) {
        this.playerNo = playerNo;
    }

    void setBoardState(PentagoBoardState boardState) {
        this.boardState = boardState;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    void setWinScore(double winScore) {
        this.winScore = winScore;
    }
}
