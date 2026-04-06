package com.origin.pondspawn.entity.custum;

import com.origin.pondspawn.init.ModEntityTypes;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class TongueScarfMouth extends Entity{

    private TongueScarf parent = null;
    private static final TrackedData<Optional<UUID>> PARENT_UUID = DataTracker.registerData(TongueScarfMouth.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    private boolean initialized = false;

    public UUID getParentUuid() {
        return this.dataTracker.get(PARENT_UUID).orElse(null);
    }

    public void setParent(TongueScarf parent) {
        this.parent = parent;
        this.dataTracker.set(PARENT_UUID,Optional.ofNullable(parent.getUuid()));
    }

    public TongueScarfMouth(EntityType<?> type, World world) {
        super(type, world);
        this.ignoreCameraFrustum = true;
    }


    public static TongueScarfMouth factory(World world,TongueScarf parent) {
        TongueScarfMouth mouth = new TongueScarfMouth(ModEntityTypes.TONGUE_SCARF_MOUTH_ENTITY_TYPE,world);

        mouth.setParent(parent);
        mouth.setPosition(parent.getPos());

        mouth.initialized = true;

        return mouth;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.initialized) return;

        if (parent == null || !parent.isAlive()) this.kill();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(PARENT_UUID,Optional.empty());
    }


    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
