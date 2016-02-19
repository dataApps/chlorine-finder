/*
 * Copyright 2016, DataApps Corporation (http://dataApps.io) .
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dataapps.chlorine.finder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.dataapps.chlorine.finder.Finder;
import com.dataapps.chlorine.finder.FinderEngine;

public class TestDefaultFinders {

	private static String TEXT_PART1 = "text containing some stuff - ";
	private static String TEXT_PART2 = " - end of text";

	//emails
	private static String EMAIL1 = "b@b.com";
	private static String EMAIL2 = "b.b@b.com";
	private static String EMAIL3 = "bob_smith@foo.com";
	private static String EMAIL4 = "bob-smith@foo.com";

	//credit cards
	private static String MASTERCARD1 = "5555555555554444";
	private static String MASTERCARD2 = "5105105105105100";
	private static String VISACARD1 = "4111111111111111";
	private static String VISACARD2 = "4012888888881881";
	//private static String VISACARD3 = "4222222222222";
	private static String AMEXCARD1 = "378282246310005";
	private static String AMEXCARD2 = "371449635398431";
	private static String AMEXCARD3 = "378734493671000";
	private static String DINERSCLUB1 = "30569309025904";
	private static String DINERSCLUB2 = "38520000023237";
	private static String DISCOVER1 = "6011111111111117";
	private static String DISCOVER2 = "6011000990139424";
	private static String JCB1 = "3530111333300000";
	private static String JCB2= "3566002020360505";

	//Phone Numbers
	private static String USPHONE1 = "333-444-5555";
	private static String USPHONE2 = "(658) 154 1122";
	private static String USPHONE3 = "3334445555";
	private static String USPHONE4 = "(658)1541122";

	//ips
	private static String IP1 = "127.0.0.1";
	private static String IP2 = "123.345.23.123";
	private static String IP3 = "123.abc.23.123";

	//addresses
	private static String ADDRESS1 = "2345 Nulla St";
	private static String ADDRESS2 = "8562 Fusce Rd";
	private static String ADDRESS3 = "Somecity CA 2345";

	//SSNs
	private static String SSN1 = "576-16-2345";
	private static String SSN2 = "576 16 2345";

	//ZIPCodes
	private static String ZIP1 = "95050";

	private static String URL1 = "https://www.dataApps.io";
	
	private static String IPV6_1 = "2001:db8:a0b:12f0::1";

	
	public static String[] FINDERNAMES = new String[] {"Email", "Credit Card", "US Phone#Formatted", "IPV4", "Street Address", "SSN-dashes" , "SSN-spaces" };

	private static String[] testStrings = new String[] { 
			EMAIL1, EMAIL2, EMAIL3, EMAIL4, 
			MASTERCARD1, MASTERCARD2,
			VISACARD1, VISACARD2, AMEXCARD1, AMEXCARD2, AMEXCARD3,
			DINERSCLUB1, DINERSCLUB2, DISCOVER1, DISCOVER2, JCB1, JCB2,
			USPHONE1, USPHONE2, 
			IP1, IP2,
			ADDRESS1, ADDRESS2,
			SSN1, SSN2,
			ZIP1,
			URL1,
			IPV6_1
			};

	private static String[] testStringTypes = new String[] { 
			"Email", "Email", "Email", "Email", 
			"Credit Card", "Credit Card", 
			"Credit Card", "Credit Card", "Credit Card", "Credit Card", "Credit Card",
			"Credit Card", "Credit Card", "Credit Card", "Credit Card", "Credit Card", "Credit Card",
			"US Phone#Formatted", "US Phone#Formatted", 
			"IPV4", "IPV4",
			"Street Address", "Street Address",
			"SSN-dashes","SSN-spaces",
			"ZipCode",
			"URL",
			"IPV6"
			
	};

	private static String[] testBadStrings = new String[] {USPHONE3, USPHONE4, IP3, ADDRESS3};


	private static String PLAIN_TEXT = "text containing no sensitive elements";

	@Test
	public void testFinders () {
		FinderEngine engine = new FinderEngine();
		assertEquals (FINDERNAMES.length, engine.getFinders().size());
		for (String finderName: FINDERNAMES) {
			assertTrue ("Finder Not Found:" + finderName, 
					contains(finderName, engine.getFinders()));
		}
	}

	private static boolean contains(String finderName, List<Finder> finders)  {
		for (Finder finder: finders) {
			if (finder.getName().equalsIgnoreCase(finderName)) {
				return true;
			}
		}
		return false;
	}

	@Test 
	public void testMatch() {
		for (String str: testStrings) {
			FinderEngine engine = new FinderEngine((List<Finder>)null, true, true);
			List<String> results = engine.find(TEXT_PART1 + str + TEXT_PART2);
			if (results.size() > 1)  {
				// This is an error condition, Print some helpful information on what happened
				Map<String, List<String>> matchesByType = engine.findWithType(TEXT_PART1 + str + TEXT_PART2);
				for (Map.Entry<String, List<String>> entry : matchesByType.entrySet()) {
					System.out.println("Finder Name:"+ entry.getKey());
					for (String value: entry.getValue()) {
						System.out.println("Value:"+ value);
					}
				}
			}
			assertEquals (str + " is not found. ", 1, results.size());
			assertEquals(str, results.get(0));
		}
	}

	@Test 
	public void testNonMatch() {
		for (String str: testBadStrings) {
			FinderEngine engine = new FinderEngine();
			List<String> results = engine.find(TEXT_PART1 + str + TEXT_PART2);
			assertEquals (str + " is  found. ", 0, results.size());
		}
	}

	@Test
	public void testNoMatch () {
		FinderEngine engine = new FinderEngine();
		List<String> emails = engine.find(PLAIN_TEXT);
		assertTrue (emails.isEmpty());
	}

	@Test 
	public void testMatchWithType() {
		int i = 0;
		for (String str: testStrings) {
			FinderEngine engine = new FinderEngine((List<Finder>)null, true, true);
			Map<String, List<String>> results = engine.findWithType(TEXT_PART1 + str + TEXT_PART2);
			if (results.size() > 1)  {
				// This is an error condition, Print some helpful information on what happened
				for (Map.Entry<String, List<String>> entry : results.entrySet()) {
					System.out.println("Finder Name:"+ entry.getKey());
					for (String value: entry.getValue()) {
						System.out.println("Value:"+ value);
					}
				}
			}
			assertEquals (str + " is not found. ", 1, results.size());
			String actualType = null;
			for (Map.Entry<String, List<String>> entry : results.entrySet()) {
				actualType = entry.getKey();
			}
			assertEquals (testStringTypes[i++], actualType);
			List<String>  matches = results.get(actualType);
			if (matches.size() > 1) {
				for (String value: matches) {
					System.out.println("Value:"+ value);
				}
			}
			assertEquals(str, matches.get(0));
		}
	}

	@Test 
	public void testDisabledFinders () {
		FinderEngine engine = new FinderEngine();
		List<String> results = engine.find( TEXT_PART1 + ZIP1);
		assertEquals (ZIP1 + " is  found. ", 0, results.size());
	}
}
