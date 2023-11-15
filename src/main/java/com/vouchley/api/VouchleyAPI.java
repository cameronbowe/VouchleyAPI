package com.vouchley.api;

/*
 * Made with ❤ by Cameron Bowe (https://cameronbowe.net).
 * Copyright (©) 2023, all rights reserved.
 */

import com.vouchley.api.exceptions.VouchleyException;
import com.vouchley.api.user.VouchleyUser;
import com.vouchley.api.user.review.VouchleyUserReview;

import java.util.UUID;

/**
 * Vouchley API
 * Serves as the main class for the Vouchley API.
 */
public class VouchleyAPI {

	/*
	 * Allows you to retrieve a Vouchley user via their ID.
	 *
	 * @implNote This method does call a remote
	 * API and should be run on a separate thread.
	 *
	 * @param id The ID of the user.
	 * @return The user.
	 * @throws VouchleyException If an exception occurs.
	 */
	public static VouchleyUser getUserById(final UUID id) throws VouchleyException {
		return VouchleyUser.getFromID(id); //Return the user by their ID.
	}

	/*
	 * Allows you to retrieve a Vouchley user via their username.
	 *
	 * @implNote This method does call a remote
	 * API and should be run on a separate thread.
	 *
	 * @param id The username of the user.
	 * @return The user.
	 * @throws VouchleyException If an exception occurs.
	 */
	public static VouchleyUser getUserByUsername(final String username) throws VouchleyException {
		return VouchleyUser.getFromUsername(username); //Return the user by their username.
	}

	/*
	 * Allows you to retrieve a Vouchley user's review via its ID.
	 *
	 * @implNote This method does call a remote
	 * API and should be run on a separate thread.
	 *
	 * @param id The ID of the user's review.
	 * @return The user's review.
	 * @throws VouchleyException If an exception occurs.
	 */
	public static VouchleyUserReview getUserReviewById(final UUID id) throws VouchleyException {
		return VouchleyUserReview.getFromID(id); //Return the user review by its ID.
	}

}
