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


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.dataapps.chlorine.finder.Finder;

public class RegexFinder implements Finder {
	static final Log LOG = LogFactory.getLog(RegexFinder.class);
	public static final int DEFAULT_FLAGS = 
			Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE;

	private String name;
    Pattern pattern;
    
    public RegexFinder () {}

	public RegexFinder (String name, String pattern) {
		this(name, pattern,DEFAULT_FLAGS);
	}
	
	public RegexFinder (String name, String pattern, int flags) {
		this.name = name;
		this.pattern = Pattern.compile(pattern, flags);
	}

	public List<String> find( Collection<String> samples) {
		List<String> matches = new ArrayList<>();
		for (String sample : samples) {
			matches.addAll(find (sample));
		}
		return matches;
	}

	public List<String> find(String sample) {
		List<String> matches = new ArrayList<>();
		Matcher matcher = pattern.matcher(sample);
		while (matcher.find()) {
			matches.add(removeCommas(
					sample.substring(matcher.start(), matcher.end())).trim());
		}
		return matches;
	}

	public String getName() {
		return name;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static String removeCommas(String match) {
		if (match.endsWith(",")) {
			match = match.substring(0, match.length() - 1);
		}
		if (match.startsWith(",")) {
			match = match.substring(1);
		}
		return match;
	}
}