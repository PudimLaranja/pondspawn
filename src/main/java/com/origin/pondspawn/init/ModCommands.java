package com.origin.pondspawn.init;

import com.origin.pondspawn.command.*;

public class ModCommands {
    public static void load() {
        TongueCommand.register();
        ClearTongue.register();
        ChangeTongue.register();
        AnimationCommand.register();
        WeightCommand.register();
        ChangePhysics.register();
        TongueJumpCommand.register();
    }
}
