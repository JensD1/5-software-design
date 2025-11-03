package be.uantwerpen.sd.labs.lab5.view;

import be.uantwerpen.sd.labs.lab5.controller.Controller;
import be.uantwerpen.sd.labs.lab5.database.Database;
import be.uantwerpen.sd.labs.lab5.database.RegistrationEventType;
import be.uantwerpen.sd.labs.lab5.employee.Employee;
import be.uantwerpen.sd.labs.lab5.register_entry.RegisterEntry;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.List;

public final class View implements PropertyChangeListener {
    private final Database model;
    private final Controller controller;
    private final RenderPort ui;
    private Employee currentSelection;
    private RegisterEntry currentEntrySelection;

    public View(Database model, Controller controller, RenderPort ui) {
        /*
            TODO: Implement the Constructor and register this View as a PropertyChangeListener on the model and refresh the UI.
            TIP: Assign all input variables to the class fields. Execute the addPropertyChangeListener of the model. Call refreshAll() at the end.
        */
        throw new UnsupportedOperationException("TODO: Implement the Constructor and register this View as a PropertyChangeListener on the model and refresh the UI.");
    }

    public void onAddPerson(String name, String function) {
        /*
            TODO: Implement the onAddPerson handler: call the controller to add the person and clear the UI on success; show an error on IllegalArgumentException.
            TIP: Everything should be in one big try/catch block. In the catch statement, use `ui.showError(ex.getMessage());`.
        */
        throw new UnsupportedOperationException("TODO: Implement the onAddPerson handler: call the controller to add the person and clear the UI on success; show an error on IllegalArgumentException.");
    }

    public void onUpdatePerson(Employee updated) {
        /*
            TODO: Implement the onUpdatePerson handler: call the controller to update the person; show an error on IllegalArgumentException.
            TIP: Everything should be in one big try/catch block. In the catch statement, use `ui.showError(ex.getMessage());`.
        */
        throw new UnsupportedOperationException("TODO: Implement the onUpdatePerson handler: call the controller to update the person; show an error on IllegalArgumentException.");
    }

    public void onDeleteSelected(Employee sel) {
        /*
            TODO: Implement the onDeleteSelected handler: call the controller to delete the selected person.
            TIP: The selected employee should only be deleted if it is not null. There should NOT be a try/catch statement here, only one if statement.
        */
        throw new UnsupportedOperationException("TODO: Implement the onDeleteSelected handler: call the controller to delete the selected person.");
    }

    public void onAddEntry(Employee sel) {
        if (sel == null) return;
        try {
            List<RegisterEntry> hist = model.getEntriesFor(sel);
            RegisterEntry last = hist.stream().max(Comparator.comparing(RegisterEntry::timestamp)).orElse(null);
            boolean nextIsCheckIn = (last == null) || !last.checkIn();
            if (nextIsCheckIn) controller.checkIn(sel);
            else controller.checkOut(sel);
        } catch (IllegalStateException ex) {
            ui.showError(ex.getMessage());
        }
    }

    public void onSelectionChanged(Employee sel) {
        currentSelection = sel;
        ui.setActionsEnabled(sel != null);
        ui.showEntries(sel == null ? List.of() : model.getEntriesFor(sel));
        currentEntrySelection = null;
    }

    public void onEntrySelected(RegisterEntry entry) {
        currentEntrySelection = entry;
    }

    public void onDeleteEntrySelected() {
        if (currentEntrySelection != null) {
            controller.deleteEntry(currentEntrySelection.id());
            currentEntrySelection = null;
        }
    }

    public void onEditEntrySelected(boolean newCheckIn, java.time.LocalDateTime newTimestamp) {
        if (currentEntrySelection != null) {
            try {
                controller.updateEntry(currentEntrySelection.id(), newCheckIn, newTimestamp);
            } catch (IllegalArgumentException ex) {
                ui.showError(ex.getMessage());
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object nv = evt.getNewValue();
        if (!(nv instanceof RegistrationEventType re)) return;
        if (re == RegistrationEventType.EMPLOYEE_ADDED
                || re == RegistrationEventType.EMPLOYEE_REMOVED
                || re == RegistrationEventType.EMPLOYEE_UPDATED) {
            ui.showPeople(model.getEmployees());
            if (currentSelection != null) {
                ui.showEntries(model.getEntriesFor(currentSelection));
            }
        } else if (re == RegistrationEventType.ENTRY_ADDED
                || re == RegistrationEventType.ENTRY_UPDATED
                || re == RegistrationEventType.ENTRY_REMOVED) {
            if (currentSelection != null) {
                ui.showEntries(model.getEntriesFor(currentSelection));
            } else {
                ui.showEntries(List.of());
            }
        }
    }

    private void refreshAll() {
        ui.showPeople(model.getEmployees());
        ui.setActionsEnabled(false);
    }
}
