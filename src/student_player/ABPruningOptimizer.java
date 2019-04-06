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

    ABPruningOptimizer() {
        new ABPruningOptimizer(Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    ABPruningOptimizer(int initialAlpha, int initialBeta) {
        super();
        this.initialAlpha = initialAlpha;
        this.initialBeta = initialBeta;
        this.heuristics = new PentagoHeuristicService();
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

        if (depth == 0 || legalMoves.isEmpty()) {
            bestScore = this.heuristics.computeHeuristic(pentagoBoardState, currentPlayer);
            return new AbstractMap.SimpleImmutableEntry(bestScore, bestMove);
        }

        for (PentagoMove currentMove : legalMoves) {
            if (alpha >= beta) {
                break;
            }

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
         *
         * @param currentPentagoBoardState The current state of the board and game
         * @param currentPlayer The current player
         * @return Score for the currentPentagoBoardState
         */
        private int computeHeuristic(PentagoBoardState currentPentagoBoardState, int currentPlayer) {
            return 0;
        }
    }
}
