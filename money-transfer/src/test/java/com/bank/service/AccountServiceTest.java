package com.bank.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityExistsException;

import org.junit.Test;
import org.mockito.Mockito;

import com.bank.dao.AccountRepository;
import com.bank.domain.Account;

/**
 * Test class for AccountService class.
 * @author Abhishek Malik
 */
public class AccountServiceTest {

	AccountService accountService = new AccountService();
	AccountRepository accountRepository = mock(AccountRepository.class);
	
	// one time setup
	{
		accountService.setAccountRepository(this.accountRepository);
	}
	
	@Test
	public void testSaveAccountSuccess() throws Exception {
		Account inAccount = new Account(123456, 101.00);
		when(this.accountRepository.saveAccount(Mockito.anyInt(), Mockito.anyDouble())).thenReturn(inAccount);
		Account account = this.accountService.saveAccount(101.00);
		assertEquals(101.0, account.getAccountBalance(), 0.0);
	}
	
	@Test(expected = Exception.class)
	public void testSaveAccountError() throws Exception {
		doThrow(EntityExistsException.class).when(this.accountRepository.saveAccount(Mockito.anyInt(), Mockito.anyDouble()));
		this.accountService.saveAccount(101.00);
	}
	
	@Test
	public void testTransferSuccess() throws Exception {
		Account senderAcc = new Account(123, 500.00);
		Account receiverAcc = new Account(456, 500.00);
		when(this.accountRepository.findAccount(123)).thenReturn(senderAcc);
		when(this.accountRepository.findAccount(456)).thenReturn(receiverAcc);
		this.accountService.transfer(123, 456, 100.00);
		verify(this.accountRepository, times(1)).transfer(123, 456, 100.00);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testTransferWhenInvalidSenderAccThenException() throws Exception {
		Account receiverAcc = new Account(456, 500.00);
		when(this.accountRepository.findAccount(123)).thenReturn(null);
		when(this.accountRepository.findAccount(456)).thenReturn(receiverAcc);
		this.accountService.transfer(657, 456, 100.00);
		verify(this.accountRepository, times(0)).transfer(123, 456, 100.00);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testTransferWhenInvalidReceiverAccThenException() throws Exception {
		Account senderAcc = new Account(123, 500.00);
		when(this.accountRepository.findAccount(123)).thenReturn(senderAcc);
		when(this.accountRepository.findAccount(456)).thenReturn(null);
		this.accountService.transfer(123, 989, 100.00);
		verify(this.accountRepository, times(0)).transfer(123, 456, 100.00);
	}
	
	@Test
	public void testCheckBalanceSucccess() throws Exception {
		when(this.accountRepository.findAccount(123456)).thenReturn(new Account(123456, 2300.00));
		assertEquals(2300.00, this.accountService.checkBalance(123456), 0.00);
	}
	
	@Test(expected = Exception.class)
	public void testCheckBalanceFail() throws Exception {
		when(this.accountRepository.findAccount(123456)).thenReturn(null);
		this.accountService.checkBalance(123456);
	}
	
}
