
package net.legacyfabric.example.mixin;

import net.legacyfabric.example.memory.NetworkSessionTracker;
import net.legacyfabric.fabric.impl.networking.AbstractNetworkAddon;
import net.legacyfabric.fabric.impl.networking.GlobalReceiverRegistry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GlobalReceiverRegistry.class, remap = false)
public abstract class NetworkSessionTrackingMixin {

    @Inject(method = "startSession", at = @At("TAIL"), remap = false)
    private void memoryfix$start(
            AbstractNetworkAddon<?> addon,
            CallbackInfo ci) {
        NetworkSessionTracker.started(
                (GlobalReceiverRegistry<?>)(Object)this, addon);
    }

    @Inject(method = "endSession", at = @At("TAIL"), remap = false)
    private void memoryfix$end(
            AbstractNetworkAddon<?> addon,
            CallbackInfo ci) {
        NetworkSessionTracker.ended(addon);
    }
}

