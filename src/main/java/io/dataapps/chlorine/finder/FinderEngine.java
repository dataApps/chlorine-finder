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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A class which houses a buch of Finders 
 * The FinderEngine relies on a list of FinderProviders to create Finders.
 * It also accepts a list of Finders.
 * During initialization, a DefaultFinderProvider is used.
 * 
 *
 */
public class FinderEngine extends CompositeFinder {
	private static final Log LOG = LogFactory.getLog(FinderEngine.class);

	/**
	 * Creates a FinderEngine. 
	 * Adds all Default Finders.
	 */
	public FinderEngine() {
		this((List<Finder>)null, true);
	}
	
	/**
	 * Creates a FinderEngine. 
	 * Reads all finders from file.
	 * The file syntax is as follows :
	 * <?xml version="1.0" encoding="UTF-8" standalone="no"?> 
	 * <finders>
	 *    <finder>
	 *	     <name>Email</name>
	 *	     <pattern>(\D|^)[A-Z0-9._%+-]+@([A-Z0-9.-]+)\.([A-Z]{2,4})(\D|$)</pattern>
	 *	     <enabled>true</enabled>
	 *    </finder>
	 *    ...
	 *    <finder>
     *		<class>com.example.BirthDateFinder</class>
	 *	    <enabled>true</enabled>
	 *    </finder>
	 *    ... 
	 * </finders>  
	 *    
	 *    
	 *    
	 * </finders>
	 * Adds all Default Finders.
	 */
	public FinderEngine(String fileName) {
		this(fileName, false);
	}
	
	/**
	 * Creates a FinderEngine. 
	 * Reads all finders from file.
	 * If fromClassPath is true, reads the file from the class path.
	 * 
	 * Adds all Default Finders.
	 */
	public FinderEngine(String fileName, boolean fromClassPath) {
		this(fileName, fromClassPath, true);
	}
	
	/**
	 * Creates a FinderEngine. 
	 * Reads all finders from file.
	 * If fromClassPath is true, reads the file from the class path.
     * If addDefaultFinders is true, adds all Default Finders.
	 */
	public FinderEngine(String fileName, boolean fromClassPath, boolean addDefaultFinders) {
		this(new DefaultFinderProvider(getInputStream(fileName, fromClassPath)), addDefaultFinders);
	}
	
	private static InputStream getInputStream(String fileName, boolean fromClassPath) {
		InputStream in = null;
		if (fromClassPath) {
			 in = FinderEngine.class.getClassLoader()
					.getResourceAsStream(fileName);
		} else {
			 try {
				in = new FileInputStream(fileName);
			} catch (FileNotFoundException e) {
				LOG.warn(e.getMessage());
			}
		}
		return in;
	}

	/**
	 * Creates a FinderEngine. 
	 * Reads all finders from InputStream.
	 * Adds all Default Finders.
	 */
	public FinderEngine(InputStream in) {
		this(new DefaultFinderProvider(in), true);
	}
	
	
	/**
	 * Creates a FinderEngine. 
	 * Includes all finders from FinderProvider. 
     * If addDefaultFinders is true, adds all Default Finders.
	 */
	public FinderEngine(InputStream in, boolean addDefaultFinders) {
		this(new DefaultFinderProvider(in), addDefaultFinders);
	}

	/**
	 * Creates a FinderEngine. 
	 * Includes all finders from FinderProvider. 
	 * Adds all Default Finders.
	 */
	public FinderEngine(FinderProvider finderProvider) {
		this(finderProvider.getFinders(), true);
	}
	
	/**
	 * Creates a FinderEngine. 
	 * Includes all finders from the given FinderProviders. 
	 * Adds all Default Finders.
	 */
	public FinderEngine(Set<FinderProvider> finderProviders) {
		this(createFinders(finderProviders), true);
	}
	
	/**
	 * Creates a FinderEngine.
	 * Includes all finders from the given FinderProvider.
     * If addDefaultFinders is true, adds all Default Finders.
	 */
	public FinderEngine(FinderProvider finderProvider, boolean addDefaultFinders) {
		this(finderProvider.getFinders(), addDefaultFinders);
	}
	
	/**
	 * Creates a FinderEngine. 
	 * Include all finders from FinderProviders. 
	 * If addDefaultFinders is true, adds all Default Finders.
	 */
	public FinderEngine(Set<FinderProvider> finderProviders, boolean addDefaultFinders) {
		this(createFinders(finderProviders), addDefaultFinders);
	}

	/**
	 * Creates a FinderEngine.
	 * Includes all finders from the list of Finders. 
	 * Adds all Default Finders.
	 */
	public FinderEngine(List<Finder> finders) {
		this(finders, true);
	}
	
	/**
	 * Creates a FinderEngine.
	 * Includes all finders from the list of Finders. 
	 * If addDefaultFinders is true, adds all Default Finders.
	 */
	public FinderEngine(List<Finder> finders, boolean addDefaultFinders) {
		this(finders, addDefaultFinders, false);
	}

	/**
	 * Creates a FinderEngine.
	 * Includes all finders from the list of Finders. 
	 * If addDefaultFinders is true, adds all Default Finders.
	 * If ignoreEnabledFlag is true, includes all disabled Finders too. This is used for testing.
	 * 
	 */
	 public FinderEngine(List<Finder> finders, boolean addDefaultFinders, boolean ignoreEnabledFlag) {
		super("FinderEngine");
		List<Finder> list = new ArrayList<>();
		if (finders != null) {
			list.addAll(finders);
		}
		if (addDefaultFinders) {
			list.addAll(new DefaultFinderProvider(ignoreEnabledFlag).getFinders());
		}
		setFinders(list);
	}

	private  static List<Finder> createFinders(Set<FinderProvider> finderProviders) {
		List<Finder> finders = new ArrayList<>();
		for (FinderProvider provider: finderProviders) {
			finders.addAll(provider.getFinders());
		}
		return finders;
	}
}
