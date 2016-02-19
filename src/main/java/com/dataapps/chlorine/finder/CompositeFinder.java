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

	public CompositeFinder(String name) {
		this.name = name;
	}

	public List<Finder> getFinders() {
		return finders;
	}

	public void setFinders(List<Finder> finders) {
		this.finders = finders;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void add(Finder finder) {
		finders.add(finder);
	}
	
	public List<String> find(Collection<String> samples) {
		List<String> list = new ArrayList<>();
		for (Finder finder : finders) {
			list.addAll(finder.find(samples));
		}
		return list;
	}

	public List<String> find(String sample) {
		List<String> list = new ArrayList<>();
		for (Finder finder : finders) {
			list.addAll(finder.find(sample));
		}
		return list;
	}

	public Map<String, List<String>> findWithType(List<String> samples) {
		Map<String, List<String>> map = new HashMap<>();
		for (Finder finder : finders) {
			List<String> matches = finder.find(samples);
			addToMap(map, finder, matches);
		}
		return map;
	}

	public Map<String, List<String>> findWithType(String sample) {
		Map<String, List<String>> map = new HashMap<>();
		for (Finder finder : finders) {
			List<String> matches = finder.find(sample);
			addToMap(map, finder, matches);
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
