package com.openmind.payment.shared.mediator;

import java.util.List;

public interface RequestValidator<TRequest>
{
    List<String> validate(TRequest request);
}
