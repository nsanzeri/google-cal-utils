package com.nick.sanz;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @author sanzni01 Artist Show Import Instructions ReverbNation.com
 *         ReverbNation can import a CSV formatted spreadsheet of upcoming show
 *         information. Existing information will be replaced by the imported
 *         information without warning. Prior to importing, you may want to
 *         export your current shows so that you have a backup. The format for
 *         the import file is standard CSV with one row of headers and one row
 *         for each show to be imported. You must at least have columns for the
 *         Date, the Venue name, and the Venue location. All other columns are
 *         optional. The column order does not matter. If you omit a column, its
 *         information will not be altered. Alternatively, you can include the
 *         column but leave its values on the rows blank to reset the
 *         information back to the default state. Please see the following table
 *         for information about each column. Specifying Venue Locations
 *         ReverbNation does not require a complete address for the venue,
 *         although it is helpful. You must at least narrow down the venue
 *         location to a specific city in a specific country. For countries with
 *         state / province designations (e.g. the US, Canada, Australia, etc.),
 *         the state is required as well. However, you can bypass specifying the
 *         city and state (or just city) by specifying a postal / ZIP code. So,
 *         for example: If your venue was in Durham, NC 27701 US, you could
 *         specify the city as Durham, the state as NC, and the country as US,
 *         or you could just specify the ZIP code as 27701 and the country as
 *         US, or you could specify all four. If your venue was in Bedford, MK40
 *         UK (which does not require a state to be specified), you could
 *         specify the city as Bedford and the country as UK, or you could
 *         specify the postal code as MK40 and the country as UK, or you could
 *         specify all three. Columns Column Header Required Format / Values
 *         Default Description
 * 
 *         Date Yes Parses several formats of date without or without time. We
 *         suggest you experiment to find the most comfortable format for you.
 *         None The date and possibly the start time of the show.
 * 
 *         Time No If date does not include the time, you can include it
 *         separately. Format can be either 8:00PM or 20:00 8:00PM if date does
 *         not include a time component. Time the show will start.
 * 
 *         Venue Yes Text None The name of the venue for the show.
 * 
 *         Address No Text The street address for the venue.
 * 
 *         City Yes (* see Specifying Venue Location above) Text The city for
 *         the venue address.
 * 
 *         State Yes (* see Specifying Venue Location above) Text The state for
 *         the venue address.
 * 
 *         Postal Code Yes (* see Specifying Venue Location above) Text The
 *         postal / ZIP code for the venue address.
 * 
 *         Country Yes Text The country for the venue address.
 * 
 *         Details No Text Generic show details (e.g., CD release party, free
 *         swag)
 * 
 *         Age Limit No One of "All ages", "13-18", "18+", "18+ or w/ adult"
 *         "19+", "21+", or "21+ or w/ adult" All ages Entry age restrictions
 * 
 *         Private Show No Yes, yes, Y, y, No, no, N, n No Guests must be
 *         invited
 * 
 *         Ticket Details No Text Example: $9 advanced, $11 at the door
 * 
 *         Ticket Link No Text (with or without the http://) A URL for getting
 *         information about or purchasing tickets.
 * 
 *         Artists No A comma-separated list of the artists playing A list of
 *         the artists playing, with or without you. Following the import, any
 *         errors with required fields will be reported to you, and you will be
 *         given the opportunity to view the data to be imported and possibly
 *         upload a new file before it is saved.
 * 
 * 
 */
public class ReverbNationShowImport {
	public static void main(String args[]) throws Exception {
		try {

			String feed = "https://www.google.com/calendar/feeds/pjjfdgelvdjtuvrr89tun3nu7k%40group.calendar.google.com/public/basic?futureevents=true&orderby=starttime&sortorder=ascending&max-results=100&hl=en";
			URL feedUrl = new URL(feed);
			FileWriter writer = new FileWriter("c:\\hos-gig-import.csv");

			writer.append("Date");
			writer.append(',');
			writer.append("Time");
			writer.append(',');
			writer.append("Venue");
			writer.append(',');
			// writer.append("City");
			// writer.append(',');
			// writer.append("State");
			// writer.append(',');
			writer.append("Postal Code");
			writer.append(',');
			writer.append("Country");
			writer.append(',');
			writer.append("Details");
			writer.append(',');
			writer.append("Age Limit");
			writer.append(',');
			writer.append("Private Show");

			SyndFeedInput input = new SyndFeedInput();
			SyndFeed sf = input.build(new XmlReader(feedUrl));
			List<String> datesTaken = new ArrayList<String>();
			List entries = sf.getEntries();
			for (Object object : entries) {
				SyndEntry entry = (SyndEntry) object;
				System.out.println(entry.getTitle());
				String description = entry.getDescription().getValue();
				String title = entry.getTitle();
				String venue = title.substring(title.indexOf("Sonics") + 9);
				venue = venue.replace("&#39;", "");
				String showDate = description.substring(6, description.indexOf(","));
				String zipcode = determineZip(description, venue);
				String desc = determineDecs(description, venue);
				String privateGig = "N";
				if (venue.indexOf("Private") > -1) {
					privateGig = "Y";
					zipcode = "60605";
				}

				System.out.println("Show date = " + showDate);
				datesTaken.add(showDate);
				System.out.println();

				// ========== Build CSV File ==========
				writer.append("\n"); // Date
				writer.append(showDate); // Date
				writer.append(',');
				writer.append("21:00p"); // Date
				writer.append(',');
				writer.append(venue); // Venue
				writer.append(',');
				// writer.append("City");
				// writer.append(',');
				// writer.append("State");
				// writer.append(',');
				writer.append(zipcode); // Postal Code
				writer.append(',');
				writer.append("US"); // Country
				writer.append(',');
				writer.append(desc); // Details
				writer.append(',');
				writer.append("21+"); // Age Limit
				writer.append(',');
				writer.append(privateGig); // Private Show

				// ========== Build CSV File ==========
			}
			writer.flush();
			writer.close();
			System.out.println("COMPLETE - got to c:\\hos-gig-import.csv to get your import list");
		} catch (IOException e) {
			e.printStackTrace();
		}

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