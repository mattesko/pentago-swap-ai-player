package student_player;

import boardgame.Move;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;
import pentago_swap.PentagoPlayer;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {

    private MinimaxOptimizer optimizer = null;

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
    public Move chooseMove(PentagoBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...

        Move myMove = boardState.getRandomMove();

        try {
            myMove = chooseMove(boardState, this.optimizer);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        return myMove;
    }

    private Move chooseMove(PentagoBoardState boardState, MinimaxOptimizer optimizer) {

        Move myMove = boardState.getRandomMove();

        if (boardState.getTurnNumber() == 1 || boardState.getTurnNumber() == 2) {

            // Get first best move
            PentagoBoardState nextMove = (PentagoBoardState) boardState.clone();

            this.optimizer = new MinimaxOptimizer(boardState);
            nextMove.processMove((PentagoMove) this.optimizer.getNextBestMove(10));
        }
        else {
            myMove = optimizer.getNextBestMove(3);
        }

        return myMove;
    }
}