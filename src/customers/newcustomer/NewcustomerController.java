package customers.newcustomer;

import entity.Customer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sptv22medicineshop.HomeController;

import java.net.URL;
import java.util.ResourceBundle;

public class NewcustomerController implements Initializable {

    private HomeController homeController;

    @FXML
    private Button addNewCustomerButton;

    @FXML
    private TextField tfFirstname;

    @FXML
    private TextField tfLastname;

    @FXML
    private TextField tfPhoneNumber;

    @FXML
    private void clickAddNewCustomer() {
        String firstname = tfFirstname.getText().trim();
        String lastname = tfLastname.getText().trim();
        String phoneNumber = tfPhoneNumber.getText().trim();

        if (firstname.isEmpty() || lastname.isEmpty() || phoneNumber.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        Customer customer = new Customer();
        customer.setFirstname(firstname);
        customer.setLastname(lastname);
        customer.setPhoneNumber(phoneNumber);

        try {
            homeController.getEntityManager().getTransaction().begin();
            homeController.getEntityManager().persist(customer);
            homeController.getEntityManager().getTransaction().commit();
            showAlert("Customer added successfully.");
            clearFields();
        } catch (Exception e) {
            showAlert("Failed to add customer.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        tfFirstname.clear();
        tfLastname.clear();
        tfPhoneNumber.clear();
    }
}
