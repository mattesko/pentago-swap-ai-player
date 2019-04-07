package student_player;

import boardgame.Move;

import pentago_swap.PentagoPlayer;
import pentago_swap.PentagoBoardState;
import student_player.montecarlo.MonteCarloOptimizer;

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

        long start = System.currentTimeMillis();
        MonteCarloOptimizer mctsOptimizer = new MonteCarloOptimizer();
        Move myMove = mctsOptimizer.findNextMove(pentagoBoardState);
        float timeElapsed = (System.currentTimeMillis() - start) / 1000f;

        //noinspection ConstantConditions
        if (DEBUG) {
            System.out.println(String.format("Time for Move (s): %f", timeElapsed));
            pentagoBoardState.printBoard();
        }

        return myMove;
    }
}