package users.loginform;

import entity.User;
import java.net.URL;
import java.util.ResourceBundle;
import javax.persistence.NoResultException; // Import NoResultException
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import sptv22medicineshop.HomeController;
import sptv22medicineshop.SPTV22MedicineShop;
import tools.PassEncrypt;

public class LoginformController implements Initializable {
    private HomeController homeController;
    
    @FXML private Label lbLoginInfo;
    @FXML private TextField tfLogin;
    @FXML private TextField tfPassword;
    @FXML private Button btGoToLogin;
    
    @FXML
    private void clickLogin() {
        String login = tfLogin.getText();
        String password = tfPassword.getText();
        try {
            // Retrieve user by login
            User user = homeController.getApp().getEntityManager()
                    .createQuery("SELECT u FROM User u WHERE u.login=:login", User.class)
                    .setParameter("login", login)
                    .getSingleResult();

            // Compare encrypted passwords
            PassEncrypt pe = new PassEncrypt();
            String encryptedPassword = pe.getEncryptPassword(password, user.getSalt());
            if (!user.getPassword().equals(encryptedPassword)) {
                // Incorrect password
                lbLoginInfo.setText("Неправильный логин или пароль");
                return;
            }

            // Successful login
            handleSuccessfulLogin(user);
        } catch (NoResultException e) {
            // User not found
            handleLoginFailure("Неправильный логин или пароль", e);
        } catch (Exception e) {
            // Other exceptions
            handleLoginFailure("Произошла ошибка при входе", e);
        }
    }

    private void handleSuccessfulLogin(User user) {
        // Setting user and displaying info on successful login
        SPTV22MedicineShop.setUser(user);
        homeController.getLbInfo().getStyleClass().clear();
        homeController.getLbInfo().getStyleClass().add("info");
        homeController.getLbInfo().setText("Вы вошли как " + user.getLogin());
        homeController.getLoginWindow().close();
    }

    private void handleLoginFailure(String message, Exception e) {
        // Display error message and log exception
        lbLoginInfo.setText(message);
        System.err.println("Error during login: " + e.getMessage());
    }


    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Setting prompt text for login and password fields
        tfLogin.setPromptText("Логин");
        tfPassword.setPromptText("Пароль");
        
        // Handling ENTER key press for password field
        tfPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                clickLogin();
            }
        });
        
        // Handling ENTER key press for button
        btGoToLogin.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                clickLogin();
            }
        });
    }    

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }
}