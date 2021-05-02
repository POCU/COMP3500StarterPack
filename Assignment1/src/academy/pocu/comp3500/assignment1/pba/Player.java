package academy.pocu.comp3500.assignment1.pba;

public final class Player {
    private String name;
    private int pointsPerGame;
    private int assistsPerGame;
    private int passesPerGame;
    private int shootingPercentage;

    public Player() { }

    public Player(final String name,
                  final int pointsPerGame,
                  final int assistsPerGame,
                  final int passesPerGame,
                  final int shootingPercentage) {
        this.name = name;
        this.pointsPerGame = pointsPerGame;
        this.assistsPerGame = assistsPerGame;
        this.passesPerGame = passesPerGame;
        this.shootingPercentage = shootingPercentage;
    }

    public int getPointsPerGame() {
        return this.pointsPerGame;
    }

    public void setPointsPerGame(int pointsPerGame) {
        this.pointsPerGame = pointsPerGame;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getAssistsPerGame() {
        return this.assistsPerGame;
    }

    public void setAssistsPerGame(int assistsPerGame) {
        this.assistsPerGame = assistsPerGame;
    }

    public int getPassesPerGame() {
        return this.passesPerGame;
    }

    public void setPassesPerGame(int passesPerGame) {
        this.passesPerGame = passesPerGame;
    }

    public int getShootingPercentage() {
        return this.shootingPercentage;
    }

    public void setShootingPercentage(int shootingPercentage) {
        this.shootingPercentage = shootingPercentage;
    }
}
