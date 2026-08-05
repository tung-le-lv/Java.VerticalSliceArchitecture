package com.openmind.order.shared;

import java.util.List;

public interface OperationResult
{
    boolean success();

    String message();

    List<String> errors();
}
