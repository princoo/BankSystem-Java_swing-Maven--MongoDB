package com.example.Bank;

public class CurrentAccount extends BankAccount {

	/**
	 * 
	 */
	String tradeLicenseNumber;
//	String type;

	public CurrentAccount(String name, double balance,String tradeLicenseNumber) throws Exception {
		super(name, balance, 5000);
		this.tradeLicenseNumber= tradeLicenseNumber;
		if(balance<5000) throw new Exception("Insufficient Balance");
//		this.type="Current Account";
	}
	
	
}
