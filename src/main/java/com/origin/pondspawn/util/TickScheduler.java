package com.origin.pondspawn.util;

import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TickScheduler {
    private static final List<TickScheduler.PendingTask> TASKS = new ArrayList<>();

    public static void schedule(int ticks, Runnable action) {
        TASKS.add(new TickScheduler.PendingTask(action,ticks));
    }

    public static void onServerTick(MinecraftServer server) {
        Iterator<TickScheduler.PendingTask> iterator = TASKS.iterator();
        List<TickScheduler.PendingTask> toReAdd = new ArrayList<>();

        while (iterator.hasNext()) {
            TickScheduler.PendingTask task = iterator.next();
            if (task.delay <= 0) {
                task.action().run();;
                iterator.remove();
            } else {
                iterator.remove();
                toReAdd.add(new TickScheduler.PendingTask(task.action, task.delay - 1));
            }
        }

        TASKS.addAll(toReAdd);
    }

    record PendingTask(Runnable action,int delay) {}
}
