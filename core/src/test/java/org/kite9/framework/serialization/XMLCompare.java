package org.kite9.framework.serialization;

import java.io.IOException;

import javax.xml.transform.Source;

import org.xmlunit.builder.Input;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.ComparisonListener;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DOMDifferenceEngine;
import org.xmlunit.diff.DifferenceEngine;
import org.xmlunit.input.WhitespaceStrippedSource;

import junit.framework.Assert;

public class XMLCompare {

	public static boolean checkIdenticalXML(String s1, String s2) throws IOException {
		try {
			Source in1 = Input.fromString(s1).build();
			in1 = new WhitespaceStrippedSource(in1);
			Source in2 = Input.fromString(s2).build();
			in2 = new WhitespaceStrippedSource(in2);
			
			//DiffBuilder.

			DifferenceEngine diff = new DOMDifferenceEngine();
			diff.addDifferenceListener(new ComparisonListener() {
				
		        public void comparisonPerformed(Comparison comparison, ComparisonResult outcome) {
		            Assert.fail("found a difference: " + comparison);
		        }
		    });
			
			diff.compare(in1, in2);
		} catch (NullPointerException e) {
			Assert.fail("Missing diagram file: " + e.getMessage());
			return false;
		}
	
		return true;
	}
}
