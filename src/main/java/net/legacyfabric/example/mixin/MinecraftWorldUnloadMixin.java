
package net.legacyfabric.example.mixin;

import net.legacyfabric.example.memory.NetworkSessionTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftWorldUnloadMixin {

    @Unique
    private boolean memoryfix$closingSingleplayer;

    @Inject(
        method = "connect(Lnet/minecraft/client/world/ClientWorld;Ljava/lang/String;)V",
        at = @At("HEAD")
    )
    private void memoryfix$beforeDisconnect(
            ClientWorld world, String message, CallbackInfo ci) {

        MinecraftClient client = (MinecraftClient)(Object)this;

        memoryfix$closingSingleplayer =
                world == null && client.getServer() != null;
    }

    @Inject(
        method = "connect(Lnet/minecraft/client/world/ClientWorld;Ljava/lang/String;)V",
        at = @At("TAIL")
    )
    private void memoryfix$afterDisconnect(
            ClientWorld world, String message, CallbackInfo ci) {

        if (memoryfix$closingSingleplayer) {
            memoryfix$closingSingleplayer = false;
            NetworkSessionTracker.cleanup();
        }
    }
}
