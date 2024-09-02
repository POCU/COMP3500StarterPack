package academy.pocu.comp3500.lab4.pocuhacker;

import java.util.HashMap;

public class RainbowTable {
    private final HashMap<String, String> map;

    public RainbowTable(final HashMap<String, String> map) {
        this.map = map;
    }

    public boolean contains(final String hash) {
        return this.map.containsKey(hash);
    }

    public String get(final String hash) {
        return this.map.get(hash);
    }
}
