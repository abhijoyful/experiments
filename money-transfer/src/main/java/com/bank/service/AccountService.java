package com.bank.service;

import java.util.Random;

import com.bank.dao.AccountRepository;
import com.bank.domain.Account;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.resource.Singleton;

/**
 * Account Service to handle transactions business logic underneath Rest service.
 * @author Abhishek Malik
 */
@Singleton
public class AccountService {

	
	@InjectParam
	AccountRepository accountRepository;
	
	public Account saveAccount(double initialBalance) throws Exception {
			int accNo = (new Random().nextInt(900000))+100000;  // random 6 digit number from 100000 to 999999
			return accountRepository.saveAccount(accNo, initialBalance);
	}
	
	public void transfer(int senderAccNo, int receiverAccNo, double money) throws Exception {
		Account senderAccount = accountRepository.findAccount(senderAccNo);
		Account receiverAccount = accountRepository.findAccount(receiverAccNo);
		if (senderAccount == null || receiverAccount == null) {
			throw new UnsupportedOperationException("Invalid account number for sender or receiver account.");
		}
		accountRepository.transfer(senderAccNo, receiverAccNo, money);
	}

	public double checkBalance(int accNo) throws Exception {
		Account account = accountRepository.findAccount(accNo);
		if (account == null) {
			throw new Exception("Account balance not available as account does not exist");
		}
		return account.getAccountBalance();
	}
	
	/** Setter with package access specifier for unit testing under the same package **/
	void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

}
