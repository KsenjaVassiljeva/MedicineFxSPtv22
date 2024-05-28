/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medicine.newmedicine;

import entity.Manufacturer;
import entity.Medicine;
import entity.Category;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import sptv22medicineshop.HomeController;

/**
 * FXML Controller class for adding new medicine.
 */
public class NewmedicineController implements Initializable {

    private HomeController homeController;
    private ObservableList<Manufacturer> manufacturers;
    private ObservableList<Category> categories;
    private File selectedFile;

    @FXML private TextField tfName;
    @FXML private TextField tfPrice;
    @FXML private TextField tfQuantity;
    @FXML private Button btAddManufacturer;
    @FXML private ListView<Manufacturer> lvManufacturers;
    @FXML private Button btAddCategory;
    @FXML private ListView<Category> lvCategories;
    @FXML private Button btAddMedicine;
    @FXML private Button btAddCover;

    @FXML
    private void clickAddManufacturer() {
        List<Manufacturer> listManufacturers = getHomeController().getApp().getEntityManager()
                .createQuery("SELECT m FROM Manufacturer m")
                .getResultList();
        this.manufacturers = FXCollections.observableArrayList(listManufacturers);
        ListView<Manufacturer> listViewManufacturersWindow = new ListView<>(manufacturers);
        listViewManufacturersWindow.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        listViewManufacturersWindow.setCellFactory((ListView<Manufacturer> manufacturers) -> new ListCell<Manufacturer>() {
            @Override
            protected void updateItem(Manufacturer manufacturer, boolean empty) {
                super.updateItem(manufacturer, empty);
                if (manufacturer != null) {
                    setText(manufacturer.getName());
                } else {
                    setText(null);
                }
            }
        });
        Stage modalWindows = new Stage();
        Button selectButton = new Button("Выбрать");
        selectButton.setOnAction((ActionEvent event) -> {
            ObservableList<Manufacturer> selectedManufacturers = listViewManufacturersWindow.getSelectionModel().getSelectedItems();
            lvManufacturers.getItems().addAll(selectedManufacturers);
            modalWindows.close();
        });

        HBox hbButtons = new HBox(selectButton);
        hbButtons.setSpacing(10);
        hbButtons.setAlignment(Pos.CENTER_RIGHT);
        VBox root = new VBox(listViewManufacturersWindow, hbButtons);
        Scene scene = new Scene(root, 300, 250);
        modalWindows.setTitle("Список производителей");
        modalWindows.initModality(Modality.WINDOW_MODAL);
        modalWindows.initOwner(homeController.getApp().getPrimaryStage());
        modalWindows.setScene(scene);
        modalWindows.show();
    }

