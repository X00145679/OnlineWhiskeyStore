package models.products;

import java.util.*;
import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

// Product entity managed by Ebean
@Entity
public class Category extends Model {

   // Fields
   // Annotate id as primary key
   @Id
   private Long id;

   @Constraints.Required
   private String name;

   // Category contains many products
   @ManyToMany(cascade = CascadeType.ALL)
   private List<ItemOnSale> items;

   // Default constructor
   public  Category() {
   }
			    
   public  Category(Long id, String name, List<ItemOnSale> items) {
      this.id = id;
      this.name = name;
      this.items = items;
   }

   public static Map<String,String> options() {
    LinkedHashMap<String,String> options = new LinkedHashMap();
 
    // Get all the categories from the database and add them to the options hash map
    for (Category c: Category.findAll()) {
       options.put(c.getId().toString(), c.getName());
    }
    return options;
 }

public static boolean inCategory(Long category, Long item) {
    return find.query().where().eq("items.id",item)
                        .eq("id",category)
                        .findList().size()>0;
}

   public void setId(Long id){
       this.id=id;
   }
   public void setName(String name){
       this.name=name;
   }
   public void setItems(List<ItemOnSale> items){
       this.items=items;
   }

   public Long getId(){
       return id;
   }
   public String getName(){
       return name;
   }
   public List<ItemOnSale> getItems(){
       return items;
   }

   //Generic query helper for entity Computer with id Long
public static Finder<Long,Category> find = new Finder<Long,Category>(Category.class);

//Find all Products in the database
public static List<Category> findAll() {
   return Category.find.query().where().orderBy("name asc").findList();
}

}