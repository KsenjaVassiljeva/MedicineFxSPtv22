package admin.adminpanel;

import entity.User;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import sptv22medicineshop.HomeController;

public class AdminpanelController implements Initializable {

    private HomeController homeController;
    private User selectedUser;
    private String selectedRole; // Change to String instead of Enum
    @FXML private ComboBox<User> cbUsers;
    @FXML private ComboBox<String> cbRoles; // Change ComboBox type to String
    @FXML private Button btDeleteRole;
    @FXML private Button btAddRole;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadUsers();
        loadRoles();
    }

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    public void loadUsers() {
        cbUsers.setItems(FXCollections.observableArrayList(homeController.getApp().getEntityManager()
               .createQuery("SELECT u FROM User u WHERE u.username != :admin", User.class) // Change u.login to u.username if that's the correct field
               .setParameter("admin", "admin")
               .getResultList()));
        cbUsers.setCellFactory(param -> new ListCell<User>(){
            @Override
            protected void updateItem(User user, boolean empty){
                super.updateItem(user, empty);
                if(user == null || empty){
                    setText(null);
                }else{
                    setText(user.getUsername() // Change to getUsername() if that's the correct method
                                + " - роли " 
                                + Arrays.toString(user.getRoles().toArray()));
                }
            }
        });
        cbUsers.setButtonCell(new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null || empty) {
                    setText(null);
                } else {
                    setText(user.getUsername() // Change to getUsername() if that's the correct method
                                + " - роли " 
                                + Arrays.toString(user.getRoles().toArray()));
                }
            }
        });
        cbUsers.setOnAction(event -> {
            this.selectedUser = cbUsers.getValue();
            homeController.getLbInfo().setText("");
        });
    }

    public void loadRoles() {
        // Assuming roles are stored as Strings in the database
        // Modify the query accordingly if the roles are stored differently
        cbRoles.setItems(FXCollections.observableArrayList(Arrays.asList("ADMINISTRATOR", "MANAGER", "PHARMACIST")));
        cbRoles.setCellFactory(param -> new ListCell<String>(){
            @Override
            protected void updateItem(String role, boolean empty){
                super.updateItem(role, empty);
                if(role == null || empty){
                    setText(null);
                }else{
                    setText(role);
                }
            }
        });
        cbRoles.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String role, boolean empty) {
                super.updateItem(role, empty);
                if (role == null || empty) {
                    setText(null);
                } else {
                    setText(role);
                }
            }
        });
        cbRoles.setOnAction(event -> {
            this.selectedRole = cbRoles.getValue();
            homeController.getLbInfo().setText("");
        });
    }

    @FXML
    public void clickBtAddRole() {
        if (selectedUser != null && selectedRole != null) {
            if (!selectedUser.getRoles().contains(selectedRole)) {
                selectedUser.getRoles().add(selectedRole);
                changeRole();
            }
        }
    }

    @FXML
    public void clickBtDeleteRole() {
        if (selectedUser != null && selectedRole != null) {
            if (selectedUser.getRoles().contains(selectedRole)) {
                selectedUser.getRoles().remove(selectedRole);
                changeRole();
            }
        }
    }

    private void changeRole() {
        if (selectedUser != null) {
            homeController.getApp().getEntityManager().getTransaction().begin();
            homeController.getApp().getEntityManager().merge(selectedUser);
            homeController.getApp().getEntityManager().getTransaction().commit();
            loadUsers(); // Update ComboBox after role change
        }
    }
}
