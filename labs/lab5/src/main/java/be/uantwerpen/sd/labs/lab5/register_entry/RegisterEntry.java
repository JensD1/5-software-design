package be.uantwerpen.sd.labs.lab5.register_entry;

import be.uantwerpen.sd.labs.lab5.employee.Employee;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record RegisterEntry(String id, Employee employee, boolean checkIn, LocalDateTime timestamp) {
    public RegisterEntry(Employee employee, boolean checkIn) {
        this(UUID.randomUUID().toString(), employee, checkIn, LocalDateTime.now());
    }

    public RegisterEntry withCheckIn(boolean newCheckIn) {
        return new RegisterEntry(id, employee, newCheckIn, timestamp);
    }

    public RegisterEntry withTimestamp(LocalDateTime newTs) {
        return new RegisterEntry(id, employee, checkIn, newTs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegisterEntry other)) return false;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
