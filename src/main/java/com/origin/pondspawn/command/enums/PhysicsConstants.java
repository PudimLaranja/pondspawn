package com.origin.pondspawn.command.enums;

import net.minecraft.util.StringIdentifiable;

public enum PhysicsConstants implements StringIdentifiable {
    PULL("pull"),
    SWING("swing"),
    TANGENT("tangent");

    private final String name;

    PhysicsConstants(String name) {
        this.name = name;
    }


    @Override
    public String asString() {
        return this.name;
    }
}
