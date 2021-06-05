package academy.pocu.comp3500.lab6.leagueofpocu;

public final class Player {
    private final int id;
    private final String name;
    private final int rating;

    public Player(final int id, final String name, final int rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getRating() {
        return this.rating;
    }

    /**
     * Example:
     * [0012] Hoddor           Rating: 12
     * */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(30);

        sb.append(String.format("[%4d]", this.id).replace(' ', '0'));
        sb.append(String.format(" %-16s ", this.name));
        sb.append(String.format("Rating: %d", this.rating));

        return sb.toString();
    }
}
