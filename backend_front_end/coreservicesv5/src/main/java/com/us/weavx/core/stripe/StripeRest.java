package com.us.weavx.core.stripe;

import com.stripe.exception.StripeException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentIntentCollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.exception.ObjectDoesNotExistsException;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.AccessToken;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
import com.us.weavx.core.stripe.dto.PaymentIntentDto;
import com.us.weavx.core.stripe.service.PaymentService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.staticFiles;
import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Created by simon on 11/11/2020.
 */
@RequestMapping("/stripe")
@RestController
@CrossOrigin(origins = "*")
public class StripeRest {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ConfigurationTxServices configurationServices;
    private static Gson gson = new Gson();
    static final String YOUR_DOMAIN = "http://localhost:8080/harvestful";
    
    @Autowired
    PaymentService paymentService;

    @RequestMapping(value = "/create-session", method= RequestMethod.POST)
    @ResponseBody
    public Response test(@RequestBody Request req) {

        Response res = new Response();
        
     // Set your secret key. Remember to switch to your live secret key in production!
     // See your keys here: https://dashboard.stripe.com/account/apikeys
        Stripe.apiKey = "sk_test_51Hf9gJH6FU0hzi3YRTv7cNO3feuU9BWg1T76d7SyokzNxeQx8AaEVg9GU2FjMQ2ISWnZDgjnfbV4QMTm1njdJQGU004TJHKUqc";

     PaymentIntentCreateParams params =
       PaymentIntentCreateParams.builder()
         .setAmount(61613L)
         .setCurrency("mxn")
         .addPaymentMethodType("card")
         .build();

     try {
		PaymentIntent paymentIntent = PaymentIntent.create(params);
		HashMap<String, Object> map = new HashMap();
        map.put("client_secret", paymentIntent.getClientSecret());
       // map.put("paymentIntent", paymentIntent);
        res.setResult(map);
        
	} catch (StripeException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}



        return res;
    }
    
  

    @PostMapping("/paymentintent")
    public ResponseEntity<String> payment(@RequestBody PaymentIntentDto paymentIntentDto) throws StripeException {
        PaymentIntent paymentIntent = paymentService.paymentIntent(paymentIntentDto);
        
        String id = paymentIntent.getId();
        return new ResponseEntity<String>(id, HttpStatus.OK);
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<String> confirm(@PathVariable("id") String id) throws StripeException {
        PaymentIntent paymentIntent = paymentService.confirm(id);
        String paymentStr = paymentIntent.toString();
        return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancel(@PathVariable("id") String id) throws StripeException {
        PaymentIntent paymentIntent = paymentService.cancel(id);
        String paymentStr = paymentIntent.toString();
        return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
    }
    
    
    @GetMapping("/listAll")
    public ResponseEntity<String> listAllPaymentIntents() throws StripeException {
    	PaymentIntentCollection paymentIntents = paymentService.listAllPaymentIntents();
        String paymentStr = paymentIntents.toString();
        return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
    }
    
    
    
}
