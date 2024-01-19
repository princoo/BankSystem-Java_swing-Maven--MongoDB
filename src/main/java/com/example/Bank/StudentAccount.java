package com.example.Bank;

public class StudentAccount extends SavingsAccount {
/**
	 * 
	 */
String  institutionName;

public StudentAccount(String name, double balance ,String  institutionName) {
	super(name, balance, 20000);
	min_balance=100;
	this.institutionName=institutionName;
//	this.type="Stuedent Account";
}



}
