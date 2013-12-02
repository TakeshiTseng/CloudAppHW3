package tw.ttucse.cloudhw1.client;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable
public class User implements Serializable{
	private static final long serialVersionUID = -5887296503614852877L;

	@PrimaryKey 
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long ID;
	
	@Persistent
	private String account;

	@Persistent
	private String username="";

	@Persistent
	private String password;

	public User(String account, String password) {
		this.account = account;
		this.password = password;
	}
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	@Override
	public boolean equals(Object obj) {
		return account.equals(((User)obj).getAccount());
	}
}
