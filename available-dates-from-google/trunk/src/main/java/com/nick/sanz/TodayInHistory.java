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
		StringBuffer sb = new StringBuffer();
		for (SyndEntry entry : entries) {
			SyndContent description = entry.getDescription();
			String title = StringEscapeUtils.unescapeHtml(entry.getTitle());
			System.out.println(StringEscapeUtils.unescapeHtml(entry.getTitle()));
			String date = description.getValue().substring(10, (description.getValue().indexOf(",") + 6));
			String monthday = description.getValue().substring(10, (description.getValue().indexOf(",")));
			String year = description.getValue().substring(description.getValue().indexOf(",") + 2, description.getValue().indexOf(",") + 6);
			String month = monthenum.make(cal.get(Calendar.MONTH) + 1).toShortString();
			if (date.contains(" " + (cal.get(Calendar.DATE) - 0) + ",")
					&& date.contains(month)
					){
				sb.append("Today in Hooked On Sonics history: " + monthday + "\n");
				sb.append(year + " - " + title + "\n");
				sb.append("#HookedOnSonicsHistory\n\n");
			}
		}
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
}