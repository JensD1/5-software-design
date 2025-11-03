package be.uantwerpen.sd.labs.lab5.controller;

import be.uantwerpen.sd.labs.lab5.database.Database;
import be.uantwerpen.sd.labs.lab5.employee.Employee;
import be.uantwerpen.sd.labs.lab5.register_entry.RegisterEntry;

import java.time.LocalDateTime;

public class RegistrationController implements Controller {
    private final Database db;

    public RegistrationController(Database db) {
        this.db = db;
    }

    private static String norm(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    @Override
    public void addPerson(String name, String function) {
        /*
            TODO: Validate name and function (non-null and non-empty after trimming) and call the Database to add the new employee.
            TIP: Use the helper norm(String) to normalize inputs. On invalid input throw new IllegalArgumentException(\"Please provide both a name and a profession.\").
        */
        throw new UnsupportedOperationException("TODO: Validate name and function (non-null and non-empty after trimming) and call the Database to add the new employee.");
    }

    @Override
    public void updatePerson(Employee updated) {
        /*
            TODO: Validate name and function (non-null and non-empty after trimming) and construct a new Employee preserving the same id and call the Database to update.
            TIP: Use norm(String). Keep the employee id unchanged when creating the updated object.
        */
        throw new UnsupportedOperationException("TODO: Validate name and function (non-null and non-empty after trimming) and construct a new Employee preserving the same id and call the Database to update.");
    }

    @Override
    public void deletePerson(Employee e) {
        /*
            TODO: Call the Database to remove the given employee and let the model fire the necessary events.
            TIP: Call db.removeEmployee(e). The DB implementation will remove associated entries and fire events.
        */
        throw new UnsupportedOperationException("TODO: Call the Database to remove the given employee and let the model fire the necessary events.");
    }

    @Override
    public void checkIn(Employee e) {
        /*
            TODO: Call the Database checkIn method for the given employee.
            TIP: Do not write a try/catch; UI code will handle errors thrown by the DB.
        */
        throw new UnsupportedOperationException("TODO: Call the Database checkIn method for the given employee.");
    }

    @Override
    public void checkOut(Employee e) {
        /*
            TODO: Call the Database checkOut method for the given employee.
            TIP: Do not write a try/catch; UI code will handle errors thrown by the DB.
        */
        throw new UnsupportedOperationException("TODO: Call the Database checkOut method for the given employee.");
    }

    @Override
    public void updateEntry(String entryId, boolean checkIn, LocalDateTime timestamp) {
        /*
            TODO: Validate the timestamp (it must not be in the future); find the existing entry by id; create a new RegisterEntry with the updated values and call db.updateEntry(...).
            TIP: Use find() to find the existing entry. If timestamp.isAfter(LocalDateTime.now()) throw new IllegalArgumentException(\"Future timestamps are not allowed.\").
        */
        throw new UnsupportedOperationException("TODO: Validate the timestamp (it must not be in the future); find the existing entry by id; create a new RegisterEntry with the updated values and call db.updateEntry(...).");
    }

    @Override
    public void deleteEntry(String entryId) {
        /*
            TODO: Remove the entry identified by entryId using the Database.
            TIP: Do not write a try/catch; UI code will handle errors thrown by the DB.
        */
        throw new UnsupportedOperationException("TODO: Remove the entry identified by entryId using the Database.");
    }

    private RegisterEntry find(String id) {
        for (Employee e : db.getEmployees()) {
            for (RegisterEntry re : db.getEntriesFor(e)) {
                if (re.id().equals(id)) return re;
            }
        }
        throw new IllegalArgumentException("Entry not found: " + id);
    }
}
