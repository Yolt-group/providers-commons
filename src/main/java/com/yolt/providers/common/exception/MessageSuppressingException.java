package com.yolt.providers.common.exception;

/**
 * The {@code MessageSuppressingException} will clone the cause but not the
 * message. This will allow the stacktrace to be viewed without having a cause
 * output sensitive data.
 * <p>
 * Creates a new throwable where the message is reduced to the original
 * throwable's simple class name. The cause is taken from the original as well
 * as the stacktrace.
 */
public final class MessageSuppressingException extends Exception {

    /**
     * Creates a new throwable where the message is reduced to the wrapped
     * throwable's class name. The cause is taken from the original as well as
     * the stacktrace.
     *
     * @param original the original throwable.
     */
    public MessageSuppressingException(final Throwable original) {
        super(original.getClass().getName(), original.getCause(), true, true);
        setStackTrace(original.getStackTrace());
    }

    /**
     * Returns the cause wrapped in another {@code MessageSuppressingException}
     * to prevent the message from ever being printed.
     *
     * @return the {@code MessageSuppressingException}-wrapped cause.
     */
    @Override
    public synchronized Throwable getCause() {
        return (super.getCause() == this || super.getCause() == null
            ? null
            : new MessageSuppressingException(super.getCause()));
    }
}
