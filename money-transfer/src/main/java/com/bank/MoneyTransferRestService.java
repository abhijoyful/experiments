package com.bank;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bank.domain.Account;
import com.bank.service.AccountService;
import com.sun.jersey.api.core.InjectParam;

/**
 * Rest service to handle banking requests.
 * 
 * @author Abhishek Malik
 */
@Path("/")
public class MoneyTransferRestService {
	
	@InjectParam
	private AccountService accountService;

	@Path("createAcc/{initBal}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAccount(@PathParam("initBal") double initialBalance) {
		Response response = null;
		try {
			Account account = this.accountService.saveAccount(initialBalance);
			response = new Response(1, String.format("Success. Account number %s created with balance %s.",
					account.getAccountNumber(), account.getAccountBalance()));
		} catch (Exception ex) {
			response = new Response(0, "Error. "+ex.getMessage());
			ex.printStackTrace();
		}
		return response;
	}
	
	@Path("transfer/{accNo}/{accNo2}/{amount}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response transfer(@PathParam("accNo") int senderAccNum, @PathParam("accNo2") int receiverAccNum, @PathParam("amount") double amount) {
		Response response = null;
		try {
			this.accountService.transfer(senderAccNum, receiverAccNum, amount);
			response = new Response(1, String.format(
				"Success. Amount %s has been transferred from account number %s to account number %s.",
				amount, senderAccNum, receiverAccNum));
		} catch (Exception ex) {
			response = new Response(0, "Error. "+ex.getMessage());
			ex.printStackTrace();
		}
		return response;
	}
	
	@Path("checkBal/{accNo}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkBalance(@PathParam("accNo") int accNo) {
		Response response = null;
		try {
			double accountBal = this.accountService.checkBalance(accNo);
		    response = new Response(1, String.format( 
		    	String.format("Success. Balance of account %s is: %s.", accNo, accountBal)));
		} catch (Exception ex) {
			response = new Response(0, "Error. "+ex.getMessage());
			ex.printStackTrace();
		}
		return response;
	}

	/** Setter with package access specifier for unit testing under the same package **/
	void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

}