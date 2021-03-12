package presentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author James Kelley
 * Genome Navigator and Genome Navigator Data Processor code
 * - likely used by both
 */
public class Interval {
	int start;
	int end;
	Interval() { 
		start = 0; 
		end = 0; 
	}
	Interval(int s, int e) {
		start = s; 
		end = e; 
	}
	
	public String keyString() {
		return start + "-" + end;
	}
	
	Interval(String keyString) {
		String[] entries = keyString.split("-");
		start = Integer.parseInt(entries[0]);
		end = Integer.parseInt(entries[1]);
	}
	
	public static ArrayList<String> sortedKeyStrings(List<String> keyStringList) {
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> startPosList = new ArrayList<String>();
		HashMap<String, String> startPosKeyStringMap = new HashMap<String, String>();
		for (int i = 0; i < keyStringList.size(); i++) {
			String[] entries = keyStringList.get(i).split("-");
			startPosList.add(entries[0]);
			startPosKeyStringMap.put(entries[0], keyStringList.get(i));
		}
		Collections.sort(startPosList, new IntComparator());
		for (int j = 0; j < startPosList.size(); j++) {
			list.add(startPosKeyStringMap.get(startPosList.get(j)));
		}
		
		return list;
	}
	
	public int size() {
		return end - start;
	}
	
	public int midpoint() {
		return (start + end)/2;
	}
	
	@Override
	public String toString() {
		return "[" + start + "," + end + "]";
	}
	
	public static boolean contains(ArrayList<Interval> intervals, Interval interval) {
		ArrayList<String> intervalKeyStrings = new ArrayList<String>();
		for (int i = 0; i < intervals.size(); i++) {
			intervalKeyStrings.add(intervals.get(i).keyString());
		}
		if (intervalKeyStrings.contains(interval.keyString())) {
			return true;
		}
		return false;
		
	}
	
	public static void main(String args[]) {
		List<String> keyStringList = new ArrayList<String>();
		keyStringList.add("10-20");
		keyStringList.add("1-2");
		keyStringList.add("100-200");
		keyStringList.add("31-52");
		ArrayList<String> sortedList = Interval.sortedKeyStrings(keyStringList);
		System.out.println(sortedList);
	}
}
