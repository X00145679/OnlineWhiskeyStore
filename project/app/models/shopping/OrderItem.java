package models.shopping;

import java.util.*;
import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import models.products.*;
import models.users.*;

@Entity
public class OrderItem extends Model {

    @Id
    private Long id;
    
    private int quantity;
    private double price;

    @ManyToOne
    private ShopOrder order;
    
    @ManyToOne
    private Basket basket;
    
    
    @ManyToOne
    private ItemOnSale item;

    public  OrderItem() {
    }

    public OrderItem(ItemOnSale ios) {
        item = ios;
        quantity = 1;
        price = ios.getPrice();
}
   
    public void increaseQty() {
        quantity += 1;
    }
 
    public void decreaseQty() {
        quantity -= 1;
    }
    
    public double getItemTotal() {
        return this.price * this.quantity;
    }
	
    public static Finder<Long,OrderItem> find = new Finder<Long,OrderItem>(OrderItem.class);

    public static List<OrderItem> findAll() {
        return OrderItem.find.all();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ShopOrder getOrder() {
        return order;
    }

    public void setOrder(ShopOrder order) {
        this.order = order;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public ItemOnSale getItem() {
        return item;
    }

    public void setItem(ItemOnSale item) {
        this.item = item;
    }
}
