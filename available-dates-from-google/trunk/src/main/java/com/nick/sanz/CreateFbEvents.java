package com.nick.sanz;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import com.restfb.types.Page;
import com.restfb.types.User;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @author sanzni01 Access token:
 *         AAACEdEose0cBAHmsErKLE9zC8kAhLAJq6xj1q9cZBEnQGCmPnSBRVmbf3zVMllo5Xa4nOfXc3HZBhK2hZAUaiTWyp6rXKgM0oZBTPbUK1tCZAuEjuDOjW
 * 
 *         Get it from:https://developers.facebook.com/tools/explorer
 * 
 */
public class CreateFbEvents {
	private static String myToken = "CAACEdEose0cBAJo6xZBJfUZB8sHcHyZCBJ5k5jMbZCf2rbJyexx7QmQ4iZCrP4ZAGz00e4xAKdkCPr232OHJbpezbtbE04XaycPDf3biE9TmWur1uLEPB6N0CkrMjJban0kVIaGRtW8Dr69rndSc42sVZB5t89qQ9UZD";

	public static void main(String args[]) {
		try {

			String feed = "https://www.google.com/calendar/feeds/pjjfdgelvdjtuvrr89tun3nu7k%40group.calendar.google.com/public/basic?futureevents=true&orderby=starttime&sortorder=ascending&max-results=100&hl=en";
			URL feedUrl = null;
			feedUrl = new URL(feed);

			FacebookClient facebookClient = new DefaultFacebookClient(myToken);
			User user = facebookClient.fetchObject("me", User.class);
			Page page = facebookClient.fetchObject("hookedonsonicsband", Page.class);

			System.out.println("User name: " + user.getName());
			System.out.println("Page likes: " + page.getLikes());

			SyndFeedInput input = new SyndFeedInput();
			SyndFeed sf;
			sf = input.build(new XmlReader(feedUrl));
			List<String> datesTaken = new ArrayList<String>();
			List entries = sf.getEntries();
			int testCount = 0;
			for (Object object : entries) {
				SyndEntry entry = (SyndEntry) object;
				System.out.println(entry.getTitle());
				String description = entry.getDescription().getValue();
				String title = entry.getTitle();
				String venue = title.substring(title.indexOf("Sonics") + 9);
				venue = venue.replace("&#39;", "");
				String googDate = description.substring(6, description.indexOf(","));
				Date showDate = formatGoogleDate(googDate);

				
				String zipcode = determineZip(description, venue);
				String desc = determineDecs(description, venue);
				String privateGig = "N";

				FacebookType publishEventResponse = facebookClient.publish("132220862483/events", FacebookType.class, 
						Parameter.with("name", title), 
						Parameter.with("start_time", showDate), 
						Parameter.with("location", desc), 
						Parameter.with("description", title + "\n" + desc));

				System.out.println("Published event ID: " + publishEventResponse.getId());
				testCount++;
				if (testCount > 4){
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static Date formatGoogleDate(String googDate) {
	    String target = googDate + " 21:30:30 cst 2014";
	    DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
	    Date date = null;
		try {
			date = df.parse(target);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    System.out.println(date);
	    return date;
	}

	private static String determineDecs(String description, String venue) {
		String desc = "";
		if (!venue.contains("Private")) {
			desc = description.substring(description.indexOf("Where:") + 6, description.lastIndexOf("<br>") - 1);
		}
		return desc;
	}

	private static String determineZip(String description, String venue) {
		String zipcode = "";
		if (venue.equals("Jambalaya")) {
			zipcode = "60175";
		} else if (venue.contains("Captain")) {
			zipcode = "60002";
		} else {

			if (description.indexOf("IL,") != -1) {
				zipcode = description.substring(description.indexOf("IL,") + 4, description.indexOf("IL,") + 9);

			} else {
				zipcode = description.substring(description.indexOf("IL") + 3, description.indexOf("IL") + 8);
			}
		}
		if (!zipcode.startsWith("6")) {
			zipcode = "";
		}
		return zipcode;
	}

}
