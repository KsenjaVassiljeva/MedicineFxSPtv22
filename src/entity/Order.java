package entity;

import entity.OrderItem;
import entity.User;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private LocalDate orderDate = LocalDate.now();
    private double totalPrice;
    private boolean fulfilled;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {
        // Initialize orderDate in default constructor
    }

    // Getters and Setters

    // Methods to add and remove order items
    public void addOrderItem(OrderItem orderItem) {
        if (orderItem != null) {
            orderItems.add(orderItem);
            totalPrice += orderItem.getSubtotal();
        }
    }

    public void removeOrderItem(OrderItem orderItem) {
        if (orderItem != null && orderItems.contains(orderItem)) {
            orderItems.remove(orderItem);
            totalPrice -= orderItem.getSubtotal();
        }
    }

    // Override toString method
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", totalPrice=" + totalPrice +
                ", fulfilled=" + fulfilled +
                ", user=" + user +
                '}';
    }

    public User getUser() {
    return user;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

}
