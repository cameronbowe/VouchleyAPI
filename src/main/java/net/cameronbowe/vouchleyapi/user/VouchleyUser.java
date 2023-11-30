package net.cameronbowe.vouchleyapi.user;

/*
 * Engineered with love by Cameron Bowe (https://cameronbowe.net).
 * Copyright (©) 2023, all rights reserved.
 */

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.cameronbowe.vouchleyapi.VouchleyAPI;
import net.cameronbowe.vouchleyapi.exceptions.VouchleyException;
import net.cameronbowe.vouchleyapi.user.review.VouchleyUserReview;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * Vouchley User
 * Handles the data and implementation of a Vouchley user.
 */
public class VouchleyUser {

	private final UUID id; //The id of the user.
	private final String displayName, username, title, avatarURL, discordId; //The display name, username, title, avatar URL, and Discord ID of the user.
	private final ArrayList<VouchleyUserReview> reviews; //The reviews of the user.
	private final int averageRating; //The average rating of the user.
	private final double totalValueTraded; //The total value traded of the user.
	private final ArrayList<Integer> donatorBadges; //The donator badges of the user.

	private static final Function<UUID, HttpGet> DESTINATION_ID = id -> {
		final HttpGet httpGet = new HttpGet("https://www.vouchley.com/api/v1/user?id=" + id.toString()); //Set the destination for retrieving a user via their id.
		httpGet.addHeader("Authorization", "Bearer " + VouchleyAPI.getAPIKey()); //Add the authorization header.
		return httpGet; //Return the destination.
	}; //The destination for retrieving a user via their id.
	private static final Function<String, HttpGet> DESTINATION_USERNAME = username -> {
		final HttpGet httpGet = new HttpGet("https://www.vouchley.com/api/v1/user?username=" + username); //Set the destination for retrieving a user via their username.
		httpGet.addHeader("Authorization", "Bearer " + VouchleyAPI.getAPIKey()); //Add the authorization header.
		return httpGet; //Return the destination.
	}; //The destination for retrieving a userw via their username.

