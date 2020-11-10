(function(){


    var selectDeploy=false;
    $("#typePay").click(function(e)
    {
            if(!selectDeploy)
            {
                $("#typePay img").addClass("animationImgSelect")
                $("#typePay").css("height",$("#optionsHeight")[0].clientHeight)
            }
            else
            {
                $("#typePay img").removeClass("animationImgSelect")
                $("#typePay").css("height","3.2em")
            }
            selectDeploy=!selectDeploy
    })
    $(".selectOptionBuy").click(function(e)
    {
        var valueToChange=$("#"+e.currentTarget.id).attr("value") 
        var pago = $(".selectOptionBuy").data("pago");
        var total =1001;
        
        if(e.currentTarget.id=="TarjetaCredito")
        {
            $("#imgTargetCredit").show(600)
            $("#"+$(".selectOptionBuy").parent()[0].id + " #valueTypePay").addClass(valueToChange);
            $("#"+$(".selectOptionBuy").parent()[0].id + " #valueTypePay").text(pago)
            $("#"+$(".selectOptionBuy").parent()[0].id + " #valueTypePay").removeClass("general_inputSelect_typePay_placeholder");
            $("#goFormPayment").text("PAGAR $"  +   total)
        }    
        else
        {
        
            $("#imgTargetCredit").hide(600)
            $("#"+$(".selectOptionBuy").parent()[0].id + " #valueTypePay").removeClass("general_inputSelect_typePay_1") 
            $("#"+$(".selectOptionBuy").parent()[0].id + " #valueTypePay").text(valueToChange)
            $("#"+$(".selectOptionBuy").parent()[0].id + " #valueTypePay").removeClass("general_inputSelect_typePay_placeholder");
            $("#goFormPayment").text(" ")
        }
        statusTypePay("formPay"+e.currentTarget.id);
      
      //  language(detectLanguage())
        

    
    

    });

    $("#goFormPayment").click((e)=>{
        var pago = $(".selectOptionBuy").data("pago");
        var $numberCreditCard = $("#numberCreditCard");
        var $expirationCreditCard = $("#expirationCreditCard");
        var $CVV = $("#CVV");
        var ccNum = $numberCreditCard.val();
        var ccCVC = $CVV.val();
        
        var res = $expirationCreditCard.val().split("/");
        var ccMonth = res[0];
        var ccYear = res[1];
 

        //console.log(data_strype);

        var stripe = Stripe('pk_test_51Hf9gJH6FU0hzi3Y9Pq5MctFLILmIo1nbJuc2QXNSdHtaUuf2V3XAZzcwdAkTQrZmhbgsc2zoUH1LdQhvaDXVkJO00v8KALJOO');  
 //var stripe = Stripe('pk_test_51Hf9gJH6FU0hzi3Y9Pq5MctFLILmIo1nbJuc2QXNSdHtaUuf2V3XAZzcwdAkTQrZmhbgsc2zoUH1LdQhvaDXVkJO00v8KALJOO');

 // Create an instance of Elements.
           var elements = stripe.elements();

        stripe.card.createToken({
                                    number: ccNum,
                                    cvc: ccCVC,
                                    exp_month: ccMonth,
                                    exp_year: ccYear
                                    }, stripeResponseHandler);
      
    });



    function stripeResponseHandler(status, response, data_strype) {
        console.log('card status: ', status);
        console.log('token: ', response.id);
        $.ajax({
          type: 'POST',
          url: 'https://checkout.stripe.com/checkout.js',
          headers: {
            stripeToken: response.id
          },
          data: {
            number: ccNum,
            cvc: ccCVC,
            exp_month: ccMonth,
            exp_year: ccYear
          },
          success: (response) => {
            console.log('successful payment: ', response);
          },
          error: (response) => {
            console.log('error payment: ', response);
          }
        })
      }
    

     var statusTypePay = function(id)
    {
        
        var typePay =["formPayTarjetaCredito","formPayPaypal"];
        typePay.forEach(function(e,i)
        {
            e==id
            ?
            $("#"+e).show(500)
            :
            $("#"+e).hide(500)
        })
    };


    

})();