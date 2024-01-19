package com.example.Bank;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.bson.Document;

import com.example.DatabaseUtil;
import com.example.Exceptions.AccNotFound;
import com.example.Exceptions.InvalidAmount;
import com.example.Exceptions.MaxBalance;
import com.example.Exceptions.MaxWithdraw;
import com.example.GUI.DisplayList;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Bank implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<BankAccount> accountList = new ArrayList<>();
	// private BankAccount[] accounts = new BankAccount[100];

	public int addAccount(BankAccount acc) {
		int i = 0;
		for (i = 0; i < 100; i++) {
			if (getAccounts()[i] == null) {
				break;
			}
		}
		getAccounts()[i] = acc;
		return i;
	}

	public int addAccount(String name, double balance, double maxWithLimit) {
		SavingsAccount acc = new SavingsAccount(name, balance, maxWithLimit);
		return this.addAccount(acc);
	}

	public int addAccount(String name, double balance, String tradeLicense) throws Exception {
		CurrentAccount acc = new CurrentAccount(name, balance, tradeLicense);
		return this.addAccount(acc);
	}

	public int addAccount(String name, String institutionName, double balance, double min_balance) {
		StudentAccount acc = new StudentAccount(name, balance, institutionName);
		return this.addAccount(acc);
	}

	public BankAccount findAccount(String aacountNum) {
		int i;
		for (i = 0; i < 100; i++) {
			if (getAccounts()[i] == null) {
				break;
			}
			if (getAccounts()[i].id.equals(aacountNum)) {
				System.out.println(getAccounts()[i]);
				return getAccounts()[i];
			}

		}
		return null;
	}

	public void deposit(String aacountNum, double amt) throws InvalidAmount, AccNotFound

	{
		if (amt < 0) {
			throw new InvalidAmount("Invalid Deposit amount");
		}
		BankAccount temp = findAccount(aacountNum);
		if (temp == null) {
			throw new AccNotFound("Account Not Found");
		}
		if (temp != null) {
			temp.deposit(amt, aacountNum);

		}

	}

	public void withdraw(String aacountNum, double amt) throws MaxBalance, AccNotFound, MaxWithdraw, InvalidAmount {
		BankAccount temp = findAccount(aacountNum);
		System.out.println(temp);
		if (temp == null) {
			throw new AccNotFound("Account Not Found");
		}

		if (amt <= 0) {
			throw new InvalidAmount("Invalid Amount");
		}

		if (temp.type == "Saving Account" && temp.maxWithdraw > amt) {
			throw new MaxBalance("Maximum Withdraw Limit Exceed");
		}
		if (temp != null) {
			System.out.println(temp.type);
			System.out.println(temp.min_balance);
			temp.withdraw(amt, aacountNum);
		}
	}

	public DefaultListModel<String> display() {
		DefaultListModel<String> list = new DefaultListModel<String>();
		int i;
		// String type=null;

		for (i = 0; i < 100; i++) {
			if (getAccounts()[i] == null) {
				break;
			}
			// if(getAccounts()[i].getClass().toString().equals("class
			// Bank.SavingsAccount"))
			// {
			// type="Type: Savings Account";
			// }
			//
			// else if(getAccounts()[i].getClass().toString().equals("class
			// Bank.CurrentAccount"))
			// {
			// type="Type: Current Account";
			// }
			//
			// else if(getAccounts()[i].getClass().toString().equals("class
			// Bank.StudentAccount"))
			// {
			// type="Type: Student Account";
			// }

			list.addElement(getAccounts()[i].toString());

		}

		return list;
	}

	public BankAccount[] getAccounts() {
		int index = 0;
		try {
			DatabaseUtil.openConnection();
			MongoDatabase database = DatabaseUtil.getDatabase();
			MongoCollection<Document> collection = database.getCollection("SavingAccounts");
			FindIterable<Document> documents = collection.find();

			MongoCursor<Document> cursor = documents.iterator();
			while (cursor.hasNext() && index < 100) {
				Document document = cursor.next();
				// Retrieve fields from the document
				String id = document.getObjectId("_id").toString();
				String name = document.getString("name");
				double balance = document.getDouble("balance");
				String accountType = document.getString("type");
				double maxWithdraw = document.containsKey("maxWithdraw") ? document.getDouble("maxWithdraw") : 0.0;
				// Create BankAccount object and add it to the array
				BankAccount account = new BankAccount(id, name, balance, accountType, maxWithdraw);
				accountList.add(account);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			DatabaseUtil.closeConnection();
		}
		BankAccount[] accountArray = accountList.toArray(new BankAccount[0]);
		return accountArray;
	}

	public void setAccounts(BankAccount[] accounts) {
		// this.accounts = accounts;
	}

}
