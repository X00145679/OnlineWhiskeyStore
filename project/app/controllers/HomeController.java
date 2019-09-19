package controllers;

import play.mvc.*;
import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.FilePart;
import java.io.File;
import java.io.IOException;
import java.awt.image.*;
import javax.imageio.*;
import org.imgscalr.*;
import views.html.*;
import play.api.Environment;
import play.data.*;
import play.db.ebean.Transactional;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import models.*;
import models.users.*;
import models.products.*;

public class HomeController extends Controller {

    
    private FormFactory formFactory;
    private Environment e;
    @Inject
    public HomeController(FormFactory f, Environment env){
        this.e = env;
        this.formFactory=f;
    }



    public Result index() {
        return ok(index.render(User.getUserById(session().get("email"))));
    }

    public Result onsale(Long cat) {
        List<ItemOnSale> itemList = null;
        List<Category> categoryList = Category.findAll();

        if(cat==0){
            itemList = ItemOnSale.findAll();
        }else{
            itemList= Category.find.ref(cat).getItems();
        }
        return ok(onsale.render(itemList, categoryList,User.getUserById(session().get("email")),e));
        
     }

    @Security.Authenticated(Secured.class)
    @With(AuthAdmin.class)
    public Result addItem() {
        Form<ItemOnSale> itemForm = formFactory.form(ItemOnSale.class);
        return ok(addItem.render(itemForm,User.getUserById(session().get("email"))));
    }

    @Security.Authenticated(Secured.class)
    @With(AuthAdmin.class)
    @Transactional
    public Result deleteItem(Long id){
        ItemOnSale.find.ref(id).delete();
        flash("success", "Item has been deleted.");
        return redirect(controllers.routes.HomeController.onsale(0));
    }

    @Security.Authenticated(Secured.class)
    @With(AuthAdmin.class)
    @Transactional
    public Result addItemSubmit() {
        Form<ItemOnSale> newItemForm = formFactory.form(ItemOnSale.class).bindFromRequest();
         if (newItemForm.hasErrors()) {
            return badRequest(addItem.render(newItemForm,User.getUserById(session().get("email"))));
        } else {
            ItemOnSale newItem = newItemForm.get();

            List<Category> newCats = new ArrayList<Category>();
            for (Long cat : newItem.getCatSelect()) {
                newCats.add(Category.find.byId(cat));
        }
        newItem.setCategories (newCats);
        
        if(newItem.getId()==null){
        newItem.save();
            }else{
                newItem.update();
            }
    
            MultipartFormData<File> data = request().body().asMultipartFormData();
            FilePart<File> image = data.getFile("upload");
            String saveImageMessage = saveFile(newItem.getId(),image);
            flash("success", "Item " + newItem.getName() + " was added/updated." +saveImageMessage);
            return redirect(controllers.routes.HomeController.onsale(0));
        }
    }

    @Security.Authenticated(Secured.class)
    public Result updateItem(Long id){
        ItemOnSale i;
        Form<ItemOnSale> itemForm;
        try{
            i = ItemOnSale.find.byId(id);
            itemForm = formFactory.form(ItemOnSale.class).fill(i);
        } catch (Exception ex){
            return badRequest("error");
        }
        return ok(addItem.render(itemForm,User.getUserById(session().get("email"))));

    }

    @Security.Authenticated(Secured.class)
    @Transactional
    @With(AuthAdmin.class)
    public Result deleteAdmin(String email) {
    
       
        Administrator u = (Administrator) User.getUserById(email);
        u.delete();
    
        flash("success", "User has been deleted.");
        return redirect(controllers.routes.HomeController.usersAdmin());
    }
    @Security.Authenticated(Secured.class)
    public Result updateAdmin(String email) {
        Administrator u;
        Form<Administrator> userForm;
    
        try {
            u = (Administrator)User.getUserById(email);
            u.update();
    
            userForm = formFactory.form(Administrator.class).fill(u);
        } catch (Exception ex) {
            return badRequest("error");
        }
    
        return ok(addAdmin.render(userForm,User.getUserById(session().get("email"))));
    }
    
