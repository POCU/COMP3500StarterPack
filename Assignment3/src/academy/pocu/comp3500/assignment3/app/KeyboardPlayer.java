package academy.pocu.comp3500.assignment3.app;

import academy.pocu.comp3500.assignment3.chess.Move;
import academy.pocu.comp3500.assignment3.chess.PlayerBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class KeyboardPlayer extends PlayerBase {
    public KeyboardPlayer(boolean isWhite) {
        this(isWhite, Integer.MAX_VALUE);
    }

    public KeyboardPlayer(boolean isWhite, int maxMoveTimeMilliseconds) {
        super(isWhite, maxMoveTimeMilliseconds);
    }

    @Override
    public Move getNextMove(char[][] board) {
        return getNextMove(board, null);
    }

    @Override
    public Move getNextMove(char[][] board, Move opponentMove) {
        Move move = null;
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        try {
            boolean isMoveValid = false;
            while (!isMoveValid) {
                System.out.println(String.format("Enter %s move:", isWhite() ? "W" : "B"));
                String input = reader.readLine();

                Pattern pattern = Pattern.compile("^([a-hA-H][0-7])\\s+([a-hA-H][0-7])$");
                Matcher matcher = pattern.matcher(input.trim());

                if (!matcher.find()) {
                    System.out.println(String.format("'%s' is not a valid input (e.g. 'c6 d4').", input.trim()));
                    continue;
                }

                String from = matcher.group(1);
                String to = matcher.group(2);
                int fromX = from.charAt(0) - 'a';
                int fromY = from.charAt(1) - '0';
                int toX = to.charAt(0) - 'a';
                int toY = to.charAt(1) - '0';
                move = new Move(fromX, fromY, toX, toY);

                isMoveValid = Game.isMoveValid(board, this, move);

                if (!isMoveValid) {
                    System.out.println(String.format("'%s' is not a valid move.", input.trim()));
                }
            }

            return move;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
