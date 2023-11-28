package net.cameronbowe.vouchleyapi.exceptions;

/*
 * Engineered with love by Cameron Bowe (https://cameronbowe.net).
 * Copyright (©) 2023, all rights reserved.
 */

/**
 * Vouchley Exception
 * Serves as an exception class for any
 * exceptions that may occur in the Vouchley API.
 */
public class VouchleyException extends Exception {

	/**
	 * Allows you to create a Vouchley Exception with a message.
	 *
	 * @param message The message to throw.
	 */
	public VouchleyException(final String message) {
		super(message); //Super the message.
	}

}
