package com.openmind.payment.features.processpayment;

import com.openmind.payment.domain.DomainException;
import com.openmind.payment.domain.entities.PaymentAggregate;
import com.openmind.payment.domain.events.DomainEvent;
import com.openmind.payment.domain.repositories.PaymentRepository;
import com.openmind.payment.shared.application.interfaces.EventBus;
import com.openmind.payment.shared.application.interfaces.PaymentGateway;
import com.openmind.payment.shared.mediator.RequestHandler;
import org.springframework.stereotype.Service;

@Service
public class ProcessPaymentCommandHandler implements RequestHandler<ProcessPaymentCommand, ProcessPaymentResult> {

    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;
    private final EventBus eventBus;

    public ProcessPaymentCommandHandler(PaymentRepository paymentRepository, PaymentGateway paymentGateway, EventBus eventBus) {
        this.paymentRepository = paymentRepository;
        this.paymentGateway = paymentGateway;
        this.eventBus = eventBus;
    }

    @Override
    public ProcessPaymentResult handle(ProcessPaymentCommand request) {
        try {
            PaymentAggregate payment = PaymentAggregate.create(request.orderId(), request.customerId(), request.amount());

            boolean success = paymentGateway.charge(payment);

            if (success) {
                payment.markAsProcessed();
            } else {
                payment.markAsFailed("Payment gateway declined the transaction.");
            }

            paymentRepository.add(payment);

            for (DomainEvent domainEvent : payment.getDomainEvents()) {
                eventBus.publish(domainEvent);
            }
            payment.clearDomainEvents();

            return new ProcessPaymentResult(success, payment.getId(), success ? "Payment processed successfully." : "Payment declined.");
        } catch (DomainException ex) {
            return new ProcessPaymentResult(false, null, ex.getMessage());
        }
    }
}
