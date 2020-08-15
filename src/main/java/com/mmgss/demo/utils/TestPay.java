package com.mmgss.demo.utils;

import java.util.ArrayList;
import java.util.List;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

public class TestPay {

	public static void main(String[] args) {
		
		String clientId = "AYR8ARCQj_51KQ4fyEvUx0LXDl7h9jWAQW90bf5NiilWW0kpqvCMQl1TBtkanJPTQHRTn1NC0B60afKf";
		String clientSecret = "ELrzMb8kf3wyqLGtal9zuimOOLw7F5w9itjoK6fq9ZbrJYXUCEBRsQfXp_DVlyCyWNipMt4HNRv2UhAO";

		Amount amount = new Amount();
		amount.setCurrency("USD");
		amount.setTotal("10.00");

		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);

		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl("https://example.com/cancel");
		redirectUrls.setReturnUrl("https://example.com/return");
		payment.setRedirectUrls(redirectUrls);
		
		try {
		    APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
		    Payment createdPayment = payment.create(apiContext);
		    // For debug purposes only: System.out.println(createdPayment.toString());
		    System.out.println(createdPayment.toString());
		} catch (PayPalRESTException e) {
		    // Handle errors
		} catch (Exception ex) {
		    // Handle errors
		}

	}

}
