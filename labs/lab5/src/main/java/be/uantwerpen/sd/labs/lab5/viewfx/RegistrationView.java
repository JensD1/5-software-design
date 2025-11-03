package be.uantwerpen.sd.labs.lab5.viewfx;

import be.uantwerpen.sd.labs.lab5.employee.Employee;
import be.uantwerpen.sd.labs.lab5.register_entry.RegisterEntry;
import be.uantwerpen.sd.labs.lab5.view.RenderPort;
import be.uantwerpen.sd.labs.lab5.view.View;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RegistrationView extends BorderPane implements RenderPort {
    private final ListView<Employee> peopleList = new ListView<>();
    private final ListView<RegisterEntry> entryList = new ListView<>();

    private final Button addPersonButton = new Button("Add");
    private final Button editPersonButton = new Button("Edit");
    private final Button deletePersonButton = new Button("Delete");
    private final Button addEntryButton = new Button("Add Entry");

    private final Button editEntryButton = new Button("Edit Entry");
    private final Button deleteEntryButton = new Button("Delete Entry");

    private final VBox formBox = new VBox(8);
    private final TextField nameField = new TextField();
    private final ComboBox<String> functionBox = new ComboBox<>();

    private View logic;

    public RegistrationView() {
        setPadding(new Insets(12));

        Label leftTitle = new Label("People");
        VBox left = new VBox(8, leftTitle, peopleList);
        left.setPadding(new Insets(8));
        setLeft(left);

        Label centerTitle = new Label("Entries");
        HBox centerHeader = new HBox(8, centerTitle, addEntryButton, editEntryButton, deleteEntryButton);
        VBox center = new VBox(8, centerHeader, entryList);
        center.setPadding(new Insets(8));
        setCenter(center);

        HBox actions = new HBox(8, addPersonButton, editPersonButton, deletePersonButton);
        actions.setPadding(new Insets(8));

        buildPersonForm();

        VBox right = new VBox(12, actions, formBox);
        right.setPadding(new Insets(8));
        setRight(right);

        peopleList.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (logic != null) logic.onSelectionChanged(newV);
            toggleForm(false);
        });

        entryList.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            boolean has = newV != null;
            editEntryButton.setDisable(!has);
            deleteEntryButton.setDisable(!has);
            if (logic != null && newV != null) logic.onEntrySelected(newV);
        });

        addPersonButton.setOnAction(e -> toggleForm(true));
        editPersonButton.setOnAction(e -> {
            Employee sel = peopleList.getSelectionModel().getSelectedItem();
            if (sel != null) showEditPersonDialog(sel);
        });
        deletePersonButton.setOnAction(e -> {
            Employee sel = peopleList.getSelectionModel().getSelectedItem();
            if (logic != null) logic.onDeleteSelected(sel);
        });

        addEntryButton.setOnAction(e -> {
            Employee sel = peopleList.getSelectionModel().getSelectedItem();
            if (logic != null) logic.onAddEntry(sel);
        });

        editEntryButton.setOnAction(e -> {
            RegisterEntry sel = entryList.getSelectionModel().getSelectedItem();
            if (sel != null) showEditEntryDialog(sel);
        });
        deleteEntryButton.setOnAction(e -> {
            if (logic != null) logic.onDeleteEntrySelected();
        });

        peopleList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Employee e, boolean empty) {
                super.updateItem(e, empty);
                if (empty || e == null) {
                    setText(null);
                    setOnMouseClicked(ev -> peopleList.getSelectionModel().clearSelection());
                    return;
                }
                setText(e.name() + " (" + e.function() + ")");
                setOnMouseClicked(null);
            }
        });

        entryList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(RegisterEntry re, boolean empty) {
                super.updateItem(re, empty);
                if (empty || re == null) {
                    setText(null);
                    setOnMouseClicked(ev -> entryList.getSelectionModel().clearSelection());
                    return;
                }
                String kind = re.checkIn() ? "Check In" : "Check Out";
                String ts = re.timestamp().toString().replace('T', ' ').split("\\.")[0];
                setText(kind + " — " + ts);
                setOnMouseClicked(null);
            }
        });

        toggleForm(false);
        addEntryButton.setDisable(true);
        editEntryButton.setDisable(true);
        deleteEntryButton.setDisable(true);

        addBackgroundDeselect(peopleList);
        addBackgroundDeselect(entryList);
    }

    public void attachLogic(View logic) {
        this.logic = logic;
    }

    private void buildPersonForm() {
        ObservableList<String> professions = FXCollections.observableArrayList(
                "Programmer", "Designer", "Manager", "Tester", "DevOps", "Data Scientist"
        );
        functionBox.setItems(professions);
        functionBox.setEditable(false);

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.add(new Label("Name"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Profession"), 0, 1);
        grid.add(functionBox, 1, 1);

        Button save = new Button("Save");
        Button cancel = new Button("Cancel");

        save.setOnAction(e -> {
            if (logic != null) logic.onAddPerson(nameField.getText(), functionBox.getValue());
        });

        cancel.setOnAction(e -> {
            nameField.clear();
            functionBox.getSelectionModel().clearSelection();
            toggleForm(false);
        });

        HBox buttons = new HBox(8, save, cancel);
        formBox.getChildren().setAll(new Label("Add Person"), grid, buttons);
    }

    private void showEditPersonDialog(Employee sel) {
        Dialog<Employee> dlg = new Dialog<>();
        dlg.setTitle("Edit Person");
        TextField name = new TextField(sel.name());
        ComboBox<String> fn = new ComboBox<>(functionBox.getItems());
        fn.getSelectionModel().select(sel.function());
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        gp.add(new Label("Name"), 0, 0);
        gp.add(name, 1, 0);
        gp.add(new Label("Profession"), 0, 1);
        gp.add(fn, 1, 1);
        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dlg.setResultConverter(bt -> {
            if (bt == ButtonType.OK) return new Employee(sel.id(), name.getText(), fn.getValue());
            return null;
        });
        dlg.showAndWait().ifPresent(e -> {
            if (logic != null) logic.onUpdatePerson(e);
        });
    }

    private void showEditEntryDialog(RegisterEntry sel) {
        Dialog<RegisterEntry> dlg = new Dialog<>();
        dlg.setTitle("Edit Entry");
        CheckBox checkIn = new CheckBox("Is Check-In");
        DatePicker date = new DatePicker(sel.timestamp().toLocalDate());
        TextField time = new TextField(sel.timestamp().toLocalTime().withSecond(0).withNano(0).toString());
        checkIn.setSelected(sel.checkIn());
        date.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) return;
                setDisable(item.isAfter(LocalDate.now()));
            }
        });
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        gp.add(checkIn, 0, 0, 2, 1);
        gp.add(new Label("Date"), 0, 1);
        gp.add(date, 1, 1);
        gp.add(new Label("Time (HH:MM)"), 0, 2);
        gp.add(time, 1, 2);
        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button ok = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        ok.addEventFilter(javafx.event.ActionEvent.ACTION, ev -> {
            String[] hm = time.getText() == null ? new String[]{"00", "00"} : time.getText().split(":");
            int h = hm.length > 0 ? Integer.parseInt(hm[0]) : 0;
            int m = hm.length > 1 ? Integer.parseInt(hm[1]) : 0;
            LocalDate d = date.getValue();
            LocalTime t = sel.timestamp().toLocalTime().withHour(h).withMinute(m);
            LocalDateTime ts = LocalDateTime.of(d, t);
            if (ts.isAfter(LocalDateTime.now())) {
                new Alert(Alert.AlertType.ERROR, "Future timestamps are not allowed.").showAndWait();
                ev.consume();
            }
        });
        dlg.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return null;
            String[] hm = time.getText() == null ? new String[]{"00", "00"} : time.getText().split(":");
            int h = hm.length > 0 ? Integer.parseInt(hm[0]) : 0;
            int m = hm.length > 1 ? Integer.parseInt(hm[1]) : 0;
            LocalDate d = date.getValue();
            LocalTime t = sel.timestamp().toLocalTime().withHour(h).withMinute(m);
            LocalDateTime ts = LocalDateTime.of(d, t);
            return sel.withCheckIn(checkIn.isSelected()).withTimestamp(ts);
        });
        dlg.showAndWait().ifPresent(updated -> {
            if (logic != null) logic.onEditEntrySelected(updated.checkIn(), updated.timestamp());
        });
    }

    private void toggleForm(boolean show) {
        for (Node n : formBox.getChildren()) n.setDisable(!show);
        formBox.setVisible(show);
        formBox.setManaged(show);
    }

    private void addBackgroundDeselect(ListView<?> lv) {
        lv.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            Node n = e.getPickResult().getIntersectedNode();
            while (n != null && n != lv && !(n instanceof ListCell)) n = n.getParent();
            if (n == lv || n == null) {
                lv.getSelectionModel().clearSelection();
                e.consume();
            }
        });
    }

    @Override
    public void showPeople(java.util.List<Employee> people) {
        Platform.runLater(() -> {
            Employee prev = peopleList.getSelectionModel().getSelectedItem();
            peopleList.getItems().setAll(people);
            if (prev != null) {
                for (Employee e : people) {
                    if (prev.equals(e)) {
                        peopleList.getSelectionModel().select(e);
                        break;
                    }
                }
            }
            if (logic != null) {
                logic.onSelectionChanged(peopleList.getSelectionModel().getSelectedItem());
            }
        });
    }

    @Override
    public void showEntries(java.util.List<RegisterEntry> entries) {
        javafx.application.Platform.runLater(() -> {
            java.util.List<RegisterEntry> sorted = new java.util.ArrayList<>(entries);
            sorted.sort(java.util.Comparator.comparing(RegisterEntry::timestamp));
            entryList.getItems().setAll(sorted);
        });
    }

    @Override
    public void clearInputs() {
        Platform.runLater(() -> {
            nameField.clear();
            functionBox.getSelectionModel().clearSelection();
            toggleForm(false);
        });
    }

    @Override
    public void setActionsEnabled(boolean hasSelection) {
        Platform.runLater(() -> {
            editPersonButton.setDisable(!hasSelection);
            deletePersonButton.setDisable(!hasSelection);
            addEntryButton.setDisable(!hasSelection);
            boolean hasEntry = entryList.getSelectionModel().getSelectedItem() != null;
            editEntryButton.setDisable(!hasEntry);
            deleteEntryButton.setDisable(!hasEntry);
        });
    }

    @Override
    public void showError(String message) {
        Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, message).showAndWait());
    }
}
