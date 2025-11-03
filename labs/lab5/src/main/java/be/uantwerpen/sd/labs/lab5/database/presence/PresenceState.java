package be.uantwerpen.sd.labs.lab5.database.presence;

public interface PresenceState {
    boolean canCheckIn();

    boolean canCheckOut();

    PresenceState onCheckIn();

    PresenceState onCheckOut();
}
