package mips;

public enum Register {
    ZERO,
    V0,
    V1,
    A0,
    A1,
    A2,
    A3,
    T0,
    T1,
    T2,
    T3,
    T4,
    T5,
    T6,
    T7,
    S0,
    S1,
    S2,
    S3,
    S4,
    S5,
    S6,
    S7,
    T8,
    T9,
    K0,
    K1,
    GP,
    SP,
    FP,
    RA;

    public String toString() {
        return "$" + this.name().toLowerCase();
    }
}
