package generalmanager;

import common.finder.UserList;
import common.reader.Reader;
import common.sandwich.Sandwich;
import common.user.User;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import common.writer.Writer;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Muyeed
 */
public class MerchantsController implements Initializable {

    @FXML
    private AnchorPane paneSide;
    @FXML
    private TableView<Sandwich> tableSide;
    @FXML
    private TableColumn<Sandwich, String> dtableSide;
    @FXML
    private AnchorPane paneLog;
    @FXML
    private Label labName;
    @FXML
    private ImageView imageUser;
    @FXML
    private Circle mdot;
    @FXML
    private Circle ndot;
    
    private String user;
    private String email;
    private String[] sanData;
    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, String> ttype;
    @FXML
    private TableColumn<User, String> temail;
    @FXML
    private TextField enEmail;
    @FXML
    private TableColumn<User, String> tname;
    @FXML
    private TableColumn<User, String> tphone;
    @FXML
    private TableColumn<User, String> tstate;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    // pipeline
    public void initData(String user, String email, String[] sanData) {
        // append
        this.user = user;
        this.email = email;
        this.sanData = sanData;
        
        // Side Panel
        ArrayList<Sandwich> sanList = new ArrayList();

        for (String x: sanData) {
            sanList.add(new Sandwich(x));
        }
        dtableSide.setCellValueFactory(new PropertyValueFactory("item"));
        tableSide.getItems().setAll(FXCollections.observableArrayList(sanList));
        
        // Show Panel
        paneSide.setVisible(false);
        paneLog.setVisible(false);
        ArrayList <ArrayList<String>> proFetch = (new Reader("Database/User/" + user + "/" + email, "profile.bin")).splitFile('▓');
        ArrayList <String> data = proFetch.get(0);
        labName.setText(data.get(0));
        
        // image
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(new File("Database/User/" + user + "/" + email + "/user.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (originalImage != null) {
            int targetWidth = 50;
            int targetHeight = 50;

            BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
            g2d.dispose();

            Image fxImage = SwingFXUtils.toFXImage(resizedImage, null);

            imageUser.setImage(fxImage);
        }
        
        // dot
        ArrayList <ArrayList<String>> notFetch = (new Reader("Database/User/" + user + "/" + email, "notification.bin")).splitFile('▓');
        ArrayList <ArrayList<String>> mesFetch = (new Reader("Database/User/" + user + "/" + email, "message.bin")).splitFile('▓');
        ArrayList <ArrayList<String>> dotFetch = (new Reader("Database/User/" + user + "/" + email, "dot.bin")).splitFile('▓');
        
        if (mesFetch.size() != Integer.parseInt(dotFetch.get(0).get(0))) {
            mdot.setVisible(true);
        } else {
            mdot.setVisible(false);
        }
        
        if (notFetch.size() != Integer.parseInt(dotFetch.get(0).get(1))) {
            ndot.setVisible(true);
        } else {
            ndot.setVisible(false);
        }
        
        // table
        ttype.setCellValueFactory(new PropertyValueFactory("type"));
        tname.setCellValueFactory(new PropertyValueFactory("name"));
        tphone.setCellValueFactory(new PropertyValueFactory("phone"));
        temail.setCellValueFactory(new PropertyValueFactory("email"));
        tstate.setCellValueFactory(new PropertyValueFactory("state"));
        
        table.getItems().setAll((new UserList()).getFilterList("Merchant", enEmail.getText()));
    }

    @FXML
    private void sandAction(MouseEvent event) {
        if (paneSide.isVisible()) {
            paneSide.setVisible(false);
        } else {
            paneSide.setVisible(true);
        }
    }

    @FXML
    private void windowClick(MouseEvent event) {
        Sandwich window = tableSide.getSelectionModel().getSelectedItem();
        switch (window.getItem()) {
            case "Notification":
                notClick(event);
                break;
            case "Contact":
                mailClick(event);
                break;
            case "Dashboard":
                dashClick(event);
                break;
            case "Settings":
                settClick(event);
                break;
            case "Feedback":
                feedClick(event);
                break;
            case "Requests":
                reqClick(event);
                break;
            case "History":
                hisClick(event);
                break;
            case "Clients":
                cliClick(event);
                break;
            case "Merchants":
                mrcClick(event);
                break;
            default:
                break;
        }
    }

    @FXML
    private void logClick(MouseEvent event) {
        if (paneLog.isVisible()) {
            paneLog.setVisible(false);
        } else {
            paneLog.setVisible(true);
        }
    }

    @FXML
    private void notClick(MouseEvent event) {
        if (ndot.isVisible() == true) {
            ArrayList <ArrayList<String>> notFetch = (new Reader("Database/User/" + user + "/" + email, "notification.bin")).splitFile('▓');
            ArrayList <ArrayList<String>> dotFetch = (new Reader("Database/User/" + user + "/" + email, "dot.bin")).splitFile('▓');
            String notNum = dotFetch.get(0).get(0) + "▓" + notFetch.size() + "▓";
            new Writer("Database/User/" + user + "/" + email, "dot.bin", notNum).writeFile();
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/common/notification/NotificationFXML.fxml"));
            Parent root = loader.load();

            common.notification.NotificationController controller = loader.getController();
            controller.initData(user, email, sanData);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("LC Bank Portal");

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void mailClick(MouseEvent event) {
        if (mdot.isVisible() == true) {
            ArrayList <ArrayList<String>> mesFetch = (new Reader("Database/User/" + user + "/" + email, "message.bin")).splitFile('▓');
            ArrayList <ArrayList<String>> dotFetch = (new Reader("Database/User/" + user + "/" + email, "dot.bin")).splitFile('▓');
            String mesNum = mesFetch.size() + "▓" + dotFetch.get(0).get(1) + "▓";
            new Writer("Database/User/" + user + "/" + email, "dot.bin", mesNum).writeFile();
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/common/message/MessageFXML.fxml"));
            Parent root = loader.load();

            common.message.MessageController controller = loader.getController();
            controller.initData(user, email, sanData);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("LC Bank Portal");

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void dashClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.initData(user, email, sanData);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("LC Bank Portal");

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void feedClick(MouseEvent event) {
        
    }
    
    private void settClick(MouseEvent event) {
        
    }
    
    private void reqClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Requests.fxml"));
            Parent root = loader.load();

            RequestsController controller = loader.getController();
            controller.initData(user, email, sanData);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("LC Bank Portal");

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void hisClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Requests.fxml"));
            Parent root = loader.load();

            RequestsController controller = loader.getController();
            controller.initData(user, email, sanData);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("LC Bank Portal");

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void cliClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Clients.fxml"));
            Parent root = loader.load();

            ClientsController controller = loader.getController();
            controller.initData(user, email, sanData);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("LC Bank Portal");

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void mrcClick(MouseEvent event) {
        
    }

    @FXML
    private void outClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/common/signIN/SignINFXML.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("LC Bank Portal");

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void userCLick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Viewer.fxml"));
            Parent root = loader.load();

            ViewerController controller = loader.getController();
            
            String xuser = table.getSelectionModel().getSelectedItem().getType();
            String xname = table.getSelectionModel().getSelectedItem().getName();
            String xemail = table.getSelectionModel().getSelectedItem().getEmail();
            String xphone = table.getSelectionModel().getSelectedItem().getPhone();
            String xaddress = table.getSelectionModel().getSelectedItem().getAddress();
            String xstatus = table.getSelectionModel().getSelectedItem().getState();
            
            controller.initData(user, email, sanData, xuser, xname, xemail, xphone, xaddress, xstatus);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("LC Bank Portal");

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void filterClick(MouseEvent event) {
        table.getItems().setAll((new UserList()).getFilterList("Merchant", enEmail.getText()));
    }
    
}
