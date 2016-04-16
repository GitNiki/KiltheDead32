import javafx.event.ActionEvent;

/**
 * Created by 4 on 11.03.2016.
 */
public class MainController {

    public void newFame(ActionEvent actionEvent) {
       Main2 m = new Main2();
        m.start(Main.parentStage);

    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
