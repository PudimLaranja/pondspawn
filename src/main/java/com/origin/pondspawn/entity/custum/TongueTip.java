package com.origin.pondspawn.entity.custum;

import com.origin.pondspawn.command.ClearTongue;
import com.origin.pondspawn.entity.enums.TargetTypes;
import com.origin.pondspawn.init.ModEntityTypes;
import com.origin.pondspawn.util.ModUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class TongueTip extends Entity{

    private Tongue parent;

    private static final TrackedData<Optional<UUID>> PARENT_UUID = DataTracker
            .registerData(TongueTip.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    public TongueTip(EntityType<?> type, World world) {
        super(type, world);
    }

    public static TongueTip factory(World world,Tongue parent) {
        TongueTip tip = new TongueTip(ModEntityTypes.TONGUE_TIP_ENTITY_TYPE,world);
        tip.setPosition(parent.getPos());
        tip.parent = parent;
        parent.tongueTip = tip;
        tip.setParentUuid(parent.getUuid());
        return tip;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.parent != null && this.parent.getTargetMode() != TargetTypes.AIR) {
            this.setPosition(parent.getPos());
        }
    }


        @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(PARENT_UUID,Optional.empty());
    }

    public UUID getParentUUID() {
        return this.dataTracker.get(PARENT_UUID).orElse(null);
    }

    public void setParentUuid(UUID uuid) {
        this.dataTracker.set(PARENT_UUID,Optional.ofNullable(uuid));
    }


    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
