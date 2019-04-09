package student_player;

import boardgame.Board;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

import java.util.AbstractMap;
import java.util.ArrayList;

class ABPruningOptimizer {

    private int initialAlpha;
    private int initialBeta;
    private PentagoHeuristicService heuristics;
    private long endTime;
    final private int MAX_SEARCH_TIME_MS = 1900;

    ABPruningOptimizer() {

        this.initialAlpha = Integer.MIN_VALUE;
        this.initialBeta = Integer.MAX_VALUE;
        this.heuristics = new PentagoHeuristicService();
        this.endTime = System.currentTimeMillis() + MAX_SEARCH_TIME_MS;
    }

    ABPruningOptimizer(int initialAlpha, int initialBeta, long maxSearchTime) {
        this.initialAlpha = initialAlpha;
        this.initialBeta = initialBeta;
        this.heuristics = new PentagoHeuristicService();
        this.endTime = System.currentTimeMillis() + maxSearchTime;
    }

    PentagoMove getNextBestMove(int depth, PentagoBoardState pentagoBoardState, int currentPlayer) {
        return prune(depth, pentagoBoardState, currentPlayer, this.initialAlpha, this.initialBeta).getValue();
    }

    @SuppressWarnings("unchecked")
    private AbstractMap.SimpleImmutableEntry<Integer, PentagoMove> prune(int depth, PentagoBoardState pentagoBoardState,
                                                                         int currentPlayer, int alpha, int beta) {

        ArrayList<PentagoMove> legalMoves = pentagoBoardState.getAllLegalMoves();
        PentagoMove bestMove = legalMoves.get(0);
        int bestScore;

        if (depth == 0) {
            bestScore = this.heuristics.computeHeuristic(pentagoBoardState, currentPlayer);
            return new AbstractMap.SimpleImmutableEntry(bestScore, bestMove);
        }

        int i = 0;
        while(i < legalMoves.size() && System.currentTimeMillis() < this.endTime && alpha < beta) {

            PentagoMove currentMove = legalMoves.get(i);
            i++;
            PentagoBoardState boardState = (PentagoBoardState) pentagoBoardState.clone();
            boardState.processMove(currentMove);

            if (currentPlayer == PentagoBoardState.WHITE) {
                bestScore = prune(depth - 1, boardState, PentagoBoardState.BLACK, alpha, beta).getKey();

                // The WHITE player is the MAX player
                if (bestScore > alpha) {
                    alpha = bestScore;
                    bestMove = currentMove;
                }
            }
            else {
                bestScore = prune(depth - 1, boardState, PentagoBoardState.WHITE, alpha, beta).getKey();

                // The BLACK player is the MIN player
                if (bestScore < beta) {
                    beta = bestScore;
                    bestMove = currentMove;
                }
            }
        }

        return new AbstractMap.SimpleImmutableEntry<>((currentPlayer == PentagoBoardState.WHITE) ? alpha : beta, bestMove);
    }


    private class PentagoHeuristicService {

        private PentagoHeuristicService() {super();}

        /**
         * This heuristic computes a better score for pieces that can form one ore more horizontal/vertical/diagonal
         * lines. The longer the uninterrupted line is, the higher the score. Alternatively, if there exists a move that
         * guarantees a win, then that state has the highest score.
         * @param currentPentagoBoardState The current state of the board and game
         * @param currentPlayer The current player
         * @return Score for the currentPentagoBoardState
         */
        private int computeHeuristic(PentagoBoardState currentPentagoBoardState, int currentPlayer) {

            int stateScore = 0;
            int horizontalPieceCount = 0;
            int verticalPieceCount = 0;
            int forwardDiagonalPieceCount = 0;
            int reverseDiagonalPieceCount = 0;

            if (currentPentagoBoardState.getWinner() == Board.NOBODY) {

                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 5; j++) {

                        // Horizontal Lines
                        PentagoBoardState.Piece piece = currentPentagoBoardState.getPieceAt(i, j);
                        PentagoBoardState.Piece neighbor = currentPentagoBoardState.getPieceAt(i, j + 1);

                        if (piece == PentagoBoardState.Piece.WHITE && neighbor == PentagoBoardState.Piece.WHITE) {
                            stateScore += Math.pow(2, horizontalPieceCount);
                            horizontalPieceCount++;
                        }
                        else if(piece == PentagoBoardState.Piece.BLACK && neighbor == PentagoBoardState.Piece.BLACK) {
                            stateScore -= Math.pow(2, horizontalPieceCount);
                            horizontalPieceCount++;
                        }
                        else {
                            horizontalPieceCount = 0;
                        }

                        // Vertical Lines
                        piece = currentPentagoBoardState.getPieceAt(j, i);
                        neighbor = currentPentagoBoardState.getPieceAt(j + 1, i);

                        if (piece == PentagoBoardState.Piece.WHITE && neighbor == PentagoBoardState.Piece.WHITE) {
                            stateScore += Math.pow(2, verticalPieceCount);
                            verticalPieceCount++;
                        }
                        else if(piece == PentagoBoardState.Piece.BLACK && neighbor == PentagoBoardState.Piece.BLACK) {
                            stateScore -= Math.pow(2, verticalPieceCount);
                            verticalPieceCount++;
                        }
                        else {
                            verticalPieceCount = 0;
                        }
                    }
                }

                for (int diagonalIndex = 0; diagonalIndex < 5; diagonalIndex++) {
                    PentagoBoardState.Piece piece = currentPentagoBoardState.getPieceAt(diagonalIndex, diagonalIndex);
                    PentagoBoardState.Piece neighborPiece = currentPentagoBoardState.getPieceAt(diagonalIndex + 1,
                            diagonalIndex + 1);

                    if (piece == PentagoBoardState.Piece.WHITE && neighborPiece == PentagoBoardState.Piece.WHITE) {
                        stateScore += Math.pow(2, forwardDiagonalPieceCount);
                        forwardDiagonalPieceCount++;
                    }
                    else if(piece == PentagoBoardState.Piece.BLACK && neighborPiece == PentagoBoardState.Piece.BLACK) {
                        stateScore -= Math.pow(2, forwardDiagonalPieceCount);
                        forwardDiagonalPieceCount++;
                    }
                    else {
                        forwardDiagonalPieceCount = 0;
                    }

                    piece = currentPentagoBoardState.getPieceAt(diagonalIndex, 5 - diagonalIndex);
                    neighborPiece = currentPentagoBoardState.getPieceAt(diagonalIndex + 1, 4 - diagonalIndex);

                    if (piece == PentagoBoardState.Piece.WHITE && neighborPiece == PentagoBoardState.Piece.WHITE) {
                        stateScore += Math.pow(2, reverseDiagonalPieceCount);
                        reverseDiagonalPieceCount++;
                    }
                    else if(piece == PentagoBoardState.Piece.BLACK && neighborPiece == PentagoBoardState.Piece.BLACK) {
                        stateScore -= Math.pow(2, reverseDiagonalPieceCount);
                        reverseDiagonalPieceCount++;
                    }
                    else {
                        reverseDiagonalPieceCount = 0;
                    }
                }

            }
            else {
                if (currentPentagoBoardState.getWinner() == PentagoBoardState.WHITE) {
                    stateScore = Integer.MAX_VALUE;
                }
                else {
                    stateScore = Integer.MIN_VALUE;
                }
            }

            // Need to invert the score since BLACK would be the MIN Player
            if (currentPlayer == PentagoBoardState.BLACK) {
                stateScore = -stateScore;
            }

            return stateScore;
        }
    }
}
