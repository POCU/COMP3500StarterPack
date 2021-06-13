package academy.pocu.comp3500.assignment3.chess;

public abstract class PlayerBase {
    private final boolean isWhite;
    private final int maxMoveTimeMilliseconds;

    public PlayerBase(final boolean isWhite, final int maxMoveTimeMilliseconds) {
        this.isWhite = isWhite;
        this.maxMoveTimeMilliseconds = maxMoveTimeMilliseconds;
    }

    public final boolean isWhite() {
        return this.isWhite;
    }

    public final int getMaxMoveTimeMilliseconds() {
        return this.maxMoveTimeMilliseconds;
    }

    public abstract Move getNextMove(final char[][] board);

    public abstract Move getNextMove(final char[][] board, final Move opponentMove);
}
