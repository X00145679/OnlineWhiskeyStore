package controllers;

import play.mvc.*;
import play.data.*;
import javax.inject.Inject;

import views.html.*;
import play.db.ebean.Transactional;
import play.api.Environment;

// Import models
import models.users.*;
import models.products.*;
import models.shopping.*;

@Security.Authenticated(Secured.class)

@With(CheckIfCustomer.class)

public class ShoppingCtrl extends Controller {

    private FormFactory formFactory;
    private Environment env;

    @Inject
    public ShoppingCtrl(Environment e, FormFactory f) {
        this.env = e;
        this.formFactory = f;
    }
    
    @Transactional
    public Result viewOrder(long id) {
        ShopOrder order = ShopOrder.find.byId(id);
        return ok(orderConfirmed.render((Customer)User.getUserById(session().get("email")), order));
    }

    @Transactional
    public Result addToBasket(Long id) {
        ItemOnSale item = ItemOnSale.find.byId(id);
        
        Customer customer = (Customer)User.getUserById(session().get("email"));
        
        if (customer.getBasket() == null) {
            
            customer.setBasket(new Basket());
            customer.getBasket().setCustomer(customer);
            customer.update();
        }
       
        customer.getBasket().addItemOnSale(item);
        customer.update();

        item.decrementStock();
        item.update();
       
        return ok(basket.render(customer));
    }

    @Transactional
    public Result emptyBasket() {
        
        Customer c = (Customer)User.getUserById(session().get("email"));
        c.getBasket().removeAllItems();
        c.getBasket().update();
        
        return ok(basket.render(c));
    }

    @Transactional
    public Result placeOrder() {
        Customer c = (Customer)User.getUserById(session().get("email"));
       
        ShopOrder order = new ShopOrder();
        
        order.setCustomer(c);
        order.setItems(c.getBasket().getBasketItems());
        order.save();
       
       
        for (OrderItem i: order.getItems()) {
            
            i.setOrder(order);
            i.setBasket(null);
            i.update();
        }
        
        order.update();
        c.getBasket().setBasketItems(null);
        c.getBasket().update();
        
        return ok(orderConfirmed.render(c, order));
    }

    @Transactional
    public Result showBasket() {
        return ok(basket.render((Customer)User.getUserById(session().get("email"))));
    }
    @Transactional
    public Result addOne(Long itemId, Long pid) {
        
        // Get the order item
        OrderItem item = OrderItem.find.byId(itemId);
        ItemOnSale ios = ItemOnSale.find.byId(pid);
        if(ios.getStock()>0){
        // Increment quantity
        item.increaseQty();
        // Save
        item.update();
        }else{
            flash("error","Sorry, no more of these items left");
        }

        // Show updated basket
        return redirect(routes.ShoppingCtrl.showBasket());
    }

    @Transactional
    public Result removeOne(Long itemId, Long pid) {
    
        OrderItem item = OrderItem.find.byId(itemId);
        ItemOnSale ios = ItemOnSale.find.byId(pid);

        Customer c = (Customer)User.getUserById(session().get("email"));
      
        c.getBasket().removeItem(item,ios);
        c.getBasket().update();
       
        return ok(basket.render(c));
    }
}