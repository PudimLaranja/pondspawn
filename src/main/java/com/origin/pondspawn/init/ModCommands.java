package com.origin.pondspawn.init;

import com.origin.pondspawn.command.*;
import com.origin.pondspawn.command.ScarfCommand;

public class ModCommands {
    public static void load() {
        TongueCommand.register();
        ClearTongue.register();
        ChangeTongue.register();
        AnimationCommand.register();
        WeightCommand.register();
        ChangePhysics.register();
        TongueJumpCommand.register();
        ScarfCommand.register();
        PushCommand.register();
        ClearPondspawnCommand.register();
    }
}
