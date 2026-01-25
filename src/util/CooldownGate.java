package util;

public class CooldownGate {

    private long lastScan = 0;
    private static final long COOLDOWN_MS = 4000;

    public boolean allow() {
        long now = System.currentTimeMillis();
        if (now - lastScan < COOLDOWN_MS) {
            return false;
        }
        lastScan = now;
        return true;
    }
}
