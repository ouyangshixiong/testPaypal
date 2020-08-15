package com.mmgss.demo.service;

import java.util.ArrayList;
import java.util.List;

import com.mmgss.demo.config.PaypalPaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.mmgss.demo.config.PaypalPaymentIntent;

/**
 * 支付service类
 */
@Service
public class PaypalService {
	//注入认证信息bean
    @Autowired
    private APIContext apiContext;

    /**
     * 支付方法
     * @param total  交易金额
     * @param currency 货币类型
     * @param method  枚举-作用
     * @param intent  枚举-意图
     * @param description  交易描述
     * @param cancelUrl  交易取消后跳转url
     * @param successUrl  交易成功后跳转url
     * @return
     * @throws PayPalRESTException
     */
    public Payment createPayment(
            Double total, 
            String currency, 
            PaypalPaymentMethod method,
            PaypalPaymentIntent intent, 
            String description, 
            String cancelUrl, 
            String successUrl) throws PayPalRESTException{
    	//设置金额和单位对象
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));
        //设置具体的交易对象
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        //交易集合-可以添加多个交易对象
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        //设置反馈url
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        //加入反馈对象
        payment.setRedirectUrls(redirectUrls);
        //加入认证并创建交易
        return payment.create(apiContext);
    }

    /**
     * 并执行交易（相当于提交事务）
     * @param paymentId
     * @param payerId
     * @return
     * @throws PayPalRESTException
     */
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }
}
