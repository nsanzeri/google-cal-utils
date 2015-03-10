package com.nick.sanz;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class TodayInHistory {
	public static void main(String args[]) throws Exception {
		String feed = "https://www.google.com/calendar/feeds/pjjfdgelvdjtuvrr89tun3nu7k%40group.calendar.google.com/public/basic?futureevents=false&orderby=starttime&sortorder=ascending&max-results=2000&hl=en";
		Month monthenum = null;
		URL feedUrl = new URL(feed);
 
		Calendar cal = Calendar.getInstance();
		cal.get(Calendar.DATE);
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed sf = input.build(new XmlReader(feedUrl));
		List<String> datesTaken = new ArrayList<String>();
		List<SyndEntry> entries = sf.getEntries();
		
		Boolean firstDate = true;
		String holdYear = "2002";
		System.out.println("\n==== Year = " + holdYear);
		int dayCount = 3;
		int counter = dayCount;
		boolean firstRun = true;
		for (int i = 0; i < counter; i++) {
			createEntry(monthenum, cal, entries, firstDate, holdYear, dayCount, firstRun);

			dayCount--;
			firstRun = false;
		}
		System.out.println("#HookedOnSonicsHistory\n");
		System.out.println("============= Historic events - END ==========================");
			
//		Hooked On Sonics @ 
//		HOS @ 
		//		System.out.println("============= Historic events ==========================");
//		for (SyndEntry entry : entries) {
//			SyndContent description = entry.getDescription();
//			System.out.println(description.getValue().substring(10, description.getValue().indexOf(",")) + " 2014");
//			datesTaken.add(description.getValue().substring(6, description.getValue().indexOf(",")));
//		}

	}

	private static void createEntry(Month monthenum, Calendar cal, List<SyndEntry> entries, Boolean firstDate, String holdYear, int dayCount, boolean firstRun) {
		StringBuffer sb = new StringBuffer();
		sb.append("Today in Hooked On Sonics history: ");
		int gigCount = 1;
		for (SyndEntry entry : entries) {
			SyndContent description = entry.getDescription();
			String title = StringEscapeUtils.unescapeHtml(entry.getTitle());
			String where = description.getValue().substring((description.getValue().indexOf("Where:") + 7), (description.getValue().indexOf("<br>Event") - 1));
			String date = description.getValue().substring(10, (description.getValue().indexOf(",") + 6));
			String monthday = description.getValue().substring(10, (description.getValue().indexOf(",")));
			String year = description.getValue().substring(description.getValue().indexOf(",") + 2, description.getValue().indexOf(",") + 6);
			
			if (firstRun) {
				if (!year.equals(holdYear)) {
					System.out.println("\n==== Year = " + year);
					holdYear = year;
					 gigCount = 1;
				}
				System.out.println(gigCount + ". " +StringEscapeUtils.unescapeHtml(entry.getTitle()) + "   ---  " + date);
				 gigCount++;
			}
			String month = monthenum.make(cal.get(Calendar.MONTH) + 1).toShortString();
			if (date.contains(" " + (cal.get(Calendar.DATE) - (dayCount - 1)) + ",") && date.contains(month)) {
				if (firstDate) {
					sb.append(monthday + "\n");
				}
				firstDate = false;
				sb.append(year + " - " + removeHookedOs(title) + " (" + removePlace(title, where) + ")\n");
				
			}
		}
		if (firstRun) {
			System.out.println("============= Historic events ==========================");
		}
		System.out.println(sb.toString());
		
	}

	private static String removeHookedOs(String title) {
		if (title.contains("Hooked On Sonics @")){
			title = title.substring(19);
		}
		if (title.contains("HOS @")){
			title = title.substring(6);
		}
		return title;
	}
	
	private static String removePlace(String title, String where) {
		if (title.contains("Private") || title.contains("Private Party") || title.contains("Private Wedding")){
			where = "";
		}
//		if (where.contains(",")){
//			where = where.substring(where.indexOf(",") + 2);
//		}
		return where;
	}
	


}