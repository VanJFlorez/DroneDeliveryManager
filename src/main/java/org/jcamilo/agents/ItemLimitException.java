package org.jcamilo.agents;

public class ItemLimitException extends Exception {

    public ItemLimitException() {
        super("You have exceeded the limit permited items by drone.");
    }
}