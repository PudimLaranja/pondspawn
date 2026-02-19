package com.origin.pondspawn;

import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.entity.enums.TongueModes;
import net.minecraft.entity.Entity;

public interface PlayerWithTongueData {
    void pondspawn$setTongueEntity(Tongue tongue);

    Tongue pondspawn$getTongueEntity();

    void pondspawn$setTarget(Entity value);

    Entity pondspawn$getTarget();

    boolean pondspawn$jumpAllowed();

}
