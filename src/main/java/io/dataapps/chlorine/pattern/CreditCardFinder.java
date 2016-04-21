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
package io.dataapps.chlorine.pattern;

import io.dataapps.chlorine.finder.FinderResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class CreditCardFinder extends RegexFinder {
	public CreditCardFinder(String name, String pattern) {
		super(name, pattern);
	}

	@Override
	public FinderResult find(String input) {
		List<String> matches = new ArrayList<>();
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {

			String match = input.substring(matcher.start()+1, matcher.end()-1);

			if (postMatchCheck(match)) {
				matches.add(match);
			}
		}
		return new FinderResult(matches, 
				FinderUtil.removeMatches(input, matches));
	}


	protected boolean postMatchCheck(String match) {
		String numerics = match.replaceAll("[^\\d.]", "");
		int[] digits = new int[numerics.length()];

		for (int i=0; i < digits.length; i++) {
			char c = numerics.charAt(i);
			digits[i] = c - '0';
		}

		return luhnCheck(digits);
	}

	private static boolean luhnCheck(int[] digits) {
		// http://stackoverflow.com/questions/20740444/check-credit-card-validity-using-luhn-algorithm
		int sum = 0;
		int length = digits.length;
		for (int i = 0; i < length; i++) {

			int digit = digits[length - i - 1];
			if (i % 2 == 1) {
				digit *= 2;
			}
			sum += digit > 9 ? digit - 9 : digit;
		}
		return sum % 10 == 0;
	}
}
