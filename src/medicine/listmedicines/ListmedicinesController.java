package medicine.listmedicines;

import entity.Medicine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sptv22medicineshop.HomeController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import medicines.medicine.MedicineController;

public class ListmedicinesController implements Initializable {

    private HomeController homeController;
    @FXML private HBox hbListMedicinesContent;
    private Stage modalWindow;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code, if any
    }

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    public HomeController getHomeController() {
        return homeController;
    }

    public void loadMedicinesList() {
        try {
            hbListMedicinesContent.getChildren().clear();
            List<Medicine> listMedicines = homeController.getApp().getEntityManager()
                    .createQuery("SELECT m FROM Medicine m")
                    .getResultList();
            for (Medicine medicine : listMedicines) {
                FXMLLoader medicineLoader = new FXMLLoader(getClass().getResource("/medicine/medicine/medicine.fxml"));
                VBox vbMedicineRoot = medicineLoader.load();
                MedicineController medicineController = medicineLoader.getController();
                medicineController.setListMedicinesController(this);
                medicineController.setMedicine(medicine);
                setupMedicineEventHandlers(vbMedicineRoot, medicine);
                hbListMedicinesContent.getChildren().add(vbMedicineRoot);
            }
        } catch (IOException ex) {
            Logger.getLogger(ListmedicinesController.class.getName()).log(Level.SEVERE, "Error loading medicine list", ex);
        }
    }

    private void setupMedicineEventHandlers(VBox vbMedicineRoot, Medicine medicine) {
        vbMedicineRoot.setOnMouseEntered(event -> vbMedicineRoot.setCursor(Cursor.HAND));
        vbMedicineRoot.setOnMouseExited(event -> vbMedicineRoot.setCursor(Cursor.DEFAULT));
        vbMedicineRoot.setOnMouseClicked(event -> showMedicineDetails(medicine));
    }

    private void showMedicineDetails(Medicine medicine) {
        try {
            modalWindow = new Stage();
            FXMLLoader medicineLoader = new FXMLLoader(getClass().getResource("/medicine/medicine/medicine.fxml"));
            VBox vbMedicineRoot = medicineLoader.load();
            MedicineController medicineController = medicineLoader.getController();
            medicineController.setMedicineDetails(medicine);
            modalWindow.setScene(new Scene(vbMedicineRoot, 400, 600));
            modalWindow.setTitle(medicine.getName());
            modalWindow.initModality(Modality.WINDOW_MODAL);
            modalWindow.initOwner(getHomeController().getApp().getPrimaryStage());
            modalWindow.show();
        } catch (IOException ex) {
            Logger.getLogger(ListmedicinesController.class.getName()).log(Level.SEVERE, "Error loading medicine details", ex);
        }
    }

    public void loadMedicines() {
    List<Medicine> listMedicines = homeController.getApp().getEntityManager()
            .createQuery("SELECT m FROM Medicine m")
            .getResultList();
        try {
            hbListMedicinesContent.getChildren().clear();
            for (Medicine medicine : listMedicines) {
                FXMLLoader medicineLoader = new FXMLLoader(getClass().getResource("/medicine/medicine/medicine.fxml"));
                VBox vbMedicineRoot = medicineLoader.load();
                MedicineController medicineController = medicineLoader.getController();
                medicineController.setListMedicinesController(this);
                medicineController.setMedicine(medicine);
                hbListMedicinesContent.getChildren().add(vbMedicineRoot);
            }
        } catch (IOException ex) {
            Logger.getLogger(ListmedicinesController.class.getName()).log(Level.SEVERE, "Failed to load medicine.fxml", ex);
        }
    }
}

