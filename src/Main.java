import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by 4 on 11.03.2016.
 */
public class Main extends Application{

    static Stage parentStage;
    static Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        parentStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        scene = new Scene(root, 640, 640);
        scene.getStylesheets().add("mainStyle.css");
        parentStage.setResizable(false);
        parentStage.setScene(scene);
        parentStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
