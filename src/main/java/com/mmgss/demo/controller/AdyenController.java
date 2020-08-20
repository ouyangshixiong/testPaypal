package com.mmgss.demo.controller;

import com.adyen.Client;
import com.adyen.enums.Environment;
import com.adyen.model.Amount;
import com.adyen.model.checkout.*;
import com.adyen.service.Checkout;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author alexouyang
 * @Date 2020-08-20
 */
@RestController
@RequestMapping("/adyen/")
public class AdyenController {

    String merchantAccount = "ApplasueECOM";
    String xApiKey = "AQEjhmfuXNWTK0Qc+iSRgnQ0pveOTS8UbLoxJdqpd+8aBOieXRUQwV1bDb7kfNy1WIxIIkxgBw==-N/rC/6AfvVCaEwwMs8xFpc3ONS3QETE1TOHAsEcpI1I=-F{Wna9f{)?_uFy_E";

    @RequestMapping(method = RequestMethod.POST, value = "get-payment-methods")
    public void getPaymentMethods() throws Exception{
        // Set your X-API-KEY with the API key from the Customer Area.
        Client client = new Client(xApiKey, Environment.TEST);

        Checkout checkout = new Checkout(client);
        PaymentMethodsRequest paymentMethodsRequest = new PaymentMethodsRequest();
        paymentMethodsRequest.setMerchantAccount(merchantAccount);
        paymentMethodsRequest.setCountryCode("US");
        paymentMethodsRequest.setShopperLocale("US");
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setValue(1000L);
        paymentMethodsRequest.setAmount(amount);
        paymentMethodsRequest.setChannel(PaymentMethodsRequest.ChannelEnum.WEB);
        PaymentMethodsResponse response = checkout.paymentMethods(paymentMethodsRequest);
        List<PaymentMethod> list =  response.getPaymentMethods();
        for(PaymentMethod method : list){
            System.out.println(method);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "pay")
    public void pay() throws Exception{
        // Set your X-API-KEY with the API key from the Customer Area.
        Client client = new Client(xApiKey,Environment.TEST);

        Checkout checkout = new Checkout(client);
        PaymentsRequest paymentsRequest = new PaymentsRequest();
        paymentsRequest.setMerchantAccount(merchantAccount);
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setValue(1000L);
        paymentsRequest.setAmount(amount);
        paymentsRequest.setReference("12341324132545245234");
        String encryptedCardNumber = "test_4111111111111111";
        String encryptedExpiryMonth = "test_03";
        String encryptedExpiryYear = "test_2030";
        String encryptedSecurityCode = "test_737";
        String holderName = "Ruby";
        paymentsRequest.setReference("rubypeng");
        paymentsRequest.addEncryptedCardData(encryptedCardNumber,encryptedExpiryMonth, encryptedExpiryYear, encryptedSecurityCode, holderName);
        paymentsRequest.setReturnUrl("http://localhost:8088/");
        PaymentsResponse paymentsResponse = checkout.payments(paymentsRequest);
        CheckoutPaymentsAction action = paymentsResponse.getAction();
        if( action == null ){
            System.out.println("no need any other action, payment finished!");
        }
        System.out.println(paymentsResponse.getResultCode());
    }

}
