package models.products;
import java.util.*;
import javax.persistence.*;
import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class ItemOnSale extends Model {

    // Properties
    @Id
    private Long id;
    @Constraints.Required
    private String name;
    @Constraints.Required
    private String description;
    @Constraints.Required
    private int stock;
    @Constraints.Required
    private double price;
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "items")
    private List<Category> categories;

    private List<Long> catSelect = new ArrayList<Long>();

    // Default Constructor
    public ItemOnSale() {
    }

    // Constructor to initialise object
    public ItemOnSale(Long id, String name, String description, int stock, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
    }

    public static final Finder<Long, ItemOnSale> find = new Finder<>(ItemOnSale.class);
			    
    public static final List<ItemOnSale> findAll() {
       return ItemOnSale.find.all();
        }

    // Accessor methods
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
    public int getStock(){
        return stock;
    }
    public void setStock(int stock){
        this.stock=stock;
    }
    public void decrementStock(){
        stock--;
    }
    public void incrementStock(){
        stock++;
    }

    public void incrementStock(int quantity){
        stock+=quantity;
    }

    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price=price;
    }
    public List<Category> getCategories(){
        return categories;
    }
    public void setCategories(List<Category> categories){
        this.categories=categories;
    }

    public List<Long> getCatSelect(){
        return catSelect;
    }

    public void setCatSelect(List<Long> catSelect){
        this.catSelect = catSelect;
    }

}