    @Security.Authenticated(Secured.class)
    public Result addAdmin() {
        Form<Administrator> userForm = formFactory.form(Administrator.class);
        return ok(addAdmin.render(userForm,User.getUserById(session().get("email"))));
    }
    @Security.Authenticated(Secured.class)
    @Transactional
    public Result addAdminSubmit() {
    Form<Administrator> newUserForm = formFactory.form(Administrator.class).bindFromRequest();
    if (newUserForm.hasErrors()) {
        
        return badRequest(addAdmin.render(newUserForm,User.getUserById(session().get("email"))));
    } else {
        Administrator newUser = newUserForm.get();
        System.out.println("Name: "+newUserForm.field("name").getValue().get());
        System.out.println("Email: "+newUserForm.field("email").getValue().get());
        System.out.println("Password: "+newUserForm.field("password").getValue().get());
        System.out.println("Role: "+newUserForm.field("role").getValue().get());
        
        if(User.getUserById(newUser.getEmail())==null){
            newUser.save();
        }else{
            newUser.update();
        }
        flash("success", "User " + newUser.getName() + " was added/updated.");
        return redirect(controllers.routes.HomeController.usersAdmin()); 
        }
    }
    @Security.Authenticated(Secured.class)
    public Result addCustomer() {
        Form<Customer> cForm = formFactory.form(Customer.class);
        return ok(addCustomer.render(cForm,User.getUserById(session().get("email"))));
    }
    @Security.Authenticated(Secured.class)
    @Transactional
    public Result addCustomerSubmit() {
    Form<Customer> newUserForm = formFactory.form(Customer.class).bindFromRequest();
    if (newUserForm.hasErrors()) {
        
        return badRequest(addCustomer.render(newUserForm,User.getUserById(session().get("email"))));
    } else {
        Customer newUser = newUserForm.get();
        System.out.println("Name: "+newUserForm.field("name").getValue().get());
        System.out.println("Email: "+newUserForm.field("email").getValue().get());
        System.out.println("Password: "+newUserForm.field("password").getValue().get());
        System.out.println("Role: "+newUserForm.field("role").getValue().get());
        
        if(User.getUserById(newUser.getEmail())==null){
            newUser.save();
        }else{
            newUser.update();
        }
        flash("success", "User " + newUser.getName() + " was added/updated.");
        return redirect(controllers.routes.HomeController.usersCustomer()); 
        }
    }
    @Security.Authenticated(Secured.class)
    @Transactional
    @With(AuthAdmin.class)
    public Result deleteCustomer(String email) {
    
        
        Customer u = (Customer) User.getUserById(email);
        u.delete();
    
        flash("success", "User has been deleted.");
        return redirect(controllers.routes.HomeController.usersCustomer());
    }
    @Security.Authenticated(Secured.class)
    public Result updateCustomer(String email) {
        Customer u;
        Form<Customer> userForm;
    
        try {
           u = (Customer) User.getUserById(email);
            u.update();
    
            userForm = formFactory.form(Customer.class).fill(u);
        } catch (Exception ex) {
            return badRequest("error");
        }
    
       
        return ok(addCustomer.render(userForm,User.getUserById(session().get("email"))));
    }
    @Security.Authenticated(Secured.class)
    @With(AuthAdmin.class)
    public Result usersAdmin() {
        List<Administrator> userList = null;
    
        userList = Administrator.findAll();
    
        return ok(admin.render(userList,User.getUserById(session().get("email"))));
    
     }
     @Security.Authenticated(Secured.class)
     @With(AuthAdmin.class)
     public Result usersCustomer() {
        List<Customer> cList = null;
    
        cList = Customer.findAll();
    
        return ok(customers.render(cList,User.getUserById(session().get("email"))));
    
     }

public String saveFile(Long id,FilePart<File> uploaded){

    if(uploaded!=null) {

        String mimeType = uploaded.getContentType();
        if(mimeType.startsWith("image/")) {
            String fileName = uploaded.getFilename();
            String extension = "";
            int i = fileName.lastIndexOf('.');
            if(i>=0){
                extension = fileName.substring(i+1);
            }
            File file = uploaded.getFile();
            File dir = new File("public/images/productImages");
            if(!dir.exists()){
                dir.mkdirs();
            }

            File newFile = new File("public/images/productImages/", id+ "." + extension);
            if(file.renameTo(newFile)){
            try{
                BufferedImage img = ImageIO.read(newFile);
                BufferedImage scaledImg = Scalr.resize(img,90);
                

                if(ImageIO.write(scaledImg, extension, new File("public/images/productImages/",id + "thumb.jpg"))) {
                    return "/ file uploaded and thumbnail created.";
                } else {
                    return "/ file uploaded but thumbnail creation failed";
                }}catch (IOException e){
                return "/file uploaded but thumbnail creation failed.";
            }
        }} else {
     return "/ file upload failed";
    }
 } return "";
 }
   

}
