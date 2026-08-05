package com.openmind.order.shared.mediator;

public interface Mediator
{
    <TResponse> TResponse send(Request<TResponse> request);
}
