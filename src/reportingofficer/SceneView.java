package reportingofficer;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**^
 *
 * @author Ishrak
 */
public class SceneView extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent root = loader.load();
        
        reportingofficer.DashboardController controller = loader.getController();
        String[] sanData = {"Dashboard", "Contact", "Notification", "Reports", "Settings", "Feedback"};
        controller.initData("REPORTINGOFFICER", "ishrak@lc.ro.com", sanData);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        stage.setTitle("LC Bank Portal");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
