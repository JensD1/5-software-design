package be.uantwerpen.sd.labs.lab5.database;

import be.uantwerpen.sd.labs.lab5.database.presence.AbsentState;
import be.uantwerpen.sd.labs.lab5.database.presence.PresenceState;
import be.uantwerpen.sd.labs.lab5.database.presence.PresentState;
import be.uantwerpen.sd.labs.lab5.employee.Employee;
import be.uantwerpen.sd.labs.lab5.register_entry.RegisterEntry;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class RegistrationDB implements Database {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final List<Employee> employees = new ArrayList<>();
    private final List<RegisterEntry> entries = new ArrayList<>();
    private final Map<String, PresenceState> presenceByEmployeeId = new HashMap<>();

    public RegistrationDB() {
    }

    @Override
    public synchronized List<Employee> getEmployees() {
        return List.copyOf(employees);
    }

    @Override
    public synchronized List<RegisterEntry> getEntriesFor(Employee employee) {
        return entries.stream().filter(e -> e.employee().equals(employee)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public synchronized void addEmployee(Employee e) {
        employees.add(e);
        presenceByEmployeeId.put(e.id(), new AbsentState());
        fire(RegistrationEventType.EMPLOYEE_ADDED);
    }

    @Override
    public synchronized void updateEmployee(Employee updated) {
        int idx = indexOfEmployee(updated.id());
        if (idx < 0) return;
        employees.set(idx, updated);
        fire(RegistrationEventType.EMPLOYEE_UPDATED);
    }

    @Override
    public synchronized void removeEmployee(Employee e) {
        int idx = indexOfEmployee(e.id());
        if (idx < 0) return;
        Employee removed = employees.remove(idx);
        List<RegisterEntry> removedEntries = entries.stream().filter(en -> en.employee().equals(removed)).collect(Collectors.toList());
        entries.removeAll(removedEntries);
        presenceByEmployeeId.remove(removed.id());
        fire(RegistrationEventType.EMPLOYEE_REMOVED);
        for (RegisterEntry re : removedEntries) {
            fire(RegistrationEventType.ENTRY_REMOVED);
        }
    }

    @Override
    public synchronized RegisterEntry checkIn(Employee e) {
        PresenceState st = presenceByEmployeeId.computeIfAbsent(e.id(), k -> new AbsentState());
        if (!st.canCheckIn()) throw new IllegalStateException("Already checked in.");
        RegisterEntry entry = new RegisterEntry(e, true);
        entries.add(entry);
        presenceByEmployeeId.put(e.id(), st.onCheckIn());
        fire(RegistrationEventType.ENTRY_ADDED);
        return entry;
    }

    @Override
    public synchronized RegisterEntry checkOut(Employee e) {
        PresenceState st = presenceByEmployeeId.computeIfAbsent(e.id(), k -> new AbsentState());
        if (!st.canCheckOut()) throw new IllegalStateException("Not checked in.");
        RegisterEntry entry = new RegisterEntry(e, false);
        entries.add(entry);
        presenceByEmployeeId.put(e.id(), st.onCheckOut());
        fire(RegistrationEventType.ENTRY_ADDED);
        return entry;
    }

    @Override
    public synchronized void updateEntry(RegisterEntry updated) {
        if (updated.timestamp().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Future timestamps are not allowed.");
        }
        int idx = indexOfEntry(updated.id());
        if (idx < 0) return;
        entries.set(idx, updated);
        recomputePresenceFor(updated.employee().id());
        fire(RegistrationEventType.ENTRY_UPDATED);
    }

    @Override
    public synchronized void removeEntry(String entryId) {
        int idx = indexOfEntry(entryId);
        if (idx < 0) return;
        RegisterEntry old = entries.remove(idx);
        recomputePresenceFor(old.employee().id());
        fire(RegistrationEventType.ENTRY_REMOVED);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    private void fire(RegistrationEventType evt) {
        pcs.firePropertyChange("registration", null, evt);
    }

    private int indexOfEmployee(String id) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).id().equals(id)) return i;
        }
        return -1;
    }

    private int indexOfEntry(String id) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).id().equals(id)) return i;
        }
        return -1;
    }

    private void recomputePresenceFor(String employeeId) {
        PresenceState st = new AbsentState();
        List<RegisterEntry> hist = entries.stream().filter(e -> e.employee().id().equals(employeeId)).sorted(Comparator.comparing(RegisterEntry::timestamp)).collect(Collectors.toList());
        for (RegisterEntry re : hist) {
            if (re.checkIn()) st = st.onCheckIn();
            else st = st.onCheckOut();
        }
        presenceByEmployeeId.put(employeeId, (st instanceof PresentState) ? new PresentState() : new AbsentState());
    }
}
