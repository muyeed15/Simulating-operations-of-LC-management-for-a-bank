package lcofficer;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import common.finder.Tree;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Muyeed
 */
public class LcApplicationController implements Initializable {

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
    private TextField amWordTxt;
    @FXML
    private TextField appAddressTxt;
    @FXML
    private TextField appCompTxt;
    @FXML
    private TextField beneCompTxt;
    @FXML
    private TextField BeneEmailTxt;
    @FXML
    private TextField appEmailTxt;
    @FXML
    private TextField appconTxt;
    @FXML
    private TextField appTellTxt;
    @FXML
    private CheckBox appLichk;
    @FXML
    private CheckBox benefTxt;
    @FXML
    private TextField amountTxt;
    @FXML
    private TextField issuBankTxt;
    @FXML
    private TextField exLcNoTxt;
    @FXML
    private DatePicker exDateTxt;
    @FXML
    private TextField redTxt;
    @FXML
    private TextField beneConTxt;
    @FXML
    private TextField beneTellTxt;
    @FXML
    private CheckBox yesChk;
    @FXML
    private CheckBox psPerChk;
    @FXML
    private CheckBox transPerChk;
    @FXML
    private Button ProceedButt;
    @FXML
    private TextField benAddressTxt;
    @FXML
    private ComboBox<String> LcNoComb;

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
        
        // lc
        ArrayList<String> lcList = new Tree("Database/Official/LC").view();
        
        for (String X: lcList) {
            if ((new Reader("Database/Official/LC/", X)).splitFile('▓').get(0).get(4).equals("Pending")) {
                LcNoComb.getItems().add(X.split("\\.")[0]);
            }
        }
        
        LcNoComb.setValue("Select");
        
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
    private void proceedClick(MouseEvent event) {
        if (appCompTxt.getText().isEmpty() || appAddressTxt.getText().isEmpty() || appTellTxt.getText().isEmpty()
                || appEmailTxt.getText().isEmpty() || beneCompTxt.getText().isEmpty() || benAddressTxt.getText().isEmpty()
                || BeneEmailTxt.getText().isEmpty() || beneTellTxt.getText().isEmpty() || amountTxt.getText().isEmpty()
                || issuBankTxt.getText().isEmpty() || exLcNoTxt.getText().isEmpty() || exDateTxt.getValue() == null
                || redTxt.getText().isEmpty() || beneConTxt.getText().isEmpty() || LcNoComb.getValue().equals("Select")) {
            
            (new Prompt()).getAlert("Please fill in all fields.", "error");
            return;
        }
        
        try {
            PdfWriter write = new PdfWriter(new FileOutputStream("Database/Official/LC_PDF/" + LcNoComb.getValue() + ".pdf"));
            PdfDocument pdf = new PdfDocument(write);
            Document document = new Document(pdf);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a, dd/MM/yyyy");
            Paragraph timeAndDate = new Paragraph(now.format(formatter));
            timeAndDate.setFontSize(8);
            document.add(timeAndDate.setTextAlignment(TextAlignment.RIGHT));
            
            document.add(new Paragraph("Letter Of Credit").setFontSize(20).setBold().setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("LC Bank").setFontSize(12).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("House no. 6, Road 18, Block B Mirpur 10 Roundabout, Dhaka 1216").setFontSize(12).setTextAlignment(TextAlignment.CENTER));
            
            document.add(new Paragraph("\nLC Number:"));
            document.add(new Paragraph(LcNoComb.getValue()).setFontSize(8));
            
            document.add(new Paragraph("\nApplicant:"));
            document.add(new Paragraph(appCompTxt.getText()).setFontSize(8));
            document.add(new Paragraph("Email: " + appEmailTxt.getText()).setFontSize(8));
            document.add(new Paragraph("Tell: " + appTellTxt.getText()).setFontSize(8));
            document.add(new Paragraph("Address: " + appAddressTxt.getText()).setFontSize(8));
            document.add(new Paragraph("Country: " + appconTxt.getText()).setFontSize(8));

            document.add(new Paragraph("\nBeneficiary:"));
            document.add(new Paragraph(beneCompTxt.getText()).setFontSize(8));
            document.add(new Paragraph("Email: " + BeneEmailTxt.getText()).setFontSize(8));
            document.add(new Paragraph("Tell: " + beneTellTxt.getText()).setFontSize(8));
            document.add(new Paragraph("Address: " + benAddressTxt.getText()).setFontSize(8));
            document.add(new Paragraph("Country: " + appconTxt.getText()).setFontSize(8));

            document.add(new Paragraph("\nReferral Number:"));
            document.add(new Paragraph(redTxt.getText()).setFontSize(8));
            
            document.add(new Paragraph("Terms and Conditions:"));
            document.add(new Paragraph("1. This Letter of Credit is subject to the Uniform Customs and Practice for Documentary Credits (UCP 600).").setFontSize(8));
            document.add(new Paragraph("2. Partial shipments are " + (psPerChk.isSelected() ? "allowed" : "not allowed") + ".").setFontSize(8));
            document.add(new Paragraph("3. Transshipment is " + (transPerChk.isSelected() ? "allowed" : "not allowed") + ".").setFontSize(8));
            document.add(new Paragraph("4. All banking charges outside of the applicant's address are for the beneficiary's account.").setFontSize(8));
            
            // image
            try {
                com.itextpdf.layout.element.Image signatureImage = new com.itextpdf.layout.element.Image(ImageDataFactory.create("src/common/iconFiles/signature.png"));
                signatureImage.setWidth(70);
                signatureImage.setHeight(20);
                signatureImage.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                document.add(signatureImage);

                Paragraph signature = new Paragraph("Approved By: " + "____________");
                signature.setFontSize(12);
                signature.setTextAlignment(TextAlignment.RIGHT);
                document.add(signature);
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
            
            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("hh:mm a");

            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            String notData = ("Your Letter of Credit (" + LcNoComb.getValue() + ") has arrived in the invoice!") + "▓" + "LC" + "▓"
            + currentTime.format(formatTime) + "▓" + currentDate.format(formatDate);

            new Writer("Database/User/CLIENT/"+ appEmailTxt.getText(), "notification.bin", notData).overWriteFile();
            new Writer("Database/User/MERCHANT/"+ BeneEmailTxt.getText(), "notification.bin", notData).overWriteFile();
            
            ArrayList<ArrayList<String>> lcFtech = (new Reader("Database/Official/LC", LcNoComb.getValue() + ".bin")).splitFile('▓');
            new Writer("Database/Official/LC/", LcNoComb.getValue() + ".bin", lcFtech.get(0).get(0) + "▓" + lcFtech.get(0).get(1) + "▓" + lcFtech.get(0).get(2) + "▓" + lcFtech.get(0).get(3) + "▓" + "Complete" + "▓").writeFile();
            
            document.close();
            
            (new Prompt()).getAlert("LC Created Successfully!", "information");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        (new GUI(user, email, sanData)).applcClick(event);
        
    }

    @FXML
    private void admBack(MouseEvent event) {
        (new GUI(user, email, sanData)).applcClick(event);
    }

}
