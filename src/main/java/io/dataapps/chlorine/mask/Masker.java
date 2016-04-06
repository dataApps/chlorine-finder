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
package io.dataapps.chlorine.mask;

import io.dataapps.chlorine.finder.FinderEngine;

/**
 * A class which implements a masking operation using
 * a FinderEngine
 *
 */

public interface Masker {
	
	/**
	 * initialize the Masker using the FinderEnginer and
	 * a string which provides a pointer for Masker's configuration.
	 * @param engine - FinderEngine
	 * @param configuration -String containing configuration information for
	 * Masker
	 */
	void init (FinderEngine engine, String configuration);
	
	/**
	 * Accepts an input text and mask it.
	 * @param input - text to be masked
	 * @return masked text
	 */
	
	String mask(String input);
}
