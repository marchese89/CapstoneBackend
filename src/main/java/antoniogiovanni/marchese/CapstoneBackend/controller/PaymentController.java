package antoniogiovanni.marchese.CapstoneBackend.controller;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.BadRequestException;
import antoniogiovanni.marchese.CapstoneBackend.model.Student;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.payloads.*;
import antoniogiovanni.marchese.CapstoneBackend.service.SolutionService;
import antoniogiovanni.marchese.CapstoneBackend.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private StripeService stripeService;

    @Autowired
    private SolutionService solutionService;

    @PostMapping("/create-payment")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ClientSecretDTO createPayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(paymentRequestDTO.amount(), paymentRequestDTO.currency());
            return new ClientSecretDTO(paymentIntent.getClientSecret());
        } catch (StripeException e) {
            e.printStackTrace();
            throw new BadRequestException("Error during payment creation");
        }
    }
    @PostMapping("/confirm-payment")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public PaymentResponseDTO confirmPayment(@RequestBody PaymentConfirmDTO paymentConfirmDTO,@AuthenticationPrincipal User currentUser) throws StripeException, MessagingException, IOException {
        String message = stripeService.confirmPayment(paymentConfirmDTO);
        solutionService.acceptSolution(paymentConfirmDTO.solutionId(),(Student) currentUser);
        return new PaymentResponseDTO(message);
    }

}