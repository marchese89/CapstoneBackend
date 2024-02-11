package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.payloads.PaymentConfirmDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.TokenCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.model.Token;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    public PaymentIntent createPaymentIntent(Long amount, String currency) throws StripeException {
        Stripe.apiKey = secretKey;

        PaymentIntent intent = PaymentIntent.create(
                new PaymentIntentCreateParams.Builder()
                        .setCurrency(currency)
                        .setAmount(amount)
                        .build()
        );

        return intent;
    }

    public String confirmPayment(PaymentConfirmDTO paymentConfirmDTO) throws StripeException {
        Stripe.apiKey = secretKey;

        Map<String, Object> params = new HashMap<>();
        params.put("payment_method", paymentConfirmDTO.paymentMethodId());
        params.put("amount", paymentConfirmDTO.amount());
        params.put("currency", paymentConfirmDTO.currency());
        params.put("confirm", true);
        params.put("return_url","http://localhost:3000");
        PaymentIntent paymentIntent = PaymentIntent.create(params);

        return "Payment confirmed!";
    }


}
