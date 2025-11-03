package be.uantwerpen.sd.labs.lab5;

import be.uantwerpen.sd.labs.lab5.controller.Controller;
import be.uantwerpen.sd.labs.lab5.controller.RegistrationController;
import be.uantwerpen.sd.labs.lab5.database.Database;
import be.uantwerpen.sd.labs.lab5.database.RegistrationDB;
import be.uantwerpen.sd.labs.lab5.view.View;
import be.uantwerpen.sd.labs.lab5.viewfx.RegistrationView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        /*
            TODO: Create the model instance.
            TIP: Use the concrete DB implementation for this lab.
        */
        Database model = null;

        /*
            TODO: Inject the model into the controller.
            TIP: Pass the model to the controller constructor.
        */
        Controller controller = null;

        RegistrationView view = new RegistrationView();

        /*
            TODO: Wire the MVC triad: give the View its model + controller.
            TIP: The View should observe the model and call the controller.
        */
        View viewLogic = null;

        view.attachLogic(viewLogic);

        Scene scene = new Scene(view, 960, 560);
        stage.setTitle("Lab 5 — GoF MVC");
        stage.setScene(scene);
        stage.show();
    }

}
