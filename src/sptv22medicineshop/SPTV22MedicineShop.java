package sptv22medicineshop;

import entity.Customer;
import entity.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import tools.PassEncrypt;

/**
 * Main class representing the Medicine Shop application.
 */
public class SPTV22MedicineShop extends Application {
    
    public static User user;

    public static void setUser(User user) {
        SPTV22MedicineShop.user = user;
    }

    private Stage primaryStage;
    public enum ROLES {ADMINISTRATOR, MANAGER, PHARMACIST};
    private final EntityManager entityManager;

    /**
     * Constructor for initializing the application.
     */
    public SPTV22MedicineShop() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SPTV22MedicineShopPU");
        this.entityManager = emf.createEntityManager();
        checkSuperUser();
    }

    /**
     * Start method for launching the application.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SPTV22MedicineShopPU");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("home.fxml"));
        Parent root = loader.load();
        HomeController homeController = loader.getController();
        homeController.setApp(this);
        homeController.loadMedicines();
        Scene scene = new Scene(root,600,400);
        scene.getStylesheets().add(getClass().getResource("home.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    /**
     * Method to check if there is a super user in the database. If not, it creates one.
     */
    private void checkSuperUser() {
     if (getEntityManager().createQuery("SELECT COUNT(u) FROM User u", Long.class)
             .getSingleResult() == 0) {
         User admin = new User();
         admin.setLogin("admin");
         PassEncrypt pe = new PassEncrypt();
         String encryptedPassword = pe.getEncryptPassword("12345", pe.getSalt());
         admin.setPassword(encryptedPassword);
         admin.generateAndSetSalt(); // Генерация и установка соли
         admin.getRoles().add(ROLES.ADMINISTRATOR.toString());
         admin.getRoles().add(ROLES.MANAGER.toString());
         admin.getRoles().add(ROLES.PHARMACIST.toString());
         Customer customer = new Customer();
         customer.setFirstname("Anna");
         customer.setLastname("Wesker");
         customer.setPhoneNumber("5654456565");
         getEntityManager().getTransaction().begin();
         getEntityManager().persist(customer);
         admin.setCustomer(customer);
         getEntityManager().persist(admin);
         getEntityManager().getTransaction().commit();
     }
 }



    /**
     * Main method to launch the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Getter for the entity manager.
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Getter for the primary stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
