package com.mitadventures.game;

import java.text.DecimalFormat;

public class Timer {

	private DecimalFormat df = new DecimalFormat("00");
	private int seconds;
	private int minutes;
	private long hours;
	private boolean ticking = false;
	
	public Timer() {
		seconds = 0;
		minutes = 0;
		hours = 0;
	}
	
	public Timer(long hours, int minutes, int seconds) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}
	
	public void start() {
		ticking = true;
	}
	
	public void stop() {
		ticking = false;
	}
	
	public void tick() {
		if (ticking) {
			seconds++;
			if (seconds >= 60) {
				minutes++;
				seconds = 0;
			}
			if (minutes >= 60) {
				hours++;
				minutes = 0;
			}
		}
	}
	
	public String getTime() {
		return df.format(hours) + ":" + df.format(minutes) + ":" + df.format(seconds);
	}
	
	public static Timer parseTimer(String time) {
		// 00:00:29
		int i = time.indexOf(':');
		long hours = Long.parseLong(time.substring(0, i));
		int minutes = Integer.parseInt(time.substring(i + 1, i + 3));
		int seconds = Integer.parseInt(time.substring(i + 4, i + 6));
		return new Timer(hours, minutes, seconds);
	}
}
