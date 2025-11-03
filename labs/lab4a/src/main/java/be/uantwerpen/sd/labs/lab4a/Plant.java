package be.uantwerpen.sd.labs.lab4a;


public interface Plant {
    String commonName();

    double spacingMeters();         // recommended spacing between saplings

    String soilPreference();        // a short note (e.g., "loam", "sandy", "clay")

    default String info() {
        return commonName() + " (" + soilPreference() + ", spacing ~" + spacingMeters() + " m)";
    }
}
