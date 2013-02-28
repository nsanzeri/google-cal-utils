package com.nick.sanz;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class GetAvailableDatesFromXml {
	public static void main(String args[]) throws Exception {
		String feed = "https://www.google.com/calendar/feeds/pjjfdgelvdjtuvrr89tun3nu7k%40group.calendar.google.com/public/basic?futureevents=true&orderby=starttime&sortorder=ascending&max-results=100&hl=en";

		URL feedUrl = new URL(feed);
 
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed sf = input.build(new XmlReader(feedUrl));
		List<String> datesTaken = new ArrayList<String>();
		List entries = sf.getEntries();
		Iterator it = entries.iterator();
		while (it.hasNext()) {
			SyndEntry entry = (SyndEntry) it.next();
			System.out.println(entry.getTitle());
			// System.out.println(entry.getLink());
			// SyndContentImpl date = (SyndContentImpl)
			// entry.getContents().get(0);
			// System.out.println("DATE====== " +
			// date.getValue().substring(date.getValue().indexOf("When: " + 6),
			// date.getValue().indexOf(",")));
			// System.out.println("BEFORE ENCODE ====== " + date.getValue());
			// datesTaken.add(date.getValue().substring(date.getValue().indexOf("When: ",
			// 1)));
			// datesTaken.add(date.getValue());
			SyndContent description = entry.getDescription();
			System.out.println(description.getValue().substring(6, description.getValue().indexOf(",")));
			datesTaken.add(description.getValue().substring(6, description.getValue().indexOf(",")));
			System.out.println();
		}

		System.out.println("****************************************************");
		for (String dateString : datesTaken) {
			System.out.println(dateString);
		}
		System.out.println("WEEKENDS ****************************************************");
		Month monthenum = null;
		boolean isFirstPrint = true;
		int year = 2013;
		int month = Calendar.FEBRUARY;
		int monthHold = Calendar.FEBRUARY;
		int yearHold = year;
		Calendar cal = new GregorianCalendar(year, month, 3);

		System.out.println("Hooked On Sonics - Available Dates");
		System.out.println("");

		
		System.out.println(monthenum.make(cal.get(Calendar.MONTH) + 1));
		System.out.println("----------------------");

		for (int i = 0; i < 365; i++) {
//			if (cal.get(Calendar.YEAR) < 2012) {
				// get the day of the week for the current day
				int day = cal.get(Calendar.DAY_OF_WEEK);
				// check if it is a Saturday or Sunday
				String searchDate = "";
				if (day == Calendar.FRIDAY) {
					searchDate = "Fri " + cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " " + cal.get(Calendar.DAY_OF_MONTH);
					if (!datesTaken.contains(searchDate)){
						System.out.println(searchDate);
					}

				}
				if (day == Calendar.SATURDAY) {
					searchDate = "Sat " + cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " " + cal.get(Calendar.DAY_OF_MONTH);
					if (!datesTaken.contains(searchDate)){
						System.out.println(searchDate);
					}

				}


				if (cal.get(Calendar.MONTH) != monthHold) {
					if (cal.get(Calendar.YEAR) != yearHold) {
						yearHold = cal.get(Calendar.YEAR);
					}
					System.out.println("");
					System.out.println("");
					System.out.println(monthenum.make(cal.get(Calendar.MONTH) + 1) + " - " + cal.get(Calendar.YEAR));
					System.out.println("------------");
					isFirstPrint = false;
					monthHold = cal.get(Calendar.MONTH);
				}

				// advance to the next day
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
		//}

	}
}