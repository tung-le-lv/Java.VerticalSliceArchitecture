package com.openmind.payment.shared.mediator;

public interface Mediator
{
    <TResponse> TResponse send(Request<TResponse> request);
}
