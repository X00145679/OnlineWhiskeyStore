package models.users;

public class Login{

    private String email;
    private String password;


    public String validate(){

        if(User.authenticate(email,password)==null){
            return "Invalid username or password";
        }
        return null;
    }

    //getters and setters
    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public String getPassword(){
        return password;

    }

    public void setPassword(String password){
        this.password = password;

    }


}