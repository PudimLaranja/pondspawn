package com.origin.pondspawn.component.classes;

import com.origin.pondspawn.component.interfaces.TongueComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

import java.util.UUID;

public class MyTongueComponent implements TongueComponent {
    private UUID tongueUuid;


    @Override
    public void setTongueUuid(UUID tongueUuid) {
        this.tongueUuid = tongueUuid;
    }

    @Override
    public UUID getTongueUuid() {
        return this.tongueUuid;
    }

    @Override
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (nbt.contains("TongueUuid")) {
            this.tongueUuid = nbt.getUuid("TongueUuid");
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        if (this.tongueUuid != null) {
            nbt.putUuid("TongueUuid", this.tongueUuid);
        }
    }
}
