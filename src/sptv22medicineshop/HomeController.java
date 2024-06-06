package sptv22medicineshop;

import admin.adminpanel.AdminpanelController;
import customers.newcustomer.NewcustomerController;
import customers.listcustomers.ListcustomersController;
import customers.customerprofile.CustomerprofileController;
import entity.Medicine;
import entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import medicine.newmedicine.NewmedicineController;
import medicine.listmedicines.ListmedicinesController;
import medicines.medicine.MedicineController;
import range.RangepageController;
import users.loginform.LoginformController;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main controller class for the pharmacy application.
 */
public class HomeController implements Initializable {
    private SPTV22MedicineShop app;
    private Stage loginWindow;
    @FXML private Label lbHello;
    @FXML private Label lbInfo;
    @FXML private VBox vbContent;
    public static User user;
    private final SPTV22MedicineShop medicineShop = new SPTV22MedicineShop();
    private int secondsLeft = 86400; // Общее количество секунд (1 день = 86400 секунд)
    private Label lbDiscountTimer = new Label();

    @FXML
    public void showAdminPanel() {
        if (SPTV22MedicineShop.user == null) {
            displayErrorMessage("Please log in with your credentials!");
            return;
        }
        if (!SPTV22MedicineShop.user.getRoles().contains(SPTV22MedicineShop.ROLES.ADMINISTRATOR.toString())) {
            displayErrorMessage("Access denied. Administrators only!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/adminpanel/adminpanel.fxml"));
            VBox vbAdminPanelRoot = loader.load();
            AdminpanelController adminpanelController = loader.getController();
            adminpanelController.setHomeController(this);
            adminpanelController.loadUsers();
            adminpanelController.loadRoles();
            vbContent.getChildren().clear();
            vbContent.getChildren().add(vbAdminPanelRoot);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    @FXML
    public void showRangepage() {
        if (SPTV22MedicineShop.user == null) {
            displayErrorMessage("Please log in with your credentials!");
            return;
        }
        if (!SPTV22MedicineShop.user.getRoles().contains(SPTV22MedicineShop.ROLES.ADMINISTRATOR.toString())) {
            displayErrorMessage("Access denied. Administrators only!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/range/rangepage.fxml"));
            VBox vbAdminpanelRoot = loader.load();
            RangepageController rangepageController = loader.getController();
            rangepageController.setHomeController(this);
            rangepageController.setOrderHistory();
            rangepageController.showRangeProducts();
            rangepageController.showRangeCustomers();
            getApp().getPrimaryStage().setHeight(getApp().getPrimaryStage().getHeight() + 70);
            vbContent.getChildren().clear();
            vbContent.getChildren().add(vbAdminpanelRoot);
        } catch (IOException e) {
            e.printStackTrace();
            displayErrorMessage("Error loading rangepage: " + e.getMessage());
        }
    }

    @FXML
    public void login() {
        loginWindow = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/users/loginform/loginform.fxml"));
            VBox vbLoginFormRoot = loader.load();
            LoginformController loginformController = loader.getController();
            loginformController.setHomeController(this);
            Scene scene = new Scene(vbLoginFormRoot, 401, 180);
            loginWindow.setTitle("Вход");
            loginWindow.initModality(Modality.WINDOW_MODAL);
            loginWindow.initOwner(getApp().getPrimaryStage());
            loginWindow.setScene(scene);
            loginWindow.show();
        } catch (IOException e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }

