package com.mmgss.demo.controller;

import javax.servlet.http.HttpServletRequest;

import com.mmgss.demo.config.PaypalPaymentMethod;
import com.mmgss.demo.utils.URLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.mmgss.demo.config.PaypalPaymentIntent;
import com.mmgss.demo.service.PaypalService;

@Controller
@RequestMapping("/")
public class PaymentController {

    public static final String PAYPAL_SUCCESS_URL = "pay/success";
    public static final String PAYPAL_CANCEL_URL = "pay/cancel";

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PaypalService paypalService;

    /**
     * 进入项目跳转
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    /**
     * 开始交易
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "pay")
    public String pay(HttpServletRequest request){
        String cancelUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_CANCEL_URL;
        String successUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_SUCCESS_URL;
        try {
        	//调用交易方法
            Payment payment = paypalService.createPayment(
                    500.00,
                    "USD", 
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "uTest payment demo",
                    cancelUrl, 
                    successUrl);
            //交易成功后，跳转反馈地址
            for(Links links : payment.getLinks()){
                if(links.getRel().equals("approval_url")){
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    /**
     * 交易取消
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_CANCEL_URL)
    public String cancelPay(){
        return "cancel";
    }

    /**
     * 交易成功，并执行交易（相当于提交事务）
     * @param paymentId
     * @param payerId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){
                return "success";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }
}
