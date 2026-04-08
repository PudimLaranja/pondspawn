package com.origin.pondspawn.entity.custum;

import com.origin.pondspawn.init.ModEntityTypes;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayerManager;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class TongueScarf extends Entity{

    private PlayerEntity player;

    private boolean built = false;

    public static final TrackedData<Optional<UUID>> PLAYER_UUID = DataTracker
            .registerData(TongueScarf.class,TrackedDataHandlerRegistry.OPTIONAL_UUID);

    public TongueScarf(EntityType<?> type, World world) {
        super(type, world);

        this.ignoreCameraFrustum = true;
    }

    public static TongueScarf factory(World world,PlayerEntity parent) {
        TongueScarf entity = new TongueScarf(ModEntityTypes.TONGUE_SCARF_ENTITY_TYPE,world);
        entity.player = parent;
        entity.setPlayerUuid(parent.getUuid());
        entity.setPosition(parent.getPos());

        entity.built = true;
        return entity;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getVehicle() instanceof PlayerEntity player) {
            OriginComponent component = ModComponents.ORIGIN.get(player);
            OriginLayer defaultLayer = OriginLayerManager.get(Identifier.of("origins","origin"));

            if (defaultLayer == null) return;

            Origin currentOrigin = component.getOrigin(defaultLayer);

            if (!(currentOrigin.getId().equals(Identifier.of("pondspawn","pondspawn")))) {
                this.getPassengerList().forEach(Entity::kill);
                this.kill();

            }
        } else {
            this.kill();
        }
    }

    @Override
    public void kill() {
        this.getPassengerList().forEach(Entity::kill);
        super.kill();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(PLAYER_UUID,Optional.empty());
    }

   public UUID getPlayerUuid() {
        return this.dataTracker.get(PLAYER_UUID).orElse(null);
   }

   public void setPlayerUuid(UUID uuid) {
        this.dataTracker.set(PLAYER_UUID,Optional.ofNullable(uuid));
   }


    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
