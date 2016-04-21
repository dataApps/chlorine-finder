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

import java.util.List;
import java.util.regex.Pattern;

public class FinderUtil {
	
	public static String removeMatches(String input, List<String> matches) {
		return replaceMatches(input, matches, "");
	}
	
	public static String replaceMatches(String input, List<String> matches, String replacement) {
		if (matches.size() > 0) {
			StringBuilder sb = new StringBuilder("(");
			boolean first = true;
			for (String match:matches) {
				if (!first) {
					sb.append("|");
				}
				sb.append(Pattern.quote(match));
				first = false;
			}
			sb.append(")");
			
			return input.replaceAll(sb.toString(), replacement);
		} else {
			return input;
		}
	}
}
