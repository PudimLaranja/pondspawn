package com.origin.pondspawn;

import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.entity.custum.TongueScarf;
import net.minecraft.entity.Entity;

public interface PlayerWithTongueData {
    void pondspawn$setTongueEntity(Tongue tongue);

    Tongue pondspawn$getTongueEntity();

    void pondspawn$setScarfEntity(TongueScarf scarf);

    TongueScarf pondspawn$getScarfEntity();

    void pondspawn$setTarget(Entity value);

    Entity pondspawn$getTarget();

    boolean pondspawn$jumpAllowed();

    boolean pondspawn$isTongueOut();

    void pondspawn$setTongueOut(boolean isOut);

}
