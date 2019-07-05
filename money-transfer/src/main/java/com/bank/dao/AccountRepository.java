package com.bank.dao;

import java.util.logging.Logger;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TransactionRequiredException;

import com.bank.domain.Account;
import com.bank.util.EntityManagerUtil;
import com.sun.jersey.spi.resource.Singleton;

/**
 * Repository class for Account entity transactions.
 * @author Abhishek Malik
 */
@Singleton
public class AccountRepository {
	
	private static final Logger LOG = Logger.getLogger(AccountRepository.class.getName());
	
	private EntityManager entityManager;
	
	public AccountRepository() {
		this.entityManager = EntityManagerUtil.getEntityManager();
	}
	
	public Account saveAccount(int accNo, double initialBalance) throws Exception {
		EntityTransaction tx = this.entityManager.getTransaction();
		Account account = new Account(accNo, initialBalance);
		tx.begin();
		try {
			this.entityManager.persist(account);
			tx.commit();
			return account;
		} catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException ex) {
			tx.rollback();
			ex.printStackTrace();
			throw new Exception("Account creation failed. System Error. " + ex.getMessage(), ex);
		}
	}
	
	public Account findAccount(int accountNumber) {
		return this.entityManager.find(Account.class, accountNumber);
	}
	
	public void transfer(int accountNumberToDebit, int accountNumberToCredit, double amount) throws Exception {
		if (this.entityManager.find(Account.class, accountNumberToDebit).getAccountBalance() < amount) {
			throw new UnsupportedOperationException("Debit failed. Insufficient Funds in source account.");
		}
		else {
			Account accountToDebit = this.entityManager.find(Account.class, accountNumberToDebit);
			accountToDebit.setAccountBalance(accountToDebit.getAccountBalance() - amount);
			Account accountToCredit = this.entityManager.find(Account.class, accountNumberToCredit);
			accountToCredit.setAccountBalance(accountToCredit.getAccountBalance() + amount);
			
			EntityTransaction tx = this.entityManager.getTransaction();
			try {
				tx.begin();
				LOG.info("Sender's account balance before transaction is: " + accountToDebit.getAccountBalance());
				LOG.info("Receiver's account balance before transaction is: " + accountToCredit.getAccountBalance());
				accountToDebit = this.entityManager.merge(accountToDebit);
				accountToCredit = this.entityManager.merge(accountToCredit);
				tx.commit();
				LOG.info("Sender's new account balance after transaction is: " + accountToDebit.getAccountBalance());
				LOG.info("Receiver's new account balance after transaction is: " + accountToCredit.getAccountBalance());
			} catch (Exception ex) {
				tx.rollback();
				throw new Exception("Money transfer failed. System error. " + ex.getMessage(), ex);
			}
		}
	}
	
	/** Setter with package access modifier for unit testing under the same package*/
	void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
