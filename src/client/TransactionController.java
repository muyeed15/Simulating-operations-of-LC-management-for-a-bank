package client;

import common.finder.Tree;
import common.lc.Transaction;
import common.prompt.Prompt;
import common.reader.Reader;
import common.sandwich.Sandwich;
import common.switcher.GUI;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Muyeed
 */
public class TransactionController implements Initializable {

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
    private ComboBox<String> filterComb;
    @FXML
    private TextField enEmail;
    @FXML
    private Button createID1;
    @FXML
    private TableView<Transaction> table;
    @FXML
    private TableColumn<Transaction, String> tid;
    @FXML
    private TableColumn<Transaction, String> tsend;
    @FXML
    private TableColumn<Transaction, String> trece;
    @FXML
    private TableColumn<Transaction, String> ttime;
    @FXML
    private TableColumn<Transaction, String> tdate;
    @FXML
    private TableColumn<Transaction, String> tstatus;

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
        ArrayList<ArrayList<String>> notFetch = (new Reader("Database/User/" + user + "/" + email, "notification.bin"))
                .splitFile('▓');
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
        
        // Transactions
        String[] filterList = {"All", "Pending", "Complete"};
        filterComb.getItems().addAll(filterList);
        filterComb.setValue("All");
        
        fetchTran();
    }
    
    private void fetchTran() {
        ArrayList<Transaction> TableTran= new ArrayList();
        ArrayList<String> fetchPI = (new Tree("Database/Official/TRANSACTION")).view();
        ArrayList<ArrayList<String>> fetchTemp = (new Reader("Database/User/" + user + "/" + email, "pi.bin")).splitFile('▓');

        for(String X: fetchPI){
            ArrayList<ArrayList<String>> fetchData = (new Reader("Database/Official/TRANSACTION",X)).splitFile('▓');
            String xserial = X.split("\\.")[0];
            String xemail = fetchData.get(0).get(1);
            String xmrcemail = fetchData.get(0).get(2);
            String xtime = fetchData.get(0).get(3);
            String xdate = fetchData.get(0).get(4);
            String xstatus = fetchData.get(0).get(5);

            for (ArrayList<String> Y: fetchTemp) {
                if (Y.get(0).equals(xserial)) {
                    if (filterComb.getValue().toLowerCase().equals("all") || (filterComb.getValue().toLowerCase().equals(xstatus.toLowerCase()))) {
                        if (enEmail.getText().isEmpty() || (-1 != (xmrcemail.toLowerCase()).indexOf(enEmail.getText().toLowerCase()))) {
                            TableTran.add(new Transaction(xserial, xemail, xmrcemail, xtime, xdate, xstatus));
                        }
                    }   
                }
            }
        }
        
        tid.setCellValueFactory(new PropertyValueFactory("id"));
        tsend.setCellValueFactory(new PropertyValueFactory("sender"));
        trece.setCellValueFactory(new PropertyValueFactory("reciever"));
        ttime.setCellValueFactory(new PropertyValueFactory("time"));
        tdate.setCellValueFactory(new PropertyValueFactory("date"));
        tstatus.setCellValueFactory(new PropertyValueFactory("status"));
        table.getItems().addAll(TableTran);
        
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

    }

    private void feedClick(MouseEvent event) {

    }

    private void settClick(MouseEvent event) {

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
    private void filterClick(MouseEvent event) {
        table.getItems().clear();
        fetchTran();
    }

    @FXML
    private void tranClick(MouseEvent event) {
        if (table.getSelectionModel().getSelectedItem().getReciever().equals(email) || table.getSelectionModel().getSelectedItem().getStatus().equals("Complete")) {
            (new Prompt()).getAlert("Transaction already completed!", "warning");
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateTransaction.fxml"));
            Parent root = loader.load();

            CreateTransactionController controller = loader.getController();
            controller.initData(user, email, sanData, table.getSelectionModel().getSelectedItem().getId());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("LC Bank Portal");

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
