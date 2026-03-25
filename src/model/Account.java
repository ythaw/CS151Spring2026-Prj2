package model;

import interfaces.Deactivatable;

public abstract class Account implements Deactivatable {
	protected String accountId;
	protected String name;
	protected String email;
	protected String password;
	protected boolean active;
	protected String deactivationReason;
	
	public Account(String accountID, String name, String email, String password) {
		this.accountId = accountId;
		this.name = name;
		this.email = email;
		this.active = true;
		this.deactivationReason = " ";
	}
	
	// login account
	public boolean login(String password) {
		return this.password.equals(password) && active;
	}
	
	// Logs out account
	public void logout() {
		System.out.println(name + "Logged out");
	}
	
	// Update account password with valiation
	public void updatePassword(String oldPassword, String newPassword) {
		if (!this.password.equals(oldPassword)) {
			throw new IllegalArgumentException("Old password is incorrect.");
		}
		if (newPassword == null || newPassword.trim().isEmpty()) {
			throw new IllegalArgumentException("New password cannot be empty.");
		}
		this.password = newPassword;
	}
	
	// Update email with basic valiation
	public void updateEmail(String newEmail) {
        if (newEmail == null || newEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        this.email = newEmail;
    }
	
	// Check is email format is valid
	public boolean hasValidEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }
	
	// Retuurn formatted contact information
    public String getContactCard() {
        return "ID: " + accountId + "\n"
                + "Name: " + name + "\n"
                + "Email: " + email + "\n"
                + "Status: " + getStatus();
    }
    
    // Deactives the account with a reason
     @Override
    public void deactivate(String reason) {
        this.active = false;
        this.deactivationReason = reason;
    }
    
    // Reactives the account
     @Override
    public void activate() {
        this.active = true;
        this.deactivationReason = "";
    }
    
    // Check if account is active
    @Override
    public boolean isActive() {
        return active;
    }
    
    // Return account status
    @Override
    public String getStatus() {
        return active ? "Active" : "Inactive: " + deactivationReason;
    }

	// Getter for account ID, name, email
	public String getAccount(){
		return accountId;
	}
    
    public String getName(){
		return name;
	}
	
	public String getEmail(){
		return email;
	}
	
	// Abstract methoad to define role (Studen, Professor,..)
	public abstract String getRole();
}
