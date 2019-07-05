package com.bank.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.Test;
import org.mockito.Mockito;

import com.bank.domain.Account;

/**
 * Test class for AccountRepository.
 * @author Abhishek Malik
 *
 */
public class AccountRepositoryTest {
	AccountRepository accountRepository = new AccountRepository();
	EntityManager entityManager = mock(EntityManager.class);
	EntityTransaction tx = mock(EntityTransaction.class);
	
	// one time setup
	{
		accountRepository.setEntityManager(entityManager);
		when(entityManager.getTransaction()).thenReturn(tx);
	}
	
	@Test
	public void testSaveAccountSuccess() throws Exception {
		Account account = this.accountRepository.saveAccount(123456, 1000.00);
		assertNotNull(account);
		assertEquals(1000.0, account.getAccountBalance(), 0.0);
		assertEquals(123456, account.getAccountNumber());
		verify(this.tx, times(1)).begin();
		verify(this.tx, times(1)).commit();
		verify(this.tx, times(0)).rollback();
	}
	
	@Test(expected=Exception.class)
	public void testSaveAccountFailShouldRollback() throws Exception {
		doThrow(IllegalArgumentException.class).when(this.entityManager).persist(Mockito.any());
		this.accountRepository.saveAccount(123456, 1000.00);
		verify(this.tx, times(1)).begin();
		verify(this.tx, times(1)).commit();
		verify(this.tx, times(1)).rollback();
	}
	
	@Test
	public void testFindAccountSuccess() {
		when(this.entityManager.find(Account.class, 123456)).thenReturn(new Account(123456));
		assertEquals(123456, accountRepository.findAccount(123456).getAccountNumber());
	}
	
	@Test
	public void testFindAccountFail() {
		assertNull(accountRepository.findAccount(787895));
	}
	
	@Test
	public void testTransferSuccess() throws Exception {
		Account accountToDebit = new Account(123456, 1400.00);
		Account accountToCredit = new Account(343434, 1000.00);
		double amount = 1200.00;
		when(entityManager.find(Account.class, 123456)).thenReturn(accountToDebit);
		when(entityManager.find(Account.class, 343434)).thenReturn(accountToCredit);
		when(entityManager.merge(accountToDebit)).thenReturn(new Account(123456, 200.00));
		when(entityManager.merge(accountToCredit)).thenReturn(new Account(123456, 2200.00));
		accountRepository.transfer(123456, 343434, amount);
		verify(this.tx, times(1)).begin();
		verify(this.tx, times(1)).commit();
		verify(this.tx, times(0)).rollback();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testTransferFailInsufficientFundsNonTransactional() throws Exception {
		Account accountToDebit = new Account(123456, 1000.00);
		double amountToDebit = 1200.00;
		when(entityManager.find(Account.class, 123456)).thenReturn(accountToDebit);
		accountRepository.transfer(123456, 343434, amountToDebit);
		verify(this.tx, times(0)).begin();
		verify(this.tx, times(0)).commit();
		verify(this.tx, times(0)).rollback();
	}
	
	@Test(expected = Exception.class)
	public void testDebitMoneyWhenFailShouldRollback() throws Exception {
		accountRepository.transfer(123456, 456783, 100.00);
		verify(this.tx, times(1)).begin();
		verify(this.tx, times(1)).commit();
		verify(this.tx, times(1)).rollback();
	}
	
	
}
