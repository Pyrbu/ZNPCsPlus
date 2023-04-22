package io.github.znetworkw.znpcservers.commands.exception;

import java.io.Serial;

/**
 * @author xCodiq - 20/04/2023
 */
public class CommandException extends Exception {
	@Serial private static final long serialVersionUID = 1L;

	public CommandException(String message) {
		super(message);
	}

	public CommandException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommandException(Throwable cause) {
		super(cause);
	}
}
