package com.openmind.order.shared.mediator;

import com.openmind.order.shared.ValidationException;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal stand-in for MediatR: resolves the one {@link RequestHandler} bean matching a
 * request's runtime type (via generic-type inspection, done once at startup), runs any
 * matching {@link RequestValidator} beans first and aggregates their errors into a single
 * {@link ValidationException} before dispatching — the same shape as the original
 * ValidationBehavior pipeline.
 */
@Component
public class SimpleMediator implements Mediator {

    private final Map<Class<?>, RequestHandler<Request<Object>, Object>> handlers = new HashMap<>();
    private final Map<Class<?>, List<RequestValidator<Object>>> validators = new HashMap<>();

    @SuppressWarnings("unchecked")
    public SimpleMediator(List<RequestHandler<?, ?>> handlerBeans, List<RequestValidator<?>> validatorBeans) {
        for (RequestHandler<?, ?> handler : handlerBeans) {
            Class<?> requestType = ResolvableType.forClass(RequestHandler.class, handler.getClass())
                    .getGeneric(0)
                    .resolve();
            if (requestType == null) {
                throw new IllegalStateException("Cannot resolve request type for handler " + handler.getClass());
            }
            handlers.put(requestType, (RequestHandler<Request<Object>, Object>) handler);
        }

        for (RequestValidator<?> validator : validatorBeans) {
            Class<?> requestType = ResolvableType.forClass(RequestValidator.class, validator.getClass())
                    .getGeneric(0)
                    .resolve();
            if (requestType == null) {
                throw new IllegalStateException("Cannot resolve request type for validator " + validator.getClass());
            }
            validators.computeIfAbsent(requestType, key -> new ArrayList<>())
                    .add((RequestValidator<Object>) validator);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TResponse> TResponse send(Request<TResponse> request) {
        List<RequestValidator<Object>> matchingValidators = validators.get(request.getClass());
        if (matchingValidators != null && !matchingValidators.isEmpty()) {
            List<String> errors = new ArrayList<>();
            for (RequestValidator<Object> validator : matchingValidators) {
                errors.addAll(validator.validate(request));
            }
            if (!errors.isEmpty()) {
                throw new ValidationException(errors);
            }
        }

        RequestHandler<Request<Object>, Object> handler = handlers.get(request.getClass());
        if (handler == null) {
            throw new IllegalStateException("No handler registered for " + request.getClass());
        }
        return (TResponse) handler.handle((Request<Object>) request);
    }
}
