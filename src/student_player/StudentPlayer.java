package student_player;

import boardgame.Move;

import pentago_swap.PentagoPlayer;
import pentago_swap.PentagoBoardState;
import student_player.montecarlo.MonteCarloTreeSearchOptimizer;

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

        MonteCarloTreeSearchOptimizer mctsOptimizer = new MonteCarloTreeSearchOptimizer();

        long start = System.currentTimeMillis();
        Move myMove = mctsOptimizer.findNextMove(pentagoBoardState);
        float timeElapsed = (System.currentTimeMillis() - start) / 1000f;

        if (DEBUG) {
            System.out.println(String.format("Time for Move (s): %f", timeElapsed));
            pentagoBoardState.printBoard();
        }

        // Return your move to be processed by the server.
        return myMove;
    }
}