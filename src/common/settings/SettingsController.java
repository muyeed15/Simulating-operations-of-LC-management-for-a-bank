package common.settings;

import common.aes.AES;
import common.notification.*;
import common.reader.Reader;
import common.sandwich.Sandwich;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javax.imageio.ImageIO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Collections;

// controllers
import common.message.MessageController;
import common.prompt.Prompt;
import common.switcher.GUI;
import common.writer.Writer;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author Muyeed
 */
public class SettingsController implements Initializable {

    private TableView<Notification> table;
    private TableColumn<Notification, String> ntable;
    private TableColumn<Notification, String> utable;
    private TableColumn<Notification, String> ttable;
    private TableColumn<Notification, String> dtable;
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

    private String user;
    private String email;
    private String[] sanData;
    private ArrayList<Notification> notList;
    @FXML
    private Circle mdot;
    @FXML
    private Circle ndot;
    @FXML
    private ImageView imageUser2;
    @FXML
    private Label labName2;
    @FXML
    private PasswordField enNewPassword;
    @FXML
    private PasswordField enConfirmPassword;
    @FXML
    private PasswordField enCurrentPassword;

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

        for (String x : sanData) {
            sanList.add(new Sandwich(x));
        }
        dtableSide.setCellValueFactory(new PropertyValueFactory("item"));
        tableSide.getItems().setAll(FXCollections.observableArrayList(sanList));

        // Show Panel
        paneSide.setVisible(false);
        paneLog.setVisible(false);
        ArrayList<ArrayList<String>> proFetch = (new Reader("Database/User/" + user + "/" + email, "profile.bin"))
                .splitFile('▓');
        ArrayList<String> data = proFetch.get(0);
        labName.setText(data.get(0));
        labName2.setText(data.get(0));

