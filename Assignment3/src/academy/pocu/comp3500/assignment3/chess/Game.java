package academy.pocu.comp3500.assignment3.chess;

import java.util.concurrent.TimeUnit;

public class Game {
    public final static int BOARD_SIZE = 8;

    protected char[][] board;

    private final int maxMoveTimeoutInMilliseconds;
    private final PlayerBase white;
    private final PlayerBase black;
    private int currentTurn = 0;
    private boolean isGameOver = false;
    private long timeOutInMilliseconds = -1;
    private boolean isIllegalMoveMade = false;
    private char winner = 0;
    private StringBuilder capturedPieces = new StringBuilder(32);
    private Move lastMove = new Move();

    public Game(PlayerBase player1, PlayerBase player2, int maxMoveTimeoutInMilliseconds) {
        this.maxMoveTimeoutInMilliseconds = maxMoveTimeoutInMilliseconds;

        assert (player1.isWhite() != player2.isWhite());

        if (player1.isWhite()) {
            this.white = player1;
            this.black = player2;
        } else {
            this.white = player2;
            this.black = player1;
        }

        this.board = createNewBoard();
    }

    public void nextTurn() {
        if (this.isGameOver) {
            return;
        }

        ++this.currentTurn;

        PlayerBase player;
        if (this.currentTurn % 2 != 0) {
            player = this.white;
        } else {
            player = this.black;
        }

        final char[][] boardCopy = createCopy(this.board);

        long start = System.nanoTime();
        Move nextMove;
        if (this.currentTurn < 2) {
            nextMove = player.getNextMove(boardCopy);
        } else {
            nextMove = player.getNextMove(boardCopy, this.lastMove);
        }
        long end = System.nanoTime();
        long duration = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);

        this.lastMove.fromX = nextMove.fromX;
        this.lastMove.fromY = nextMove.fromY;
        this.lastMove.toX = nextMove.toX;
        this.lastMove.toY = nextMove.toY;

        if (duration > this.maxMoveTimeoutInMilliseconds) {
            this.winner = this.currentTurn % 2 != 0 ? 'B' : 'W';
            this.isGameOver = true;
            this.timeOutInMilliseconds = duration;

            return;
        }

        if (!MoveValidator.isValid(board, player, nextMove)) {
            this.winner = this.currentTurn % 2 != 0 ? 'B' : 'W';
            this.isGameOver = true;
            this.isIllegalMoveMade = true;

            return;
        }

        final char origPiece = this.board[nextMove.toY][nextMove.toX];
        final char newPiece = this.board[nextMove.fromY][nextMove.fromX];

        this.board[nextMove.toY][nextMove.toX] = newPiece;
        this.board[nextMove.fromY][nextMove.fromX] = 0;

        if (origPiece != 0) {
            this.capturedPieces.append(origPiece);
        }

