package com.origin.pondspawn.entity.enums;

import net.minecraft.util.StringIdentifiable;

public enum TongueModes implements StringIdentifiable {
    DEFAULT("default"),
    LOOSE("loose"),
    PULL("pull"),
    LOCK("lock");

    private final String name;

    TongueModes(String name) {
        this.name = name;
    }


    @Override
    public String asString() {
        return this.name;
    }
}
