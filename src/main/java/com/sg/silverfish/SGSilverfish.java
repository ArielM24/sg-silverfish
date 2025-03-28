package com.sg.silverfish;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Difficulty;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SGSilverfish implements ModInitializer {
	public static final String MOD_ID = "sg-silverfish";

	public static final Random r = new Random();

	public static final int spawnRatio = 50;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerEntityEvents.ENTITY_LOAD.register((entity, serverLevel) -> {
			if(!entity.getWorld().getDifficulty().equals(Difficulty.HARD)){
				return;
			}
			if (!entity.getType().equals(EntityType.BAT)) {
				return;
			}
			BatEntity bat = (BatEntity) entity;
			NbtElement check = ((IEntityDataSaver)bat).getPersistentData().get("silverfish_spawn");
			if(check != null){
				return;
			}
			((IEntityDataSaver)bat).getPersistentData().putString("silverfish_spawn", "checked");
			boolean willSpawn = r.nextInt(100) <= spawnRatio;
			if (!willSpawn) {
				return;
			}
			if (bat.getPos().getY() >= 0) {
				return;
			}
			SilverfishEntity silverfish = EntityType.SILVERFISH.create(serverLevel, SpawnReason.NATURAL);
			silverfish.setPos(bat.getPos().getX(), bat.getPos().getY(), bat.getPos().getZ());
			serverLevel.spawnNewEntityAndPassengers(silverfish);
		});
	}
}