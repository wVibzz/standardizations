package net.vibzz.standardizations;

public final class Rng {

    public static final long MIX_C1 = 0xBF58476D1CE4E5B9L;
    public static final long MIX_C2 = 0x94D049BB133111EBL;
    public static final long GAMMA = 0x9E3779B97F4A7C15L;
    public static final long SALT_A = 0x6C3671D916B4A139L;
    public static final long SALT_B = 0xA24BAED4963EE407L;

    private Rng() {}

    public static long mix(long z) {
        z = (z ^ (z >>> 30)) * MIX_C1;
        z = (z ^ (z >>> 27)) * MIX_C2;
        return z ^ (z >>> 31);
    }

    public static double uniform(long bits) {
        return (bits >>> 11) * 0x1.0p-53;
    }
}
