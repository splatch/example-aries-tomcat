package org.code_house.workshop.aries.war.blueprint;

public class Component {

    private String message;

    public Component(String message) {
        this.message = message;
    }

    public void start() {
        System.out.println("Hello with message: " + message);
    }

}
