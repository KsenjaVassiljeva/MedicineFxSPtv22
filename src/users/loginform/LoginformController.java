package users.loginform;

import entity.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import sptv22medicineshop.HomeController;
import tools.PassEncrypt;

public class LoginformController implements Initializable {
    private HomeController homeController;
    @FXML private Label lbLoginInfo;
    @FXML private TextField tfLogin;
    @FXML private PasswordField pfPassword;
    @FXML private Button btLogin;

    @FXML
    private void clickLogin() {
        String login = tfLogin.getText();
        String password = pfPassword.getText();
        try {
            User user = (User) homeController.getApp().getEntityManager().createQuery("SELECT u FROM User u WHERE u.login=:login")
                .setParameter("login", login)
                .getSingleResult();
            PassEncrypt pe = new PassEncrypt();
            String encryptedPassword = pe.getEncryptPassword(password, pe.getSalt());
            if (!user.getPassword().equals(encryptedPassword)) {
                lbLoginInfo.setText("Неправильный пароль");
                return;
            }
            // Устанавливаем текущего пользователя в классе SPTV22MedicineShop
            sptv22medicineshop.SPTV22MedicineShop.setUser(user);
            homeController.getLbInfo().getStyleClass().clear();
            homeController.getLbInfo().getStyleClass().add("info");
            homeController.getLbInfo().setText("Вы вошли как " + user.getLogin());
            homeController.getLoginWindow().close();
        } catch (Exception e) {
            lbLoginInfo.setText("Нет такого пользователя");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfLogin.setPromptText("Логин");
        pfPassword.setPromptText("Пароль");
        pfPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                clickLogin();
            }
        });

        // Обработчик события для кнопки входа
        btLogin.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                clickLogin();
            }
        });
    }

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }
}
