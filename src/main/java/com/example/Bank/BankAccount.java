package com.example.Bank;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.example.DatabaseUtil;
import com.example.Exceptions.AccNotFound;
import com.example.Exceptions.InvalidAmount;
import com.example.Exceptions.MaxBalance;
import com.example.Exceptions.MaxWithdraw;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;

public class BankAccount {

	/**
	 * 
	 */
	String name;
	double balance;
	double min_balance;
	String acc_num;
	String type;
	String id;
	double maxWithdraw;
	// String type;

	public BankAccount(String name, double balance, double min_balance) {
		this.name = name;
		this.balance = balance;
		this.min_balance = min_balance;
		acc_num = 10000 + (int) (Math.random() * 89999) + "";
	}

	public BankAccount(String id, String name, double balance, String type,double maxWithdraw) {
		this.name = name;
		this.balance = balance;
		this.type = type;
		this.id = id;
		this.maxWithdraw = maxWithdraw;
	}

	public void deposit(double amount, String accNumber) throws InvalidAmount, AccNotFound {
		try {
			DatabaseUtil.openConnection();
			MongoDatabase database = DatabaseUtil.getDatabase();
			MongoCollection<Document> collection = database.getCollection("SavingAccounts");
			ObjectId objectId = new ObjectId(accNumber);

			Document updatedDocument = collection.findOneAndUpdate(
					Filters.eq("_id", objectId),
					Updates.inc("balance", amount),
					new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));

			if (updatedDocument == null) {
				throw new AccNotFound("Account Not Found");
			}

		} catch (Exception e) {
			System.err.println("Error updating account balance: " + e.getMessage());
		} finally {
			DatabaseUtil.closeConnection();
		}
		// balance += amount;
	}

	public void withdraw(double amount,String AccountNumber) throws MaxWithdraw, MaxBalance {
		if (amount <= maxWithdraw && amount < balance) {
			try {
				DatabaseUtil.openConnection();
				MongoDatabase database = DatabaseUtil.getDatabase();
				MongoCollection<Document> collection = database.getCollection("SavingAccounts");
				ObjectId objectId = new ObjectId(AccountNumber);
				Document updatedDocument = collection.findOneAndUpdate(
						Filters.eq("_id", objectId),
						Updates.inc("balance", -amount),
						new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));
	
				if (updatedDocument == null) {
					throw new AccNotFound("Account Not Found");
				}
	
			} catch (Exception e) {
				System.err.println("Error withdrawing account money: " + e.getMessage());
			} finally {
				DatabaseUtil.closeConnection();
			}
			// balance -= amount;
		}
		else if(amount > maxWithdraw){
			throw new MaxWithdraw("Maximum Withdraw Limit Exceed");
		}
		else {
			System.out.println("inside");
			System.out.println("amount" + amount);
			System.out.println("max" + maxWithdraw);
			throw new MaxBalance("Insufficient Balance");
		}
	}

	public double getbalance() {
		return balance;
	}

	@Override
	public String toString() {
		return "Name: " + name + ", Id: " + id + ", Balance: " + balance + "Type:" + type + "Max widthraw:" + maxWithdraw;
	}
}
