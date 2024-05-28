package medicines.medicine;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import entity.Medicine;
import javafx.collections.ObservableList;
import medicine.listmedicines.ListmedicinesController;

public class MedicineController implements Initializable {

    private ListmedicinesController listMedicinesController;

    @FXML
    private Label lbMedicineName;

    @FXML
    private Label lbDescription;

    @FXML
    private Label lbPrice;

    @FXML
    private Label lbQuantityInStock;

    @FXML
    private ImageView ivMedicineImage;

    public void setListMedicinesController(ListmedicinesController listMedicinesController) {
        this.listMedicinesController = listMedicinesController;
    }

    public void setMedicine(Medicine medicine) {
        ivMedicineImage.setImage(new Image(new ByteArrayInputStream(medicine.getImage())));
        lbMedicineName.setText(medicine.getName());
        lbDescription.setText(medicine.getDescription());
        lbPrice.setText(String.valueOf(medicine.getPrice()));
        lbQuantityInStock.setText(String.valueOf(medicine.getQuantityInStock()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization code here
    }

    public ImageView getIvMedicineImage() {
        return ivMedicineImage;
    }

    public Label getLbMedicineName() {
        return lbMedicineName;
    }

    public Label getLbDescription() {
        return lbDescription;
    }

    public Label getLbPrice() {
        return lbPrice;
    }

    public Label getLbQuantityInStock() {
        return lbQuantityInStock;
    }

    public void setMedicines(ObservableList<Medicine> medicines) {
        // Since it's a list of medicines, you might want to display them in some way.
        // For example, you can iterate through the list and display each medicine.
        for (Medicine medicine : medicines) {
            // Here you can choose how to display each medicine in your UI,
            // such as adding them to a ListView, TableView, or any other suitable container.
            // For demonstration purposes, I'll just print the medicine name.
            System.out.println("Medicine Name: " + medicine.getName());
        }
    }

    public void setMedicineDetails(Medicine medicine) {
        ivMedicineImage.setImage(new Image(new ByteArrayInputStream(medicine.getImage())));
        lbMedicineName.setText(medicine.getName());
        lbDescription.setText(medicine.getDescription());
        lbPrice.setText(String.valueOf(medicine.getPrice()));
        lbQuantityInStock.setText(String.valueOf(medicine.getQuantityInStock()));
    }
}
