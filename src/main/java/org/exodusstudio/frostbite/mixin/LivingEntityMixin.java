package org.exodusstudio.frostbite.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements ColdAffectedEntity {
    public float innerTemperature = 20f;
    public float outerTemperature = 20f;

    Map<String, Float> tempsPerBlock = Map.of(
            "lava", 5f,
            "torch", 2f,
            "campfire", 7f
    );

    @Unique
    LivingEntity frostbite$livingEntity = (LivingEntity) ((Object) this);


    @Inject(at = @At("HEAD"), method = "baseTick()V")
    public void tick(CallbackInfo ci) {
//        try {
//            ((Player) ((Object) this)).getZ();
//        } catch (ClassCastException e) {
//
//        }
        if (frostbite$livingEntity.level().dimension().toString().equals("hoarfrost")) {
            frostbite$livingEntity.level()
                    .getBlockStates(frostbite$livingEntity.getBoundingBox().deflate(10).inflate(2.5))
                    .forEach(blockState -> {
                                String blockname = blockState.getBlock().getName().toString();
                                if (tempsPerBlock.containsKey(blockname)) {
                                    outerTemperature = outerTemperature +
                                            (tempsPerBlock.get(blockname) / (1 + frostbite$livingEntity.distanceToSqr(blockState)));
                                }
                            }
                    );
            if (innerTemperature != outerTemperature) {
                float delta = outerTemperature - innerTemperature;
                innerTemperature += (float) Math.round(delta / 10) / 2;
            }
        }
    }
}
