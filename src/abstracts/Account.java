package abstracts;

import interfaces.Deactivatable;

public abstracts class Account	implements Deactivatable {
	protected String accountId;
	protected String name;
	protected String email;
	protected String password;
	protected boolean active;
	protected String deactivationReason;
	
	public Account(String id, String name, String email, String password) {
		this.accountId = id;
		this.name = name;
		this.email = email;
		this.active = true;
	}
}
