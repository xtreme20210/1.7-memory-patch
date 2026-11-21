
package net.legacyfabric.example.memory;

import java.util.IdentityHashMap;
import java.util.Map;

import net.legacyfabric.fabric.impl.networking.AbstractNetworkAddon;
import net.legacyfabric.fabric.impl.networking.GlobalReceiverRegistry;

public final class NetworkSessionTracker {

    private static final Map<AbstractNetworkAddon<?>, GlobalReceiverRegistry<?>>
            SESSIONS = new IdentityHashMap<>();

    private NetworkSessionTracker() {
    }

    public static synchronized void started(
            GlobalReceiverRegistry<?> registry,
            AbstractNetworkAddon<?> addon) {
        SESSIONS.put(addon, registry);
    }

    public static synchronized void ended(
            AbstractNetworkAddon<?> addon) {
        SESSIONS.remove(addon);
    }

    public static void cleanup() {
        Map<AbstractNetworkAddon<?>, GlobalReceiverRegistry<?>> snapshot;

        synchronized (NetworkSessionTracker.class) {
            snapshot = new IdentityHashMap<>(SESSIONS);
        }

        int cleaned = 0;

        for (Map.Entry<AbstractNetworkAddon<?>, GlobalReceiverRegistry<?>> entry
                : snapshot.entrySet()) {
            try {
                endSession(entry.getValue(), entry.getKey());
                cleaned++;
            } catch (Exception e) {
                System.err.println("[MemoryFix] Cleanup failed");
                e.printStackTrace();
            }
        }

        System.out.println("[MemoryFix] Cleaned "
                + cleaned + "/" + snapshot.size()
                + " networking sessions");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void endSession(
            GlobalReceiverRegistry<?> registry,
            AbstractNetworkAddon<?> addon) {
        ((GlobalReceiverRegistry) registry)
                .endSession((AbstractNetworkAddon) addon);
    }
}
