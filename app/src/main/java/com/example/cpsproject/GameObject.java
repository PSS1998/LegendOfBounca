 package com.example.cpsproject;

abstract public class GameObject {
    private String name;
    private final String id;

    public GameObject(String name) {
        this.id = GameObjectIDManager.generateNewId();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    abstract void update(double deltaTime);
}
