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

import java.util.List;

public class FinderResult {
	
	List<String> matches;
	String matchesRemoved;
	
	public FinderResult() {}
	
	public FinderResult (List<String> matches, String matchesRemoved) {
		this.matches = matches;
		this.matchesRemoved = matchesRemoved;
	}
	public List<String> getMatches() {
		return matches;
	}
	public void setMatches(List<String> matches) {
		this.matches = matches;
	}
	public String getMatchesRemoved() {
		return matchesRemoved;
	}
	public void setMatchesRemoved(String matchesRemoved) {
		this.matchesRemoved = matchesRemoved;
	}
}
