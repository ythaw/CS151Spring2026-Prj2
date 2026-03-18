package model;
import interfaces.Deactivatable;
public abstract class Account implements Deactivatable {
    private String accountId;
    private String name;
    private String email;
    private String password;
    private boolean active;
    private String deactivationReason;

    public Account(String accountId, String name, String email, String password) {
        this.accountId = accountId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.active = true;
        this.deactivationReason = null;
    }
    public boolean login(String password){
        return this.password.equals(password) && active;
    }
    public void logout(){

    }
    public void updatePassword(String newPassword){
        this.password = newPassword;
    }
    public void updateEmail(String newEmail){
        this.email = newEmail;
    }
    public boolean hasValidEmail(){
        return email != null && email.contains("@");
    }
    public void deactivate(String reason){
        this.active = false;
        this.deactivationReason = reason;
    }
    public boolean isActive(){
        return active;
    }
    public String getStatus(){
        return active ? "Active" : "Deactivated: " + deactivationReason;
    }

    public abstract String getContactCard();
    
    public abstract String getRole();

}
