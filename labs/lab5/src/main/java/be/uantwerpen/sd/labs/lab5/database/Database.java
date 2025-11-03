package be.uantwerpen.sd.labs.lab5.database;

import be.uantwerpen.sd.labs.lab5.employee.Employee;
import be.uantwerpen.sd.labs.lab5.register_entry.RegisterEntry;

import java.beans.PropertyChangeListener;
import java.util.List;

public interface Database {
    List<Employee> getEmployees();

    List<RegisterEntry> getEntriesFor(Employee e);

    void addEmployee(Employee e);

    void updateEmployee(Employee updated);

    void removeEmployee(Employee e);

    RegisterEntry checkIn(Employee e);

    RegisterEntry checkOut(Employee e);

    void updateEntry(RegisterEntry updated);

    void removeEntry(String entryId);

    void addPropertyChangeListener(PropertyChangeListener l);

    void removePropertyChangeListener(PropertyChangeListener l);
}
