

package net.legacyfabric.example.mixin;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.BigTreeFeature;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BigTreeFeature.class)
public abstract class BigTreeMemoryMixin {

    @Shadow
    World world;

    @Inject(
        method = "method_4028(Lnet/minecraft/world/World;Ljava/util/Random;III)Z",
        at = @At("RETURN")
    )
    private void memoryfix$releaseWorld(
            World world,
            Random random,
            int x,
            int y,
            int z,
            CallbackInfoReturnable<Boolean> cir) {

        this.world = null;
    }
}
