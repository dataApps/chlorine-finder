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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class which can contain a group of Finders. 
 * Tries to match with all of the internal Finders
 *
 */

public class CompositeFinder implements Finder {

	private String name;
	private List<Finder> finders = new ArrayList<>();

	/**
	 * Create a CompositeFinder.
	 * @param name
	 */
	public CompositeFinder(String name) {
		this.name = name;
	}

	/**
	 * set the name.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get name of the Finder.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set a list of Finders on the CompositeFInder
	 * @param finders
	 */
	public void setFinders(List<Finder> finders) {
		this.finders = finders;
	}

	/**
	 * Get a list of all finders associated with the CompositeFinder.
	 * @return All Finders associated with the CompositeFinder.
	 */
	public List<Finder> getFinders() {
		return finders;
	}

	/**
	 * add a Finder to the CompositeFinder
	 * @param finder
	 */
	public void add(Finder finder) {
		finders.add(finder);
	}

	/**
	 * Scan the input using the finders.
	 * Return a list of actual matched values.
	 * @return a list of matches 
	 */
	public FinderResult find(String input) {
		List<String> list = new ArrayList<>();
		String temp = input;
		for (Finder finder : finders) {
			FinderResult result = finder.find(temp);
			list.addAll(result.getMatches());
			temp = result.getMatchesRemoved();
		}
		return new FinderResult(list, temp);
	}

	/**
	 * Scan the list of inputs using the finders.
	 * Return a map of finder to actual matched values.
	 * @return a map of finder to the corresponding matches. 
	 */
	public Map<String, List<String>> findWithType(String input) {
		Map<String, List<String>> map = new HashMap<>();
		String temp = input;
		for (Finder finder : finders) {
			FinderResult result = finder.find(temp);
			addToMap(map, finder, result.getMatches());
			temp = result.getMatchesRemoved();

		}
		return map;
	}

	private void addToMap(Map<String, List<String>> map, Finder finder, List<String> matches) {
		if (!matches.isEmpty()) {
			List<String> existingMatches = map.get(finder.getName());
			if (existingMatches == null) {
				existingMatches = new ArrayList<>();
				map.put(finder.getName(), existingMatches);
			}
			existingMatches.addAll(matches);
		}
	}
}
