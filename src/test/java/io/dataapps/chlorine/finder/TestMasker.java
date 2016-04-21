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
package io.dataapps.chlorine.finder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import io.dataapps.chlorine.mask.MaskFactory;
import io.dataapps.chlorine.mask.Masker;

import java.util.List;

import org.junit.Test;

public class TestMasker {

	private static String TEXT_PART1 = "text containing  - ";
	private static String TEXT_PART2 = " - end of text";

	//emails
	private static String EMAIL1 = "b@b.com";
	private static String EMAIL2 = "b.b@b.com";

	//credit cards
	private static String MASTERCARD1 = "5555555555554444";
	private static String DINERSCLUB1 = "30569309025904";

	//Phone Numbers
	private static String USPHONE1 = "333-444-5555";
	private static String USPHONE2 = "(658) 154 1122";
	private static String USPHONE3 = "3334445555";
	private static String USPHONE4 = "(658)1541122";

	//ips
	private static String IP1 = "127.0.0.1";
	private static String IP2 = "123.345.23.123";

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


	public static String[] FINDERNAMES = new String[] {"Email", "Credit Card",
		"US Phone#Formatted", "IPV4", "Street Address", "SSN-dashes" , "SSN-spaces"};

	private static String[] testStrings = new String[] { 
		EMAIL1, 
		MASTERCARD1,
		USPHONE1, USPHONE2, 
		IP1, IP2,
		ADDRESS1, ADDRESS2,
		SSN1, SSN2,
		ZIP1,
		URL1,
		IPV6_1
	};

	private static String[] testBadStrings = new String[] {USPHONE3, USPHONE4, ADDRESS3};

	private static String PLAIN_TEXT = "text containing no sensitive elements";

	private static  String multipleEmails = EMAIL1 + "," + EMAIL2;

	private static  String multipleCreditCards =  "a"+ MASTERCARD1 + "," + DINERSCLUB1  + " ";;

	@Test
	public void testMasker() {
		MaskFactory maskFactory = new MaskFactory();
		assertNotNull(maskFactory.getMaskers());
		assertEquals (1, maskFactory.getMaskers().size());
	}

	@Test 
	public void testMatch() {
		FinderEngine engine = new FinderEngine((List<Finder>)null, true, true);
		MaskFactory maskFactory = new MaskFactory(engine);
		Masker masker = maskFactory.getMasker();
		for (String str: testStrings) {
			String input = TEXT_PART1 + str + TEXT_PART2;
			String result = masker.mask(input);
			List<String> elements = engine.find(input).getMatches();
			assertEquals (1,elements.size());
			for (String element:elements) {
				assertFalse("element=" +element + ", str=" +str,result.contains(element));
			}
		}
	}

	@Test 
	public void testNonMatch() {
		MaskFactory maskFactory = new MaskFactory();
		Masker masker = maskFactory.getMasker();
		for (String str: testBadStrings) {
			String input = TEXT_PART1 + str + TEXT_PART2;
			String output = masker.mask(input);
			assertEquals(input,output);
		}
	}

	@Test
	public void testNoMatch () {
		MaskFactory maskFactory = new MaskFactory();
		Masker masker = maskFactory.getMasker();
		String result = masker.mask(PLAIN_TEXT);
		assertEquals(PLAIN_TEXT, result);
	}

	@Test 
	public void testMultipleEmailsWithCommas () {
		MaskFactory maskFactory = new MaskFactory();
		Masker masker = maskFactory.getMasker();
		String result = masker.mask(multipleEmails);
		assertFalse(result.contains(EMAIL1));
		assertFalse(result.contains(EMAIL2));
	}

	@Test 
	public void testMultipleCreditCardsWithCommas () {
		MaskFactory maskFactory = new MaskFactory();
		Masker masker = maskFactory.getMasker();
		String result = masker.mask(multipleCreditCards);
		assertFalse(result.contains(MASTERCARD1));
		assertFalse(result.contains(DINERSCLUB1));
	}
}
