package range;

import entity.Order;
import entity.User;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import sptv22medicineshop.HomeController;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RangepageController implements Initializable {
    private List<Order> orderHistory;
    private HomeController homeController;

    @FXML
    private ListView<String> lvRangeProducts;
    @FXML
    private TableView<User> tvRangeCustomers;
    @FXML
    private DatePicker dpFrom;
    @FXML
    private DatePicker dpTo;
    @FXML
    private Label lbRangeProducts;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            @Override
            public String toString(LocalDate date) {
                return date != null ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string, dateFormatter) : null;
            }
        };
        dpFrom.setConverter(converter);
        dpTo.setConverter(converter);
    }

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    @FXML
    public void changeDatePicker() {
        LocalDate localDateFrom = dpFrom.getValue();
        LocalDate localDateTo = dpTo.getValue().plusDays(1);
        LocalDate currentLocalDate = LocalDate.now();

        if (localDateFrom == null || localDateFrom.isAfter(currentLocalDate)
                || localDateFrom.isEqual(localDateTo) || localDateFrom.isAfter(localDateTo)) {
            homeController.getLbInfo().setText("Invalid date selected");
            lvRangeProducts.setItems(null);
            return;
        }
        Date dateFrom = Date.from(localDateFrom.atStartOfDay(
                ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(localDateTo.atStartOfDay(
                ZoneId.systemDefault()).toInstant());
        orderHistory = homeController.getApp().getEntityManager()
                .createQuery(
                        "SELECT o FROM Order o WHERE o.orderDate >= :dateFrom AND o.orderDate <= :dateTo"
                )
                .setParameter("dateFrom", dateFrom)
                .setParameter("dateTo", dateTo)
                .getResultList();
        showRangeProducts();
        showRangeCustomers();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
        lbRangeProducts.setText(String.format("Product range from %s to %s (exclusive)"
                , sdf.format(dateFrom), sdf.format(dateTo)));
    }

    public void setOrderHistory() {
        orderHistory = homeController.getApp().getEntityManager()
                .createQuery("SELECT o FROM Order o")
                .getResultList();
    }

    public void showRangeProducts() {
        Map<String, Integer> productFrequency = new HashMap<>();
        for (Order order : orderHistory) {
            order.getOrderItems().forEach(item -> {
                String productName = item.getProduct().getName();
                productFrequency.merge(productName, 1, Integer::sum);
            });
        }

        Map<String, Integer> sortedProductFrequency = productFrequency.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        ObservableList<String> productList = FXCollections.observableArrayList(sortedProductFrequency.entrySet().stream()
                .map(entry -> entry.getKey() + " - " + entry.getValue() + " times")
                .collect(Collectors.toList()));

        lvRangeProducts.setItems(productList);
        homeController.getLbInfo().setText("");
    }

    public void showRangeCustomers() {
        Map<User, Integer> customerFrequency = new HashMap<>();
        for (Order order : orderHistory) {
            User customer = order.getUser();
            customerFrequency.merge(customer, 1, Integer::sum);
        }

        Map<User, Integer> sortedCustomerFrequency = customerFrequency.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        ObservableList<User> customerList = FXCollections.observableArrayList(sortedCustomerFrequency.keySet());

        TableColumn<User, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<User, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, Integer> orderCountColumn = new TableColumn<>("Order Count");
        orderCountColumn.setCellValueFactory(user -> {
            int count = sortedCustomerFrequency.get(user.getValue());
            return new SimpleIntegerProperty(count).asObject();
        });

        tvRangeCustomers.getColumns().clear();
        tvRangeCustomers.getColumns().addAll(firstNameColumn, lastNameColumn, emailColumn, orderCountColumn);

        tvRangeCustomers.setItems(customerList);
    }
}
