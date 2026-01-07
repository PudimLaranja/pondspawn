package com.origin.pondspawn;

import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.entity.enums.TongueModes;

public interface PlayerWithTongueData {
    void pondspawn$setTongueEntity(Tongue tongue);
    Tongue pondspawn$getTongueEntity();

}
