package com.nick.sanz;
import java.net.URL;
import java.text.DateFormatSymbols;
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
		StringBuffer sb = new StringBuffer();
		sb.append("Today in Hooked On Sonics history: ");
		Boolean firstDate = true;
		for (SyndEntry entry : entries) {
			SyndContent description = entry.getDescription();
			String title = StringEscapeUtils.unescapeHtml(entry.getTitle());
			String where = description.getValue().substring((description.getValue().indexOf("Where:") + 7), (description.getValue().indexOf("<br>Event") - 1));
			System.out.println(StringEscapeUtils.unescapeHtml(entry.getTitle()));
			String date = description.getValue().substring(10, (description.getValue().indexOf(",") + 6));
			String monthday = description.getValue().substring(10, (description.getValue().indexOf(",")));
			String year = description.getValue().substring(description.getValue().indexOf(",") + 2, description.getValue().indexOf(",") + 6);
			String month = monthenum.make(cal.get(Calendar.MONTH) + 1).toShortString();
			if (date.contains(" " + (cal.get(Calendar.DATE) + 1) + ",")
					&& date.contains(month)
					){
				if (firstDate){
					sb.append(monthday + "\n\n");	
				}else{
					sb.append( "\n");
				}
				firstDate = false;
				sb.append(year + " - " + title + " (" + where + ")\n");
				
			}
		}
		sb.append("#HookedOnSonicsHistory\n\n");
		System.out.println("============= Historic events ==========================");
		System.out.println(sb.toString());
		System.out.println("============= Historic events - END ==========================");

		//		System.out.println("============= Historic events ==========================");
//		for (SyndEntry entry : entries) {
//			SyndContent description = entry.getDescription();
//			System.out.println(description.getValue().substring(10, description.getValue().indexOf(",")) + " 2014");
//			datesTaken.add(description.getValue().substring(6, description.getValue().indexOf(",")));
//		}

	}
	
	public enum Month {
		Jan(1), Feb(2), Mar(3), Apr(4), May(5), Jun(6), Jul(7), Aug(8), Sep(9), Oct(10), Nov(11), Dec(12);

		private static DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
		private static final int[] LAST_DAY_OF_MONTH = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		public int index;

		Month(int index) {
			this.index = index;
		}

		public static Month make(int monthIndex) {
			for (Month m : Month.values()) {
				if (m.index == monthIndex)
					return m;
			}
			throw new IllegalArgumentException("Invalid month index " + monthIndex);
		}

		public int lastDay() {
			return LAST_DAY_OF_MONTH[index];
		}

		public int quarter() {
			return 1 + (index - 1) / 3;
		}

		public String toString() {
			return dateFormatSymbols.getMonths()[index - 1];
		}

		public String toShortString() {
			return dateFormatSymbols.getShortMonths()[index - 1];
		}

		public static Month parse(String s) {
			s = s.trim();
			for (Month m : Month.values())
				if (m.matches(s))
					return m;

			try {
				return make(Integer.parseInt(s));
			} catch (NumberFormatException e) {
			}
			throw new IllegalArgumentException("Invalid month " + s);
		}

		private boolean matches(String s) {
			return s.equalsIgnoreCase(toString()) || s.equalsIgnoreCase(toShortString());
		}
	}

}