package academy.pocu.comp3500.assignment3.app;

import academy.pocu.comp3500.assignment3.chess.Move;
import academy.pocu.comp3500.assignment3.chess.PlayerBase;

import java.util.concurrent.TimeUnit;

final class Game {
    private final static int BOARD_SIZE = 8;

    private static final int[][] kingMoveOffsets = {
            {-1, 1},
            {-1, 0},
            {-1, -1},
            {0, 1},
            {0, -1},
            {1, 1},
            {1, 0},
            {1, -1}
    };

    private static final int[][] knightMoveOffsets = {
            {-2, -1},
            {-2, 1},
            {-1, -2},
            {-1, 2},
            {1, -2},
            {1, 2},
            {2, -1},
            {2, 1}
    };

    private char[][] board;
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

        if (player.getMaxMoveTimeMilliseconds() != Integer.MAX_VALUE && duration > this.maxMoveTimeoutInMilliseconds) {
            this.winner = this.currentTurn % 2 != 0 ? 'B' : 'W';
            this.isGameOver = true;
            this.timeOutInMilliseconds = duration;

            return;
        }

        if (!isMoveValid(board, player, nextMove)) {
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

    public boolean isNextTurnWhite() {
        return this.currentTurn % 2 == 0;
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

        return sb.toString();
    }

    static boolean isMoveValid(char[][] board, PlayerBase player, Move moveOrNull) {
        if (moveOrNull == null) {
            return false;
        }

        if (moveOrNull.fromX >= BOARD_SIZE || moveOrNull.fromX < 0
                || moveOrNull.fromY >= BOARD_SIZE || moveOrNull.fromY < 0) {
            return false;
        }

        final char symbol = board[moveOrNull.fromY][moveOrNull.fromX];

        if (symbol == 0) {
            return false;
        }

        if ((player.isWhite() && !Character.isLowerCase(symbol))
                || !player.isWhite() && Character.isLowerCase(symbol)) {
            return false;
        }

        if (moveOrNull.toX >= BOARD_SIZE || moveOrNull.toX < 0
                || moveOrNull.toY >= BOARD_SIZE || moveOrNull.toY < 0) {
            return false;
        }

        if (moveOrNull.fromX == moveOrNull.toX && moveOrNull.fromY == moveOrNull.toY) {
            return false;
        }

        char symbolInvariant = Character.toLowerCase(symbol);

        switch (symbolInvariant) {
            case 'p':
                return isPawnMoveValid(board, moveOrNull);

            case 'n':
                return isKnightMoveValid(board, moveOrNull);

            case 'b':
                return isBishopMoveValid(board, moveOrNull);

            case 'r':
                return isRookMoveValid(board, moveOrNull);

            case 'q':
                return isQueenMoveValid(board, moveOrNull);

            case 'k':
                return isKingMoveValid(board, moveOrNull);

            default:
                throw new IllegalArgumentException("Unknown piece symbol");
        }
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

    private static boolean isBishopMoveValid(char[][] board, Move move) {
        char fromPiece = board[move.fromY][move.fromX];
        char toPiece = board[move.toY][move.toX];

        if (toPiece != 0 && Character.isLowerCase(fromPiece) == Character.isLowerCase(toPiece)) {
            return false;
        }

        if (Math.abs(move.fromX - move.toX) != Math.abs(move.fromY - move.toY)) {
            return false;
        }

        int xIncrement = move.fromX < move.toX ? 1 : -1;
        int yIncrement = move.fromY < move.toY ? 1 : -1;

        int x = move.fromX + xIncrement;
        int y = move.fromY + yIncrement;

        while (x != move.toX && y != move.toY) {
            if (board[y][x] != 0 && x != move.toX && y != move.toY) {
                return false;
            }

            x += xIncrement;
            y += yIncrement;
        }

        return true;
    }

    private static boolean isRookMoveValid(char[][] board, Move move) {
        char fromPiece = board[move.fromY][move.fromX];
        char toPiece = board[move.toY][move.toX];

        if (toPiece != 0 && Character.isLowerCase(fromPiece) == Character.isLowerCase(toPiece)) {
            return false;
        }

        if (move.fromX == move.toX) {
            int yIncrement = move.fromY < move.toY ? 1 : -1;

            int y = move.fromY + yIncrement;

            while (y != move.toY) {
                if (board[y][move.fromX] != 0) {
                    return false;
                }

                y += yIncrement;
            }

            return true;

        } else if (move.fromY == move.toY) {
            int xIncrement = move.fromX < move.toX ? 1 : -1;

            int x = move.fromX + xIncrement;

            while (x != move.toX) {
                if (board[move.fromY][x] != 0) {
                    return false;
                }

                x += xIncrement;
            }

            return true;
        }

        return false;
    }

    private static boolean isKnightMoveValid(char[][] board, Move move) {
        char fromPiece = board[move.fromY][move.fromX];
        char toPiece = board[move.toY][move.toX];

        if (toPiece != 0 && Character.isLowerCase(fromPiece) == Character.isLowerCase(toPiece)) {
            return false;
        }

        for (int i = 0; i < knightMoveOffsets.length; ++i) {
            if (move.fromX + knightMoveOffsets[i][0] == move.toX && move.fromY + knightMoveOffsets[i][1] == move.toY) {
                return true;
            }
        }

        return false;
    }

    private static boolean isQueenMoveValid(char[][] board, Move move) {
        return isBishopMoveValid(board, move) || isRookMoveValid(board, move);
    }

    private static boolean isKingMoveValid(char[][] board, Move move) {
        char fromPiece = board[move.fromY][move.fromX];
        char toPiece = board[move.toY][move.toX];

        if (toPiece != 0 && Character.isLowerCase(fromPiece) == Character.isLowerCase(toPiece)) {
            return false;
        }

        for (int i = 0; i < kingMoveOffsets.length; ++i) {
            if (move.fromX + kingMoveOffsets[i][0] == move.toX && move.fromY + kingMoveOffsets[i][1] == move.toY) {
                return true;
            }
        }

        return false;
    }

    private static boolean isPawnMoveValid(char[][] board, Move move) {
        char fromPiece = board[move.fromY][move.fromX];
        char toPiece = board[move.toY][move.toX];

        boolean isFromPieceWhite = Character.isLowerCase(fromPiece);
        boolean isToPieceWhite = Character.isLowerCase(toPiece);

        if (toPiece != 0 && isFromPieceWhite == isToPieceWhite) {
            return false;
        }

        if (toPiece != 0 && move.fromX == move.toX) {
            return false;
        }

        boolean hasMoved = isFromPieceWhite ? move.fromY != 6 : move.fromY != 1;

        if (!hasMoved && move.fromX == move.toX && Math.abs(move.toY - move.fromY) == 2) {
            if (move.toY > move.fromY && !isFromPieceWhite && board[move.toY - 1][move.toX] == 0) {
                return true;
            }

            return move.toY < move.fromY && isFromPieceWhite && board[move.toY + 1][move.toX] == 0;
        } else if (move.fromX == move.toX && Math.abs(move.toY - move.fromY) == 1) {
            if (move.toY > move.fromY && !isFromPieceWhite) {
                return true;
            }

            return move.toY < move.fromY && isFromPieceWhite;
        } else if (move.toX == move.fromX - 1 || move.toX == move.fromX + 1) {
            if (toPiece != 0 && isToPieceWhite != isFromPieceWhite) {
                return isFromPieceWhite ? move.toY == move.fromY - 1 : move.toY == move.fromY + 1;
            }
        }

        return false;
    }
}
