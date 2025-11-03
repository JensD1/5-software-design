package be.uantwerpen.sd.labs.lab5.database.presence;

public final class AbsentState implements PresenceState {
    @Override
    public boolean canCheckIn() {
        return true;
    }

    @Override
    public boolean canCheckOut() {
        return false;
    }

    @Override
    public PresenceState onCheckIn() {
        return new PresentState();
    }

    @Override
    public PresenceState onCheckOut() {
        return this;
    }
}