    @FXML
    private void addNewCustomer() {
        displayInfoMessage("");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/customers/newcustomer/newcustomer.fxml"));
            VBox vbNewCustomer = loader.load();
            NewcustomerController newcustomerController = loader.getController();
            newcustomerController.setHomeController(this);
            app.getPrimaryStage().setTitle("SPTV22FXPharmacy - Add New Customer");
            vbContent.getChildren().clear();
            vbContent.getChildren().add(vbNewCustomer);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void customerProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/customers/customerprofile/customerprofile.fxml"));
            VBox vbCustomerProfileRoot = loader.load();
            CustomerprofileController customerProfileController = loader.getController();
            customerProfileController.setHomeController(this);
            customerProfileController.loadCustomer();
            app.getPrimaryStage().setTitle("SPTV22MedicineShop - Customer Profile");
            vbContent.getChildren().clear();
            vbContent.getChildren().add(vbCustomerProfileRoot);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            displayErrorMessage("Please log in with your credentials!");
        }
    }

    @FXML
    private void addNewMedicine() {
        if (SPTV22MedicineShop.user == null) {
            displayErrorMessage("Please log in with your credentials!");
            return;
        }
        if (!SPTV22MedicineShop.user.getRoles().contains(SPTV22MedicineShop.ROLES.MANAGER.toString())) {
            displayErrorMessage("Access denied. Managers only!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/medicine/newmedicine/newmedicine.fxml"));
            VBox vbNewMedicine = loader.load();
            NewmedicineController newMedicineController = loader.getController();
            newMedicineController.setHomeController(this);
            app.getPrimaryStage().setTitle("SPTV22FXPharmacy - Add New Medicine");
            displayInfoMessage("");
            vbContent.getChildren().clear();
            vbContent.getChildren().add(vbNewMedicine);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void listCustomers() {
        if (SPTV22MedicineShop.user == null) {
            displayErrorMessage("Please log in with your credentials!");
            return;
        }
        if (!SPTV22MedicineShop.user.getRoles().contains(SPTV22MedicineShop.ROLES.MANAGER.toString())) {
            displayErrorMessage("Access denied. Managers only!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/customers/listcustomers/listcustomers.fxml"));
            VBox vbListCustomers = loader.load();
            ListcustomersController listCustomersController = loader.getController();
            listCustomersController.setHomeController(this);
            listCustomersController.loadCustomers();
            app.getPrimaryStage().setTitle("SPTV22FXPharmacy - Customer List");
            displayInfoMessage("");
            vbContent.getChildren().clear();
            vbContent.getChildren().add(vbListCustomers);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void listMedicines() {
        if (SPTV22MedicineShop.user == null) {
            displayErrorMessage("Please log in with your credentials!");
            return;
        }
        if (!SPTV22MedicineShop.user.getRoles().contains(SPTV22MedicineShop.ROLES.MANAGER.toString())) {
            displayErrorMessage("Access denied. Managers only!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/medicine/listmedicines/listmedicines.fxml"));
            VBox vbListMedicines = loader.load();
            ListmedicinesController listMedicinesController = loader.getController();
            listMedicinesController.setHomeController(this);
            listMedicinesController.loadMedicines();
            app.getPrimaryStage().setTitle("SPTV22MedicineShop - Medicine List");
            displayInfoMessage("");
            vbContent.getChildren().clear();
            vbContent.getChildren().add(vbListMedicines);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbHello.setText("Welcome to our pharmacy!");
    }

    public void setApp(SPTV22MedicineShop app) {
        this.app = app;
    }

    public SPTV22MedicineShop getApp() {
        return app;
    }

    public Label getLbInfo() {
        return lbInfo;
    }

    public Stage getLoginWindow() {
        return loginWindow;
    }

    public EntityManager getEntityManager() {
        return app.getEntityManager();
    }

    public void setUser(User user) {
        HomeController.user = user;
    }

    public static User getUser() {
        return user;
    }

    public void loadMedicines() {
        List<Medicine> listMedicines = app.getEntityManager()
                .createQuery("SELECT m FROM Medicine m", Medicine.class)
                .getResultList();
        ObservableList<Medicine> medicines = FXCollections.observableArrayList(listMedicines);
        vbContent.getChildren().clear(); // Очищаем содержимое, чтобы не дублировать лекарства

        for (Medicine medicine : medicines) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/medicine/medicine/medicine.fxml"));
                VBox vbMedicine = loader.load();
                MedicineController medicineController = loader.getController();
                medicineController.setMedicine(medicine); // Устанавливаем лекарство для контроллера
                vbContent.getChildren().add(vbMedicine); // Добавляем лекарство в интерфейс
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void displayErrorMessage(String message) {
        getLbInfo().getStyleClass().clear();
        getLbInfo().getStyleClass().add("infoError");
        getLbInfo().setText(message);
    }

    private void displayInfoMessage(String message) {
        getLbInfo().getStyleClass().clear();
        getLbInfo().getStyleClass().add("info");
        getLbInfo().setText(message);
    }
}
