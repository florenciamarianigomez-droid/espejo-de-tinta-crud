package com.espejodetinta.ui;

public final class Ansi {
    private Ansi() {}

    public static final String RESET = "\u001B[0m";
    public static final String BOLD  = "\u001B[1m";

    public static String rgb(int r, int g, int b) {
        return "\u001B[38;2;" + r + ";" + g + ";" + b + "m";
    }

    public static String bgRgb(int r, int g, int b) {
        return "\u001B[48;2;" + r + ";" + g + ";" + b + "m";
    }

    public static final String VERDE    = rgb(63, 122, 120);   // #3f7a78
    public static final String BEIGE    = rgb(227, 214, 195);  // #e3d6c3
    public static final String BG_BEIGE = bgRgb(227, 214, 195);
}
