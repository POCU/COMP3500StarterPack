package academy.pocu.comp3500.assignment3.chess;

public final class Move {
    public int fromX;
    public int fromY;
    public int toX;
    public int toY;

    public Move() { }

    public Move(final int fromX, final int fromY, final int toX, final int toY) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }
}
