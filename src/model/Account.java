package model;

import interfaces.Deactivatable;

public abstract class Account implements Deactivatable {
    protected String accountId;
    protected String name;
    protected String email;
    protected String password;
    protected boolean active;
    protected String deactivationReason;

    public Account(String accountId, String name, String email, String password) {
		
	if (accountId == null || accountId.trim().isEmpty()) {
        throw new IllegalArgumentException("Account ID cannot be empty.");
    }

    if (name == null) {
        throw new IllegalArgumentException("Name cannot be empty.");
    }
    name = name.trim();
    if (name.isEmpty()) {
        throw new IllegalArgumentException("Name cannot be empty.");
    }

    if (email == null || email.trim().isEmpty()) {
        throw new IllegalArgumentException("Email cannot be empty.");
    }
    email = email.trim();
    if (!email.contains("@") || !email.contains(".")) {
        throw new IllegalArgumentException("Email format is invalid.");
    }

    if (password == null || password.trim().isEmpty()) {
        throw new IllegalArgumentException("Password cannot be empty.");
    }

    this.accountId = accountId.trim();
    this.name = name;
    this.email = email;
    this.password = password;
    this.active = true;
    this.deactivationReason = "";
    
    }

    // Login account
    public boolean login(String password) {
        return this.password.equals(password) && active;
    }

    // Log out account
    public void logout() {
        System.out.println(name + " logged out");
    }

    // Update account password with validation
    public void updatePassword(String oldPassword, String newPassword) {
        if (!this.password.equals(oldPassword)) {
            throw new IllegalArgumentException("Old password is incorrect.");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty.");
        }
        this.password = newPassword;
    }

    // Update email with basic validation
    public void updateEmail(String newEmail) {
        if (newEmail == null || newEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        String trimmed = newEmail.trim();
        if (!hasValidEmail(trimmed)) {
            throw new IllegalArgumentException("Email format is invalid (must contain '@' and '.').");
        }
        this.email = trimmed;
    }


    // Check if email format is valid
    public boolean hasValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    // Return formatted contact information
    public String getContactCard() {
        return "ID: " + accountId + "\n"
                + "Name: " + name + "\n"
                + "Email: " + email + "\n"
                + "Status: " + getStatus();
    }

    // Deactivate the account with a reason
    @Override
    public void deactivate(String reason) {
        this.active = false;
        this.deactivationReason = reason;
    }

    // Reactivate the account
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

    // Getters
    public String getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // Abstract method to define role
    public abstract String getRole();
}
