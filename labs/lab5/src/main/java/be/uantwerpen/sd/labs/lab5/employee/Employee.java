package be.uantwerpen.sd.labs.lab5.employee;

import java.util.Objects;
import java.util.UUID;

public record Employee(String id, String name, String function) {

    public Employee(String name, String function) {
        this(UUID.randomUUID().toString(), name, function);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee other)) return false;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
