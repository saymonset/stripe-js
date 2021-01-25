package com.us.weavx.core.stripe.service;

import org.springframework.stereotype.Service;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentIntentCollection;
import com.us.weavx.core.stripe.dto.PaymentIntentDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class PaymentService {
	 String secretKey = "sk_test_51Hf9gJH6FU0hzi3YRTv7cNO3feuU9BWg1T76d7SyokzNxeQx8AaEVg9GU2FjMQ2ISWnZDgjnfbV4QMTm1njdJQGU004TJHKUqc";
	 
	 
	 public PaymentIntent paymentIntent(PaymentIntentDto paymentIntentDto) throws StripeException {
	        Stripe.apiKey = secretKey;
	        List<String> paymentMethodTypes = new ArrayList();
	        paymentMethodTypes.add("card");
	        Map<String, Object> params = new HashMap<>();
	        params.put("amount", paymentIntentDto.getAmount());
	        params.put("currency", paymentIntentDto.getCurrency());
	        params.put("description", paymentIntentDto.getDescription());
	        params.put("payment_method_types", paymentMethodTypes);
	        return PaymentIntent.create(params);
	    }

	    public PaymentIntent confirm(String id) throws StripeException {
	        Stripe.apiKey = secretKey;
	        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
	        Map<String, Object> params = new HashMap<>();
	        params.put("payment_method", "pm_card_visa");
	        paymentIntent.confirm(params);
	        return paymentIntent;
	    }

	    public PaymentIntent cancel(String id) throws StripeException {
	        Stripe.apiKey = secretKey;
	        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
	        paymentIntent.cancel();
	        return paymentIntent;
	    }
	    
	    public PaymentIntentCollection listAllPaymentIntents() throws StripeException {
	        Stripe.apiKey = secretKey;
	        Map<String, Object> params = new HashMap<>();
		     params.put("limit", 3);

		     PaymentIntentCollection paymentIntents =
		     PaymentIntent.list(params);
	        return paymentIntents;
	    }
	    
	    /***
	     *
	     * Stripe.apiKey = "sk_test_51Hf9gJH6FU0hzi3YRTv7cNO3feuU9BWg1T76d7SyokzNxeQx8AaEVg9GU2FjMQ2ISWnZDgjnfbV4QMTm1njdJQGU004TJHKUqc";

	     
	     * */
}
