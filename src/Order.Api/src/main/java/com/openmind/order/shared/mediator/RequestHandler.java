package com.openmind.order.shared.mediator;

public interface RequestHandler<TRequest extends Request<TResponse>, TResponse>
{
    TResponse handle(TRequest request);
}
