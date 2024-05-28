/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customers.listcustomers;

import entity.Customer;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import sptv22medicineshop.HomeController;

public class ListcustomersController implements Initializable {

    private HomeController homeController;
    private ObservableList<Customer> customers;
    @FXML private ListView<Customer> lvListCustomers;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    public void loadCustomers() {
        List<Customer> listCustomers = homeController.getApp().getEntityManager()
                .createQuery("SELECT c FROM Customer c")
                .getResultList();
        this.customers = FXCollections.observableArrayList(listCustomers);
        lvListCustomers.setItems(this.customers);
        lvListCustomers.setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");
        lvListCustomers.setCellFactory(new Callback<ListView<Customer>, ListCell<Customer>>(){
                @Override
                public ListCell<Customer> call(ListView<Customer> p) {
                    return new ListCell<Customer>(){
                       @Override
                       protected void updateItem(Customer customer, boolean empty){
                           super.updateItem(customer, empty);
                           if(customer != null){
                               int index = getIndex();
                               setText((index + 1) + 
                                       ". " +
                                       customer.getFirstname() + " " + customer.getLastname() +
                                       ". " +
                                       customer.getPhoneNumber()
                                       );
                           } else {
                               setText(null);
                           }
                       }
                    };
                }
            });
    }
}

