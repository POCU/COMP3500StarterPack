package academy.pocu.comp3500.lab4.pocuhacker;

public final class User {
    private String id;
    private String email;
    private String passwordHash;

    public User(final String id, final String email, final String passwordHash) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return this.email;
    }

    public String getId() {
        return this.id;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }
}