    @FXML
    private void clickAddCategory() {
        List<Category> listCategories = getHomeController().getApp().getEntityManager()
                .createQuery("SELECT c FROM Category c")
                .getResultList();
        this.categories = FXCollections.observableArrayList(listCategories);
        ListView<Category> listViewCategoriesWindow = new ListView<>(categories);
        listViewCategoriesWindow.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        listViewCategoriesWindow.setCellFactory((ListView<Category> categories) -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                if (category != null) {
                    setText(category.getName());
                } else {
                    setText(null);
                }
            }
        });
        Stage modalWindows = new Stage();
        Button selectButton = new Button("Выбрать");
        selectButton.setOnAction(event -> {
            ObservableList<Category> selectedCategories = listViewCategoriesWindow.getSelectionModel().getSelectedItems();
            lvCategories.getItems().addAll(selectedCategories);
            modalWindows.close();
        });

        HBox hbButtons = new HBox(selectButton);
        hbButtons.setSpacing(10);
        hbButtons.setAlignment(Pos.CENTER_RIGHT);
        VBox root = new VBox(listViewCategoriesWindow, hbButtons);
        Scene scene = new Scene(root, 300, 250);
        modalWindows.setTitle("Список категорий");
        modalWindows.initModality(Modality.WINDOW_MODAL);
        modalWindows.initOwner(homeController.getApp().getPrimaryStage());
        modalWindows.setScene(scene);
        modalWindows.show();
    }

    @FXML
    private void clickAddMedicine() {
        if (tfName.getText().isEmpty() || tfPrice.getText().isEmpty()
                || tfQuantity.getText().isEmpty() || lvManufacturers.getItems().isEmpty()
                || lvCategories.getItems().isEmpty()) {
            homeController.getLbInfo().getStyleClass().clear();
            homeController.getLbInfo().getStyleClass().add("infoError");
            homeController.getLbInfo().setText("Заполните все поля формы");
            return;
        }
        Medicine medicine = new Medicine();
        medicine.setName(tfName.getText());
        medicine.setPrice(Double.parseDouble(tfPrice.getText()));
        medicine.setQuantity(Integer.parseInt(tfQuantity.getText()));

        List<Manufacturer> medicineManufacturers = new ArrayList<>();
        for (int i = 0; i < lvManufacturers.getItems().size(); i++) {
            Manufacturer manufacturer = lvManufacturers.getItems().get(i);
            medicineManufacturers.add(manufacturer);
        }
        medicine.setManufacturers(medicineManufacturers);

        List<Category> medicineCategories = new ArrayList<>();
        for (int i = 0; i < lvCategories.getItems().size(); i++) {
            Category category = lvCategories.getItems().get(i);
            medicineCategories.add(category);
        }
        medicine.setCategories(medicineCategories);

        if (selectedFile != null) {
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                byte[] fileContent = new byte[(int) selectedFile.length()];
                fis.read(fileContent);
                medicine.setImage(fileContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            homeController.getApp().getEntityManager().getTransaction().begin();
            homeController.getApp().getEntityManager().persist(medicine);
            homeController.getApp().getEntityManager().getTransaction().commit();
            tfName.setText("");
            tfPrice.setText("");
            tfQuantity.setText("");
            lvManufacturers.getItems().clear();
            lvCategories.getItems().clear();
            btAddCover.setText("Выберите изображение");
            selectedFile = null;
            homeController.getLbInfo().getStyleClass().clear();
            homeController.getLbInfo().getStyleClass().add("info");
            homeController.getLbInfo().setText("Лекарство добавлено");
        } catch (Exception e) {
            homeController.getLbInfo().getStyleClass().clear();
            homeController.getLbInfo().getStyleClass().add("infoError");
            homeController.getLbInfo().setText("Не удалось добавить лекарство");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lvManufacturers.setCellFactory(new Callback<ListView<Manufacturer>, ListCell<Manufacturer>>() {
            @Override
            public ListCell<Manufacturer> call(ListView<Manufacturer> p) {
                return new ListCell<Manufacturer>() {
                    @Override
                    protected void updateItem(Manufacturer manufacturer, boolean empty) {
                        super.updateItem(manufacturer, empty);
                        if (manufacturer != null) {
                            setText(manufacturer.getName());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });

        lvCategories.setCellFactory(new Callback<ListView<Category>, ListCell<Category>>() {
            @Override
            public ListCell<Category> call(ListView<Category> p) {
                return new ListCell<Category>() {
                    @Override
                    protected void updateItem(Category category, boolean empty) {
                        super.updateItem(category, empty);
                        if (category != null) {
                            setText(category.getName());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });

        btAddCover.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выберите файл");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Изображения", "*.jpg", "*.png"),
                    new FileChooser.ExtensionFilter("Все файлы", "*.*")
            );
            selectedFile = fileChooser.showOpenDialog(getHomeController().getApp().getPrimaryStage());
            if (selectedFile != null) {
                btAddCover.setText("Выбран файл: " + selectedFile.getName());
                System.out.println("Выбранный файл: " + selectedFile.getAbsolutePath());
            } else {
                btAddCover.setText("Файл не выбран");
                System.out.println("Файл не выбран.");
            }
        });
    }

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    public HomeController getHomeController() {
        return homeController;
    }

    public ObservableList<Manufacturer> getManufacturers() {
        return manufacturers;
    }

    public ObservableList<Category> getCategories() {
        return categories;
    }
}
