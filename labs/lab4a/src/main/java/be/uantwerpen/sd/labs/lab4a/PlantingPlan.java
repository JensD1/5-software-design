package be.uantwerpen.sd.labs.lab4a;

public class PlantingPlan {
    private Plant plant;
    private int samplings;
    private double spacingMeters;
    private String note;

    public PlantingPlan(Plant plant, int samplings, double spacingMeters, String note) {
        this.plant = plant;
        this.samplings = samplings;
        this.spacingMeters = spacingMeters;
        this.note = note;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public int getSamplings() {
        return samplings;
    }

    public void setSamplings(int samplings) {
        this.samplings = samplings;
    }

    public double getSpacingMeters() {
        return spacingMeters;
    }

    public void setSpacingMeters(double spacingMeters) {
        this.spacingMeters = spacingMeters;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
