package org.kite9.tool.model;

public class Construct {

	public static class Con1 {
		
		Con2 c2;
		
		public Con1() {
			c2 = new Con2();
		}
		
	}
	
	public static class Con2 {
		
		public Con2() {
			
		}
		
	}
}
