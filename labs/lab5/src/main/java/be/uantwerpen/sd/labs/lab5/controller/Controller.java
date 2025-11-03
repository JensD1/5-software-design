package be.uantwerpen.sd.labs.lab5.controller;

import be.uantwerpen.sd.labs.lab5.employee.Employee;

import java.time.LocalDateTime;

public interface Controller {
    void addPerson(String name, String function);

    void updatePerson(Employee updated);

    void deletePerson(Employee e);

    void checkIn(Employee e);

    void checkOut(Employee e);

    void updateEntry(String entryId, boolean checkIn, LocalDateTime timestamp);

    void deleteEntry(String entryId);
}
