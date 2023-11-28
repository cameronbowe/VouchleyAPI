package net.cameronbowe.vouchleyapi.user.review;

/*
 * Engineered with love by Cameron Bowe (https://cameronbowe.net).
 * Copyright (©) 2023, all rights reserved.
 */

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.cameronbowe.vouchleyapi.exceptions.VouchleyException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/*
 * Vouchley User Review
 * Handles the data and implementation of a Vouchley user's review.
 */
public class VouchleyUserReview {

	private final UUID id, receiver, sender; //The ID, receiver, and sender of the user's review.
	private final double value; //The value of the user's review.
	private final int rating; //The rating of the user's review.
	private final long timeSent; //The time the user's review was sent.
	private final String message, reply, platform, product; //The message, reply, platform, and product of the user's review.

	/**
	 * Allows you to create a user's review.
	 *
	 * @param id The ID of the user's review.
	 * @param receiver The receiver of the user's review.
	 * @param sender The sender of the user's review.
	 * @param value The value of the user's review.
	 * @param rating The rating of the user's review.
	 * @param timeSent The time the user's review was sent.
	 * @param message The message of the user's review.
	 * @param reply The reply of the user's review.
	 * @param platform The platform of the user's review.
	 * @param product The product of the user's review.
	 */
	private VouchleyUserReview(final UUID id, final UUID receiver, final UUID sender, final double value, final int rating, final long timeSent, final String message, final String reply, final String platform, final String product) {
		this.id = id; //Set the ID of the user's review.
		this.receiver = receiver; //Set the receiver of the user's review.
		this.sender = sender; //Set the sender of the user's review.
		this.value = value; //Set the value of the user's review.
		this.rating = rating; //Set the rating of the user's review.
		this.timeSent = timeSent; //Set the time the user's review was sent.
		this.message = message; //Set the message of the user's review.
		this.reply = reply; //Set the reply of the user's review.
		this.platform = platform; //Set the platform of the user's review.
		this.product = product; //Set the product of the user's review.
	}

	/*
	 * Allows you to retrieve a Vouchley user's review via its ID.
	 *
	 * @apiNote This method does call a remote
	 * API and should be run on a separate thread.
	 *
	 * @param id The ID of the user's review.
	 * @return The user's review.
	 * @throws VouchleyException If an exception occurs.
	 */
	public static VouchleyUserReview getFromID(final UUID id) throws VouchleyException {
		try (final CloseableHttpResponse response = HttpClients.createMinimal().execute(new HttpGet("https://www.vouchley.com/api/v1/review?id=" + id.toString()))) { //Create the request.
			if (response.getStatusLine().getStatusCode() != 200) return null; //If the request failed, return null.
			final JsonObject jsonObject = JsonParser.parseString(EntityUtils.toString(response.getEntity())).getAsJsonObject(); //Parse the response.
			return new VouchleyUserReview(UUID.fromString(jsonObject.get("id").getAsString()), UUID.fromString(jsonObject.get("receiver").getAsString()), UUID.fromString(jsonObject.get("sender").getAsString()), jsonObject.get("value").getAsDouble(), jsonObject.get("rating").getAsInt(), jsonObject.get("timeSent").getAsLong(), jsonObject.get("message").getAsString(), Optional.ofNullable(jsonObject.get("reply")).map(JsonElement::getAsString).orElse(null), jsonObject.get("platform").getAsString(), Optional.ofNullable(jsonObject.get("product")).map(JsonElement::getAsString).orElse(null)); //Return the user's review.
		} catch (final IOException exception) { //If an exception occurs, return null.
			throw new VouchleyException("An exception occurred whilst retrieving a user's review."); //Throw an exception.
		}
	}

	/**
	 * Allows you to get the ID of the user's review.
	 *
	 * @return The ID of the user's review.
	 */
	public UUID getId() {
		return this.id; //Return the ID of the user's review.
	}

	/**
	 * Allows you to get the receiver of the user's review.
	 *
	 * @return The receiver of the user's review.
	 */
	public UUID getReceiver() {
		return this.receiver; //Return the receiver of the user's review.
	}

	/**
	 * Allows you to get the sender of the user's review.
	 *
	 * @return The sender of the user's review.
	 */
	public UUID getSender() {
		return this.sender; //Return the sender of the user's review.
	}

	/**
	 * Allows you to get the value of the user's review.
	 *
	 * @return The value of the user's review.
	 */
	public Double getValue() {
		return this.value; //Return the value of the user's review.
	}

	/**
	 * Allows you to get the rating of the user's review.
	 *
	 * @return The rating of the user's review.
	 */
	public Integer getRating() {
		return this.rating; //Return the rating of the user's review.
	}

	/**
	 * Allows you to get the time the user's review was sent.
	 *
	 * @return The time the user's review was sent.
	 */
	public Long getTimeSent() {
		return this.timeSent; //Return the time the user's review was sent.
	}

	/**
	 * Allows you to get the message of the user's review.
	 *
	 * @return The message of the user's review.
	 */
	public String getMessage() {
		return this.message; //Return the message of the user's review.
	}

	/**
	 * Allows you to get the platform of the user's review.
	 *
	 * @return The platform of the user's review.
	 */
	public String getPlatform() {
		return this.platform; //Return the platform of the user's review.
	}

	/**
	 * Allows you to get the product of the user's review.
	 *
	 * @apiNote May return null.
	 *
	 * @return The product of the user's review.
	 */
	public String getProduct() {
		return this.product; //Return the product of the user's review.
	}

	/**
	 * Allows you to get the receiver's reply to the review.
	 *
	 * @apiNote May return null.
	 *
	 * @return The receiver's reply to the review.
	 */
	public String getReply() {
		return this.reply; //Return the receiver's reply to the review.
	}

}
