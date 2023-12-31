package net.cameronbowe.vouchleyapi;

/*
 * Engineered with love by Cameron Bowe (https://cameronbowe.net).
 * Copyright (©) 2023, all rights reserved.
 */

import net.cameronbowe.vouchleyapi.exceptions.VouchleyException;
import net.cameronbowe.vouchleyapi.user.VouchleyUser;
import net.cameronbowe.vouchleyapi.user.review.VouchleyUserReview;

import java.util.UUID;

/**
 * Vouchley API
 * A utility to access Vouchley's API with ease.
 */
public class VouchleyAPI {

	private static String API_KEY; //The API key.

	/*
	 * Allows you to set the API key.
	 *
	 * @param apiKey The API key.
	 */
	public static void setAPIKey(final String apiKey) {
		API_KEY = apiKey; //Set the API key.
	}

	/*
	 * Allows you to retrieve the API key.
	 *
	 * @return The API key.
	 */
	public static String getAPIKey() {
		return API_KEY; //Return the API key.
	}

	/*
	 * Allows you to retrieve a Vouchley user via their ID.
	 *
	 * This method does call a remote
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
	 * This method does call a remote
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
	 * This method does call a remote
	 * API and should be run on a separate thread.
	 *
	 * @param id The ID of the user's review.
	 * @return The user's review.
	 * @throws VouchleyException If an exception occurs.
	 */
	public static VouchleyUserReview getUserReviewById(final UUID id) throws VouchleyException {
		return VouchleyUserReview.getFromID( id); //Return the user review by its ID.
	}

}