        if (isKingCaptured()) {
            this.winner = this.currentTurn % 2 != 0 ? 'W' : 'B';
            this.isGameOver = true;
            return;
        }
    }

    public boolean isGameOver() {
        return this.isGameOver;
    }

    public boolean isIllegalMoveMade() {
        return this.isIllegalMoveMade;
    }

    public boolean isTimedOut() {
        return this.timeOutInMilliseconds != -1;
    }

    public char getWinner() {
        return this.winner;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);

        char colorCode = this.currentTurn % 2 != 0 ? 'W' : 'B';

        if (this.currentTurn == 0) {
            sb.append(String.format("Turn: %d", this.currentTurn));
        } else {
            sb.append(String.format("Turn: %d (%c)", this.currentTurn, colorCode));
        }

        sb.append(System.lineSeparator());

        if (capturedPieces.length() > 0) {
            sb.append(String.format("Captured: %s", String.join(", ", capturedPieces)));
            sb.append(System.lineSeparator());
        }

        sb.append(System.lineSeparator());

        sb.append(printBoard(board));

        sb.append(System.lineSeparator());

        if (this.timeOutInMilliseconds != -1) {
            sb.append(String.format("Timeout: %dms", this.timeOutInMilliseconds));

            sb.append(System.lineSeparator());

            sb.append(String.format("Game over. %c loses.", colorCode));
        } else if (this.isIllegalMoveMade) {
            sb.append(String.format("Illegal move by %c: (%c%d) -> (%c%d)", colorCode, (char) (this.lastMove.fromX + 'a'), this.lastMove.fromY, (char) (this.lastMove.toX + 'a'), this.lastMove.toY));

            sb.append(System.lineSeparator());

            sb.append(String.format("Game over. %c loses.", colorCode));
        } else if (this.isGameOver) {
            sb.append(String.format("Game over. %c wins.", colorCode));
        } else if (this.currentTurn != 0) {
            char piece = board[this.lastMove.toY][this.lastMove.toX];

            sb.append(String.format("Move: %s (%c%d) -> (%c%d)", piece, (char) (this.lastMove.fromX + 'a'), this.lastMove.fromY, (char) (this.lastMove.toX + 'a'), this.lastMove.toY));
            sb.append(System.lineSeparator());
        }


        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());

        return sb.toString();
    }

    private boolean isKingCaptured() {
        if (this.capturedPieces.toString().contains("k")) {
            return true;
        }

        if (this.capturedPieces.toString().contains("K")) {
            return true;
        }

        return false;
    }

    private static String printBoard(final char[][] board) {
        final StringBuilder sb = new StringBuilder(128);

        sb.append("  ");
        for (int x = 0; x < BOARD_SIZE; ++x) {
            sb.append((char) (x + 'a'));
        }

        sb.append(System.lineSeparator());

        addHorizontalBorder(sb);

        for (int y = 0; y < BOARD_SIZE; ++y) {
            sb.append(y);
            sb.append("|");

            for (int x = 0; x < BOARD_SIZE; ++x) {
                char symbol = board[y][x];

                if (symbol == 0) {
                    sb.append(" ");
                } else {
                    sb.append(String.format("%c", board[y][x]));
                }
            }

            sb.append('|');
            sb.append(System.lineSeparator());
        }

        addHorizontalBorder(sb);

        return sb.toString();
    }

    private static void addHorizontalBorder(StringBuilder sb) {
        sb.append(' ');
        sb.append('+');
        for (int i = 0; i < BOARD_SIZE; ++i) {
            sb.append('-');
        }
        sb.append('+');
        sb.append(System.lineSeparator());
    }

    private static char[][] createCopy(final char[][] board) {
        assert (board.length == BOARD_SIZE);
        assert (board[0].length == BOARD_SIZE);

        final char[][] copy = new char[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; ++i) {
            for (int j = 0; j < BOARD_SIZE; ++j) {
                copy[i][j] = board[i][j];
            }
        }

        return copy;
    }

    private static char[][] createNewBoard() {
        final char[][] board = new char[BOARD_SIZE][BOARD_SIZE];

        // White pieces
        int y = BOARD_SIZE - 1;
        board[y][0] = 'r';
        board[y][1] = 'n';
        board[y][2] = 'b';
        board[y][3] = 'k';
        board[y][4] = 'q';
        board[y][5] = 'b';
        board[y][6] = 'n';
        board[y][7] = 'r';

        // White pawns
        y -= 1;
        for (int x = 0; x < BOARD_SIZE; ++x) {
            board[y][x] = 'p';
        }

        // Black pawns
        y = 1;
        for (int x = 0; x < BOARD_SIZE; ++x) {
            board[y][x] = 'P';
        }

        // Black pieces
        y = 0;
        board[y][0] = 'R';
        board[y][1] = 'N';
        board[y][2] = 'B';
        board[y][3] = 'K';
        board[y][4] = 'Q';
        board[y][5] = 'B';
        board[y][6] = 'N';
        board[y][7] = 'R';

        return board;
    }
}
