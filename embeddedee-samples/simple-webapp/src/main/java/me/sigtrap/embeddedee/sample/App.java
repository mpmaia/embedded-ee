package me.sigtrap.embeddedee.sample;

import me.sigtrap.embeddedee.core.EmbeddedEE;

public class App {

    public static void main(String[] args) {
        EmbeddedEE app = new EmbeddedEE();
        //forward command line args to EmbeddedEE
        app.start(args);
    }
}
