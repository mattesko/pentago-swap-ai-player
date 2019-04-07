package student_player;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

import java.util.ArrayList;

class PentagoSimpleHeuristics {
    PentagoSimpleHeuristics() {super();}

    PentagoMove getNextMove(PentagoBoardState pentagoBoardState) {
        ArrayList<PentagoMove> legalMoves = pentagoBoardState.getAllLegalMoves();

        for(PentagoMove move : legalMoves) {
            PentagoBoardState boardStateClone = (PentagoBoardState) pentagoBoardState.clone();

            boardStateClone.processMove(move);
            if (boardStateClone.getWinner() == pentagoBoardState.getTurnPlayer()) {
                return move;
            }
        }

        return null;
    }
}
