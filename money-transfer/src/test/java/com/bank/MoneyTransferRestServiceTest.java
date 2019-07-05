package com.bank;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import com.bank.domain.Account;
import com.bank.service.AccountService;

/**
 * Test class for MoneyTransferRestService.
 * @author Abhishek Malik
 */
public class MoneyTransferRestServiceTest {
	
	MoneyTransferRestService moneyTransferRestService = new MoneyTransferRestService();
	AccountService accountService = Mockito.mock(AccountService.class);
	
	// one time setup
	{
		moneyTransferRestService.setAccountService(accountService);
	}
	
	@Test
	public void testCreateAccountWhenPassThenSuccess() throws Exception {
		Account account = new Account(123, 5434.00);
		Mockito.when(this.accountService.saveAccount(5434.00)).thenReturn(account);
		Response response = this.moneyTransferRestService.createAccount(5434.00);
		assertNotNull(response);
		assertEquals("Success. Account number 123 created with balance 5434.0.", response.getMessage());
		assertEquals(1, response.getStatus());
	}
	
	@Test
	public void testCreateAccountWhenFailThenError() throws Exception {
		Mockito.when(this.accountService.saveAccount(5434.00)).thenReturn(null);
		Response response = this.moneyTransferRestService.createAccount(5434.00);
		assertNotNull(response);
		assertThat(response.getStatus(), is(0));
		assertThat(response.getMessage(), startsWith("Error."));
	}
	
	@Test
	public void testTransferWhenPassThenSuccess() throws Exception {
		Mockito.doNothing().when(this.accountService).transfer(123, 456, 100.00);
		Response response = this.moneyTransferRestService.transfer(123, 456, 100.00);
		assertNotNull(response);
		assertEquals(1, response.getStatus());
		assertEquals("Success. Amount 100.0 has been transferred from account number 123 to account number 456.",
				response.getMessage());
	}
	
	@Test
	public void testCheckBalanceWhenPassThenSuccess() throws Exception {
		Mockito.when(this.accountService.checkBalance(123)).thenReturn(5434.00);
		Response response = this.moneyTransferRestService.checkBalance(123);
		assertNotNull(response);
		assertEquals(1, response.getStatus());
		assertEquals("Success. Balance of account 123 is: 5434.0.", response.getMessage());
	}
	
	@Test
	public void testCheckBalanceWhenFailThenError() throws Exception {
		when(this.accountService.checkBalance(123)).thenThrow(new Exception());
		Response response = this.moneyTransferRestService.checkBalance(123);
		assertNotNull(response);
		assertThat(response.getStatus(), is(0));
		assertThat(response.getMessage(), startsWith("Error."));
	}
}