        // image
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File("Database/User/" + user + "/" + email + "/user.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
        imageUser.setImage(fxImage);
        imageUser2.setImage(fxImage);

        // Notification
        notList = new ArrayList();
        ArrayList<ArrayList<String>> notFetch = (new Reader("Database/User/" + user + "/" + email, "notification.bin"))
                .splitFile('▓');
        Collections.reverse(notFetch);
        for (ArrayList<String> x : notFetch) {
            notList.add(new Notification(x.get(0), x.get(1), x.get(2), x.get(3)));
        }

        // dot
        ArrayList<ArrayList<String>> mesFetch = (new Reader("Database/User/" + user + "/" + email, "message.bin"))
                .splitFile('▓');
        ArrayList<ArrayList<String>> dotFetch = (new Reader("Database/User/" + user + "/" + email, "dot.bin"))
                .splitFile('▓');

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
                (new GUI(user, email, sanData)).dashClick(event);
                break;
            case "Application":
                (new GUI(user, email, sanData)).applcClick(event);
                break;
            case "Transaction":
                (new GUI(user, email, sanData)).transClick(event);
                break;
            case "Invoice":
                (new GUI(user, email, sanData)).ivcClick(event);
                break;
            case "Switch Account":
                (new GUI(user, email, sanData)).swtClick(event);
                break;
            case "Settings":
                (new GUI(user, email, sanData)).sttClick(event);
                break;
            case "Policy":
                (new GUI(user, email, sanData)).pcyClick(event);
                break;
            case "Policy Management":
                (new GUI(user, email, sanData)).pcyClick(event);
                break;
            case "Feedback":
                (new GUI(user, email, sanData)).feedClick(event);
                break;
            case "Merchandise":
                (new GUI(user, email, sanData)).mrcDiseClick(event);
                break;
            case "Advertising":
                (new GUI(user, email, sanData)).advClick(event);
                break;
            case "Requests":
                (new GUI(user, email, sanData)).reqClick(event);
                break;
            case "History":
                (new GUI(user, email, sanData)).hisClick(event);
                break;
            case "Clients":
                (new GUI(user, email, sanData)).cliClick(event);
                break;
            case "Merchants":
                (new GUI(user, email, sanData)).mrcClick(event);
                break;
            case "Analytics":
                (new GUI(user, email, sanData)).anlClick(event);
                break;
            case "Risk Assessment":
                (new GUI(user, email, sanData)).riskClick(event);
                break;
            case "L\\C Application":
                (new GUI(user, email, sanData)).applcClick(event);
                break;
            case "Logs":
                (new GUI(user, email, sanData)).logUserClick(event);
                break;
            case "Management":
                (new GUI(user, email, sanData)).mgtClick(event);
                break;
            case "Monitoring":
                (new GUI(user, email, sanData)).monClick(event);
                break;
            case "Backup":
                (new GUI(user, email, sanData)).bkpClick(event);
                break;
            case "Relationship":
                (new GUI(user, email, sanData)).rlnClick(event);
                break;
            case "Reports":
                (new GUI(user, email, sanData)).rptClick(event);
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
            ArrayList<ArrayList<String>> notFetch = (new Reader("Database/User/" + user + "/" + email,
                    "notification.bin")).splitFile('▓');
            ArrayList<ArrayList<String>> dotFetch = (new Reader("Database/User/" + user + "/" + email, "dot.bin"))
                    .splitFile('▓');
            String notNum = dotFetch.get(0).get(0) + "▓" + notFetch.size() + "▓";
            new Writer("Database/User/" + user + "/" + email, "dot.bin", notNum).writeFile();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/common/notification/NotificationFXML.fxml"));
            Parent root = loader.load();

            NotificationController controller = loader.getController();
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
            ArrayList<ArrayList<String>> mesFetch = (new Reader("Database/User/" + user + "/" + email, "message.bin"))
                    .splitFile('▓');
            ArrayList<ArrayList<String>> dotFetch = (new Reader("Database/User/" + user + "/" + email, "dot.bin"))
                    .splitFile('▓');
            String mesNum = mesFetch.size() + "▓" + dotFetch.get(0).get(1) + "▓";
            new Writer("Database/User/" + user + "/" + email, "dot.bin", mesNum).writeFile();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/common/message/MessageFXML.fxml"));
            Parent root = loader.load();

            MessageController controller = loader.getController();
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
    private void saveClick(MouseEvent event) {
        int length = enNewPassword.getText().length();
        
        if (enCurrentPassword.getText().isEmpty() || enNewPassword.getText().isEmpty() || enConfirmPassword.getText().isEmpty()) {
            (new Prompt()).getAlert("Fields cannot be empty!", "error");
            return;
        }
        
        if (length > 7 && length < 17) {
        } else {
            (new Prompt()).getAlert("New password must contain 8-16 charecters!", "error");
            return;
        }
        
        if (enNewPassword.getText().equals(enConfirmPassword.getText())) {
        } else {
            (new Prompt()).getAlert("Confirm password does not match the new password!", "error");
            return;
        }
        
        ArrayList <ArrayList<String>> proFetch = (new Reader("Database/User/" + user + "/" + email, "profile.bin")).splitFile('▓');
        ArrayList <String> data = proFetch.get(0);
        String password = AES.decrypt(data.get(1));
        
        if (password.equals(enNewPassword.getText())) {
            (new Prompt()).getAlert("This password is already in use!", "error");
            return;
        }

        if (!password.equals(enCurrentPassword.getText())) {
            (new Prompt()).getAlert("Current password does not match!", "error");
        } else {
            String getNewPassword = enNewPassword.getText();
            String newPassword = AES.encrypt(getNewPassword);
            String newData = proFetch.get(0).get(0) + "▓" + newPassword + "▓" + proFetch.get(0).get(2) + "▓" + proFetch.get(0).get(3) + "▓" + proFetch.get(0).get(4) + "▓" + proFetch.get(0).get(5) + "▓" + proFetch.get(0).get(6) + "▓" + proFetch.get(0).get(7) + "▓" + proFetch.get(0).get(8) + "▓";
            new Writer("Database/User/" + user + "/" + email, "profile.bin", newData).writeFile();
            (new Prompt()).getAlert("Password updated!", "information");
        }
        
    }

}
