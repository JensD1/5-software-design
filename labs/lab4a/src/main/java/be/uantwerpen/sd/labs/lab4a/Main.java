package be.uantwerpen.sd.labs.lab4a;

import be.uantwerpen.sd.labs.lab4a.planners.*;
public class Main {
    public static void main(String[] args) {
        System.out.println("Reforestation of Antwerp:");
        /*
            TODO: Initialize this variable appropriately.
        */
        ReforestationPlanner antwerp1 = null;
        System.out.println(antwerp1.plan(1.2, "loam").getNote());
        /*
            TODO: Initialize this variable appropriately.
        */
        ReforestationPlanner antwerp2 = null;
        System.out.println(antwerp2.plan(2.0, "loam").getNote());

        System.out.println("\nReforestation of East-Flanders:");
        /*
            TODO: Initialize this variable appropriately.
        */
        ReforestationPlanner east1 = null;
        System.out.println(east1.plan(5.0, "clay").getNote());
        /*
            TODO: Initialize this variable appropriately.
        */
        ReforestationPlanner east2 = null;
        System.out.println(east2.plan(1.5, "clay").getNote());

        System.out.println("\nReforestation of West-Flanders:");
        /*
            TODO: Initialize this variable appropriately.
        */
        ReforestationPlanner west1 = null;
        System.out.println(west1.plan(3.1, "sandy").getNote());
        /*
            TODO: Initialize this variable appropriately.
        */
        ReforestationPlanner west2 = null;
        System.out.println(west2.plan(0.8, "wet").getNote());
        /*
            TODO: Initialize this variable appropriately.
        */
        ReforestationPlanner west3 = null;
        System.out.println(west3.plan(2.6, "loam").getNote());
    }
}
