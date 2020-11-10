(
function(){

    const urlCRUD = 'http://localhost:8180/simon/stripe/paymentintent';
    
// Custom styling can be passed to options when creating an Element.
// (Note that this demo uses a wider set of styles than the guide below.)
var style = {
    base: {
      color: '#32325d',
      fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
      fontSmoothing: 'antialiased',
      fontSize: '16px',
      '::placeholder': {
        color: '#aab7c4'
      }
    },
    invalid: {
      color: '#fa755a',
      iconColor: '#fa755a'
    }
  };

    var stripe = Stripe('pk_test_51Hf9gJH6FU0hzi3Y9Pq5MctFLILmIo1nbJuc2QXNSdHtaUuf2V3XAZzcwdAkTQrZmhbgsc2zoUH1LdQhvaDXVkJO00v8KALJOO');

    // Create an instance of the card Element.

    var elements = stripe.elements();

    var card = elements.create('card', {style: style});

        // Add an instance of the card UI component into the `card-element` <div>
        card.mount('#card-element');
 // Handle real-time validation errors from the card Element.
card.on('change', function(event) {
    var displayError = document.getElementById('card-errors');
    if (event.error) {
      displayError.textContent = event.error.message;
    } else {
      displayError.textContent = '';
    }
  });
  
  // Handle form submission.
  var form = document.getElementById('payment-form');
  form.addEventListener('submit', function(event) {
    event.preventDefault();
  
    stripe.createToken(card).then(function(result) {
      if (result.error) {
        // Inform the user if there was an error.
        var errorElement = document.getElementById('card-errors');
        errorElement.textContent = result.error.message;
      } else {
        // Send the token to your server.
        stripeTokenHandler(result.token);
      }
    });
  });
  
  // Submit the form with the token ID.
  //function stripeTokenHandler(token) {
    const stripeTokenHandler = async(token) => {
    // Insert the token ID into the form so it gets submitted to the server
    /* var form = document.getElementById('payment-form');
    var hiddenInput = document.createElement('input');
    hiddenInput.setAttribute('type', 'hidden');
    hiddenInput.setAttribute('name', 'stripeToken');
    hiddenInput.setAttribute('value', token.id);
    form.appendChild(hiddenInput);
     alert(token.id); */
    // Submit the form
  //  form.submit();

   const resp = await sendStripeBackend(token);
    alert(JSON.stringify(resp));
  };


  
const sendStripeBackend = async( token ) => {

    const jsonstr = {
        "token": token,
        "description": 'Primer pago',
        "amount": 10000,
        "currency": 'usd'
    };

    const resp = await fetch( urlCRUD, {
        method: 'POST',
        body:   JSON.stringify(jsonstr)  ,
        headers: {
            'Content-Type': 'application/json'
        }
    });

    return await resp.json();
}

})();
 