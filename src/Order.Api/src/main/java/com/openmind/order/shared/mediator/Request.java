package com.openmind.order.shared.mediator;

/**
 * Marker interface for a command or query dispatched through {@link Mediator}.
 * Mirrors MediatR's {@code IRequest<TResponse>} — commands and queries share this
 * one type, distinguished only by naming convention, same as the original.
 */
public interface Request<TResponse> {
}
