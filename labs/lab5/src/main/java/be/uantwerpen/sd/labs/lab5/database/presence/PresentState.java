package be.uantwerpen.sd.labs.lab5.database.presence;

public final class PresentState implements PresenceState {
    @Override
    public boolean canCheckIn() {
        return false;
    }

    @Override
    public boolean canCheckOut() {
        return true;
    }

    @Override
    public PresenceState onCheckIn() {
        return this;
    }

    @Override
    public PresenceState onCheckOut() {
        return new AbsentState();
    }
}