	/**
	 * Allows you to create a Vouchley user.
	 *
	 * @param id The id of the user.
	 * @param displayName The display name of the user.
	 * @param username The username of the user.
	 * @param title The title of the user.
	 * @param avatarURL The avatar URL of the user.
	 * @param discordId The Discord ID of the user.
	 * @param reviews The reviews of the user.
	 * @param averageRating The average rating of the user.
	 * @param totalValueTraded The total value traded of the user.
	 * @param donatorBadges The donator badges of the user.
	 */
	private VouchleyUser(final UUID id, final String displayName, final String username, final String title, final String avatarURL, final String discordId, final ArrayList<VouchleyUserReview> reviews, final int averageRating, final double totalValueTraded, final ArrayList<Integer> donatorBadges) {
		this.id = id; //Set the id of the user.
		this.displayName = displayName; //Set the display name of the user.
		this.username = username; //Set the username of the user.
		this.title = title; //Set the title of the user.
		this.avatarURL = avatarURL; //Set the avatar URL of the user.
		this.discordId = discordId; //Set the Discord ID of the user.
		this.reviews = reviews; //Set the reviews of the user.
		this.averageRating = averageRating; //Set the average rating of the user.
		this.totalValueTraded = totalValueTraded; //Set the total value traded of the user.
		this.donatorBadges = donatorBadges; //Set the donator badges of the user.
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
	public static VouchleyUser getFromID(final UUID id) throws VouchleyException {
		return get(DESTINATION_ID.apply(id)); //Return the user.
	}

	/*
	 * Allows you to retrieve a Vouchley user via their username.
	 *
	 * This method does call a remote
	 * API and should be run on a separate thread.
	 *
	 * @param username The username of the user.
	 * @return The user.
	 * @throws VouchleyException If an exception occurs.
	 */
	public static VouchleyUser getFromUsername(final String username) throws VouchleyException {
		return get(DESTINATION_USERNAME.apply(username)); //Return the user.
	}

	/*
	 * Allows you to retrieve a Vouchley user.
	 *
	 * This method serves as a wrapper to
	 * save code & time for retrieval methods.
	 *
	 * @param destination The destination.
	 * @return The user.
	 * @throws VouchleyException If an exception occurs.
	 */
	private static VouchleyUser get(final HttpGet httpGet) throws VouchleyException {
		try (final CloseableHttpResponse response = HttpClients.createMinimal().execute(httpGet)) { //Create the request.
			if (response.getStatusLine().getStatusCode() != 200) return null; //If the request failed, return null.
			final JsonObject jsonObject = JsonParser.parseString(EntityUtils.toString(response.getEntity())).getAsJsonObject(); //Parse the response.
			return new VouchleyUser(UUID.fromString(jsonObject.get("id").getAsString()), Optional.ofNullable(jsonObject.get("displayName")).map(JsonElement::getAsString).orElse(null), jsonObject.get("username").getAsString(), jsonObject.get("title").getAsString(), jsonObject.get("avatarURL").getAsString(), jsonObject.get("discordId").getAsString(), null, jsonObject.get("averageRating").getAsInt(), jsonObject.get("totalValueTraded").getAsDouble(), Optional.ofNullable(jsonObject.get("donatorBadges")).map(jsonElement -> {
				final ArrayList<Integer> donatorBadges = new ArrayList<>(); //Create the donator badges.
				jsonElement.getAsJsonArray().forEach(badge -> donatorBadges.add(badge.getAsInt())); //Add the donator badges.
				return donatorBadges; //Return the donator badges.
			}).orElse(null)); //Return the user.
		} catch (final IOException exception) { //If an exception occurs, return null.
			throw new VouchleyException("An exception occurred whilst retrieving a user."); //Throw an exception.
		}
	}

	/**
	 * Allows you to get the id of the user.
	 *
	 * @return The id of the user.
	 */
	public UUID getId() {
		return this.id; //Return the id of the user.
	}

	/**
	 * Allows you to get the display name of the user.
	 * <p>
	 * May return an empty string.
	 *
	 * @return The display name of the user.
	 */
	public String getDisplayName() {
		return this.displayName; //Return the display name of the user.
	}

	/**
	 * Allows you to get the username of the user.
	 *
	 * @return The username of the user.
	 */
	public String getUsername() {
		return this.username; //Return the username of the user.
	}

	/**
	 * Allows you to get the title of the user.
	 * <p>
	 * May return an empty string.
	 *
	 * @return The title of the user.
	 */
	public String getTitle() {
		return this.title; //Return the title of the user.
	}

	/**
	 * Allows you to get the avatar URL of the user.
	 * <p>
	 * May return the default Vouchley avatar(s).
	 *
	 * @return The avatar URL of the user.
	 */
	public String getAvatarURL() {
		return this.avatarURL; //Return the avatar URL of the user.
	}

	/**
	 * Allows you to get the Discord ID of the user.
	 * <p>
	 * May return an empty string.
	 *
	 * @return The Discord ID of the user.
	 */
	public String getDiscordId() {
		return this.discordId; //Return the Discord ID of the user.
	}

	/**
	 * Allows you to get the reviews of the user.
	 * <p>
	 * May return an empty list.
	 *
	 * @return The reviews of the user.
	 */
	public List<VouchleyUserReview> getReviews() {
		return this.reviews; //Return the reviews of the user.
	}

	/**
	 * Allows you to get the average rating of the user.
	 *
	 * @return The average rating of the user.
	 */
	public Integer getAverageRating() {
		return this.averageRating; //Return the average rating of the user.
	}

	/**
	 * Allows you to get the total value traded of the user.
	 *
	 * @return The total value traded of the user.
	 */
	public Double getTotalValueTraded() {
		return this.totalValueTraded; //Return the total value traded of the user.
	}

	/**
	 * Allows you to get the donator badges of the user.
	 * <p>
	 * May return an empty list.
	 *
	 * @return The donator badges of the user.
	 */
	public List<Integer> getDonatorBadges() {
		return this.donatorBadges; //Return the donator badges of the user.
	}

}
