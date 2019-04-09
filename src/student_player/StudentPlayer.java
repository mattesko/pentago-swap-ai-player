package student_player;

import boardgame.Move;

import pentago_swap.PentagoPlayer;
import pentago_swap.PentagoBoardState;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260692352");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(PentagoBoardState pentagoBoardState) {

        final boolean DEBUG = true;
        final int DEPTH = 3;
        ABPruningOptimizer optimizer = new ABPruningOptimizer();

        long start = System.nanoTime();
        Move myMove = optimizer.getNextBestMove(DEPTH, pentagoBoardState, pentagoBoardState.getTurnPlayer());
        float timeElapsed = (System.nanoTime() - start) / 1000000f;

        if (DEBUG) {
            System.out.println(myMove.toPrettyString());
            pentagoBoardState.printBoard();
            System.out.println(String.format("Time for Move (s): %f", timeElapsed));
        }

        return myMove;
    }
}
