package br.com.fiap.PowerCycle.model;

public class Vehicle {
    public String id;
    public String model;
    public int year;
    public Battery battery;
    public Efficiency efficiency;
    public Range range;
    public Charging charging;

    // Subclasses para os campos aninhados
    public static class Battery {
        public int usableCapacity;
    }

    public static class Efficiency {
        public int realWorld;
    }

    public static class Range {
        public int realWorld;
    }

    public static class Charging {
        public int acPower;
        public int dcPower;
        public String acTime;
        public String dcTime;
    }
}
