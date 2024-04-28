package dev.siea.collections.util;

import dev.siea.collections.Collections;

import java.util.Arrays;

public class Log {
    public static void error(Exception e) {
        Collections.getPlugin().getLogger().severe("An Error occurred: " + e);
    }

    public static void str(String s) {
        Collections.getPlugin().getLogger().severe(s);
    }
}
