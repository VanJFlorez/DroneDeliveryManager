package org.jcamilo.agents;

public class ItemLimitException extends Exception {
    private static final long serialVersionUID = -6018714286268491500L;

    public ItemLimitException() {
        super("You have exceeded the maximum items by drone.");
    }
}