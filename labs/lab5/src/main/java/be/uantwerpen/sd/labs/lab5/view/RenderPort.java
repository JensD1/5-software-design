package be.uantwerpen.sd.labs.lab5.view;

import be.uantwerpen.sd.labs.lab5.employee.Employee;
import be.uantwerpen.sd.labs.lab5.register_entry.RegisterEntry;

import java.util.List;

public interface RenderPort {
    void showPeople(List<Employee> people);

    void showEntries(List<RegisterEntry> entries);

    void clearInputs();

    void setActionsEnabled(boolean hasSelection);

    void showError(String message);
}
