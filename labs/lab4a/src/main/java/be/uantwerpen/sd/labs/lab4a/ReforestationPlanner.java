package be.uantwerpen.sd.labs.lab4a;


import java.util.Locale;

/*
TODO: Implement this class. This is the Abstract class for the factories to inherit from.
Methods you will implement:
  - Plant createPlant()
*/
public class ReforestationPlanner {
    public PlantingPlan plan(double hectares, String soil) {
        /*
            TODO: Replace this instance creation by a factory method call.
            TIP: Now we assign 'null', but should assign a plant.
        */
        Plant plant = null;
        double spacing = Math.max(plant.spacingMeters(), soilAdjustment(soil));
        int count = (int) Math.round((hectares * 10_000.0) / (spacing * spacing));
        String note = "Plant " + count + " × " + plant.info() + " on " + hectares + " ha (soil=" + soil + ")";
        return new PlantingPlan(plant, count, spacing, note);
    }

    private double soilAdjustment(String soil) {
        String s = soil.toLowerCase(Locale.ROOT);
        if (s.contains("sandy")) return 2.5;
        if (s.contains("clay")) return 3.0;
        if (s.contains("wet") || s.contains("saline")) return 2.0;
        return 2.0;
    }
}

