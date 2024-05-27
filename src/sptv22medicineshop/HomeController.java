/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sptv22medicineshop;

import admin.adminpanel.AdminpanelController;
import customers.newcustomer.NewCustomerController;
import customers.listcustomers.ListCustomersController;
import customers.customerprofile.CustomerProfileController;
import entity.Medicine;
import entity.User;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import medicine.newmedicine.NewMedicineController;
import medicine.listmedicines.ListMedicinesController;
import users.loginform.LoginFormController;

/**
 * 
 * Main controller class for the pharmacy application.
 * 
 */
public class HomeController implements Initializable {
    private SPTV22MedicineShop app;
    private Stage loginWindow;
    @FXML private Label lbHello;
    @FXML private Label lbInfo;
    @FXML private VBox vbContent;
    
    @FXML
    public void showAdminPanel() {
        if (SPTV22MedicineShop.user == null) {
            getLbInfo().getStyleClass().clear();
            getLbInfo().getStyleClass().add("infoError");
            getLbInfo().setText("Please log in with your credentials!"); 
            return;
        }
        if (!SPTV22MedicineShop.user.getRoles().contains(SPTV22MedicineShop.ROLES.ADMINISTRATOR.toString())) {
            getLbInfo().getStyleClass().clear();
            getLbInfo().getStyleClass().add("infoError");
            getLbInfo().setText("Access denied. Administrators only!"); 
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/admin/adminpanel/adminpanel.fxml"));
        try {
            VBox vbAdminPanelRoot = loader.load();
            AdminpanelController adminpanelController = loader.getController();
            adminpanelController.setHomeController(this);
            adminpanelController.loadUsers();
            adminpanelController.loadRoles();
            vbContent.getChildren().clear();
            vbContent.getChildren().add(vbAdminPanelRoot);
            
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
    }

    @FXML
    public void login() {
        loginWindow = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/users/loginform/loginform.fxml"));
        try {
            VBox vbLoginFormRoot = loader.load();
            LoginFormController loginFormController = loader.getController();
            loginFormController.setHomeController(this);
            Scene scene = new Scene(vbLoginFormRoot, 401, 180);
            loginWindow.setTitle("Login");
            loginWindow.initModality(Modality.WINDOW_MODAL);
            loginWindow.initOwner(getApp().getPrimaryStage());
            loginWindow.setScene(scene);
            loginWindow.show();
            
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
    }

    @FXML
    private void addNewCustomer() {
        getLbInfo().getStyleClass().clear();
        getLbInfo().getStyleClass().add("info");
        getLbInfo().setText("");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/customers/newcustomer/newcustomer.fxml"));
            VBox vbNewCustomer = loader.load();
            NewCustomerController newCustomerController = loader.getController();
            newCustomerController.setHomeController(this);
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
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/customers/customerprofile/customerprofile.fxml"));
            VBox vbCustomerProfileRoot = loader.load();
            CustomerProfileController customerProfileController = loader.getController();
            customerProfileController.setHomeController(this);
            try {
                customerProfileController.loadCustomer();
            } catch (Exception e) {
                getLbInfo().getStyleClass().clear();
                getLbInfo().getStyleClass().add("infoError");
                getLbInfo().setText("Please log in with your credentials!");  
                return;
            }
            app.getPrimaryStage().setTitle("SPTV22MedicineShop - Customer Profile");
            vbContent.getChildren().clear();
            vbContent.getChildren().add(vbCustomerProfileRoot);
            
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addNewMedicine() {
        if (SPTV22MedicineShop.user == null) {
            getLbInfo().getStyleClass().clear();
            getLbInfo().getStyleClass().add("infoError");
            getLbInfo().setText("Please log in with your credentials!"); 
            return;
        }
        if (!SPTV22MedicineShop.user.getRoles().contains(SPTV22MedicineShop.ROLES.MANAGER.toString())) {
            getLbInfo().getStyleClass().clear();
            getLbInfo().getStyleClass().add("infoError");
            getLbInfo().setText("Access denied. Managers only!"); 
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/medicine/newmedicine/newmedicine.fxml"));
            VBox vbNewMedicine = loader.load();
            NewMedicineController newMedicineController = loader.getController();
            newMedicineController.setHomeController(this);
            app.getPrimaryStage().setTitle("SPTV22FXPharmacy - Add New Medicine");
            this.lbInfo.setText("");
            vbContent.getChildren().clear();
            vbContent.getChildren().add(vbNewMedicine);
            
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void listCustomers() {
        if (SPTV22MedicineShop.user == null) {
            getLbInfo().getStyleClass().clear();
            getLbInfo().getStyleClass().add("infoError");
            getLbInfo().setText("Please log in with your credentials!"); 
            return;
        }
        if (!SPTV22MedicineShop.user.getRoles().contains(SPTV22MedicineShop.ROLES.MANAGER.toString())) {
            getLbInfo().getStyleClass().clear();
            getLbInfo().getStyleClass().add("infoError");
            getLbInfo().setText("Access denied. Managers only!"); 
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/customers/listcustomers/listcustomers.fxml"));
            VBox vbListCustomers = loader.load();
            ListCustomersController listCustomersController = loader.getController();
            listCustomersController.setHomeController(this);
            listCustomersController.loadCustomers();
            app.getPrimaryStage().setTitle("SPTV22FXPharmacy - Customer List");
            this.lbInfo.setText("");
            vbContent.getChildren().clear();
            vbContent.getChildren().add(vbListCustomers);
            
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void listMedicines() {
        if (SPTV22MedicineShop.user == null) {
            getLbInfo().getStyleClass().clear();
            getLbInfo().getStyleClass().add("infoError");
            getLbInfo().setText("Please log in with your credentials!"); 
            return;
        }
        if (!SPTV22MedicineShop.user.getRoles().contains(SPTV22MedicineShop.ROLES.USER.toString())) {
            getLbInfo().getStyleClass().clear();
            getLbInfo().getStyleClass().add("infoError");
            getLbInfo().setText("Access denied. Users only!"); 
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/medicine/listmedicines/listmedicines.fxml"));
            VBox vbListMedicines = loader.load();
            ListMedicinesController listMedicinesController = loader.getController();
            listMedicinesController.setHomeController(this);
            listMedicinesController.loadMedicines();
            app.getPrimaryStage().setTitle("SPTV22MedicineShop - Medicine List");
            this.lbInfo.setText("");
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

    void setApp(SPTV22MedicineShop app) {
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

    void loadMedicines() {
        List<Medicine> listMedicines = app.getEntityManager()
                .createQuery("SELECT m FROM Medicine m")
                .getResultList();
        ObservableList medicines = FXCollections.observableArrayList(listMedicines);
        HBox hbListMedicines = new HBox();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/medicine/medicine/medicine.fxml"));
            VBox vbMedicine = loader.load();
            // Assuming you have a MedicineController to handle the medicines
            // MedicineController medicineController = loader.getController();
            // medicineController.setMedicines(medicines);
            
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
}