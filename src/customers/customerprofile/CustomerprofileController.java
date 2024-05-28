package customers.customerprofile;

import entity.Customer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import sptv22medicineshop.HomeController;

public class CustomerprofileController implements Initializable {

    private HomeController homeController;
    private Customer customer;
    @FXML private TextField tfFirstName;
    @FXML private TextField tfLastName;
    @FXML private TextField tfPhone;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Инициализация, если необходимо
    }    

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    public void loadCustomer(Customer customer) {
        this.customer = customer;
        homeController.getLbInfo().setText("");
        tfFirstName.setText(customer.getFirstname());
        tfLastName.setText(customer.getLastname());
        tfPhone.setText(customer.getPhoneNumber());
    }

    @FXML
    private void clickEditProfile() {
        try {
            customer.setFirstname(tfFirstName.getText().trim());
            customer.setLastname(tfLastName.getText().trim());
            customer.setPhoneNumber(tfPhone.getText().trim());
            homeController.getApp().getEntityManager().getTransaction().begin();
            homeController.getApp().getEntityManager().merge(customer);
            homeController.getApp().getEntityManager().getTransaction().commit();
            homeController.getLbInfo().getStyleClass().clear();
            homeController.getLbInfo().getStyleClass().add("info");
            homeController.getLbInfo().setText("Профиль изменен!");
        } catch (Exception e) {
            homeController.getLbInfo().getStyleClass().clear();
            homeController.getLbInfo().getStyleClass().add("infoError");
            homeController.getLbInfo().setText("Изменить профиль не удалось");
        }
    }

    public void loadCustomer() {
    if (customer != null) {
        tfFirstName.setText(customer.getFirstname());
        tfLastName.setText(customer.getLastname());
        tfPhone.setText(customer.getPhoneNumber());
    } else {
        // В случае, если клиент не загружен, можно выполнить соответствующие действия
        // Например, очистить текстовые поля или вывести сообщение об ошибке
        tfFirstName.clear();
        tfLastName.clear();
        tfPhone.clear();
        // Вывести сообщение об ошибке или уведомление
        homeController.getLbInfo().getStyleClass().clear();
        homeController.getLbInfo().getStyleClass().add("infoError");
        homeController.getLbInfo().setText("Ошибка загрузки информации о клиенте");
    }
}

}
