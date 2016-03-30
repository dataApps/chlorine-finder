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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The MaskFactory looks up maskers.xml in the classpath.
 * Reads all the maskers and uses it for Scanning.
 * 
 *  The file syntax:
 * <?xml version="1.0" encoding="UTF-8" standalone="no"?> 
 * <configuration>
 * <maskers>
 *    <masker>
 *	     <name>nameofthemasker</name>
 *	     <class>classforthemasker</class>
 *	     <configuration>true</configuration>
 *    </masker>
 * </maskers>
 * <default>nameofthemasker</dafault>
 * </configuration>
 *
 */
public class MaskFactory  {
	public static final String MASK_DEFAULT_XML = "mask_default.xml";
	private static final Log LOG = LogFactory.getLog(MaskFactory.class);

	Map<String,Masker> maskers = new HashMap<> ();
	private FinderEngine engine;
	private String defaultMasker;

	public MaskFactory() {
		this(new FinderEngine());

	}

	public MaskFactory(FinderEngine engine) {
		this(engine, MaskFactory.class.getClassLoader()
				.getResourceAsStream(MASK_DEFAULT_XML));
	}

	public MaskFactory(FinderEngine fEngine, InputStream in) {
		engine = fEngine;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				boolean bName = false;
				boolean bClass = false;
				boolean bConfiguration = false;
				boolean bDefault = false;

				String name = "";
				String className = "";
				String configuration = "";
				String defaultStr;

				public void startElement(String uri, String localName,String qName, 
						Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("NAME")) {
						bName = true;
					} else if (qName.equalsIgnoreCase("CLASS")) {
						bClass = true;
					} else if (qName.equalsIgnoreCase("CONFIGURATION")) {
						bConfiguration = true;
					} else if (qName.equalsIgnoreCase("DEFAULT")) {
						bDefault = true;
					} 
				}

				public void endElement(String uri, String localName,
						String qName) throws SAXException {

					if (qName.equalsIgnoreCase("NAME")) {
						bName = false; name = name.trim();
					} else if (qName.equalsIgnoreCase("CLASS")) {
						bClass = false; className = className.trim();
					} else if (qName.equalsIgnoreCase("CONFIGURATION")) {
						bConfiguration = false; configuration = configuration.trim();
					} else if (qName.equalsIgnoreCase("DEFAULT")) {
						bDefault = false; defaultMasker = defaultStr.trim();
					} else if (qName.equalsIgnoreCase("MASKER")) {

						if (!name.isEmpty() && !className.isEmpty() && !configuration.isEmpty()) {
							try {
								Class<?> klass = Thread.currentThread().getContextClassLoader().loadClass(className);
								Masker masker = (Masker) klass.newInstance();
								masker.init(engine,configuration);
								maskers.put(name,masker);
							} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
								LOG.error(e);
							}
						}  else {
							if (name.isEmpty()) {
								LOG.error("The name for a masker cannot be empty");
							}
							if (className.isEmpty()) {
								LOG.error("The class name for a masker cannot be empty");
							}		
							if (configuration.isEmpty()) {
								LOG.error("The configuration for a masker cannot be empty");
							}
						}
						name = ""; configuration = ""; className = ""; defaultStr = "";
					}
				}

				public void characters(char ch[], int start, int length) throws SAXException {
					if (bName) {
						name += new String(ch, start, length);
					} else if (bClass) {
						className += new String(ch, start, length);
					} else if (bConfiguration) {
						configuration += new String(ch, start, length);
					} else if (bDefault) {
						defaultStr += new String(ch, start, length);
					}
				}
			};

			saxParser.parse(in, handler);

		} catch (Exception e) {
			LOG.error(e);
		}
	}

	public Map<String,Masker> getMaskers() {
		return maskers;
	}

	public Masker getMasker() {
		return maskers.get(defaultMasker);
	}

	public Masker getMasker(String name) {
		return maskers.get(name);
	}

	public FinderEngine getFinderEngine() {
		return engine;
	}	


}
