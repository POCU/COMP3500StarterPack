package academy.pocu.comp3500.lab4.pocuhacker;

import java.util.HashMap;

public class RainbowTable {
    private final HashMap<String, String> mMap;

    public RainbowTable(final HashMap<String, String> map) {
        mMap = map;
    }

    public boolean contains(final String hash) {
        return mMap.containsKey(hash);
    }

    public String get(final String hash) {
        return mMap.get(hash);
    }
}
