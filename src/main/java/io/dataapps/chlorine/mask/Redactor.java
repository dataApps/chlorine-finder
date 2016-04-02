package io.dataapps.chlorine.mask;

import io.dataapps.chlorine.finder.Finder;
import io.dataapps.chlorine.finder.FinderEngine;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Redactor implements Masker {
	private static final Log LOG = LogFactory.getLog(Redactor.class);

	Map<String,String> replacements ;
	FinderEngine engine;
	
	public void init(FinderEngine engine, Map<String,String> replacements) {
		this.engine = engine;
		this.replacements = replacements;
	}

	public void init(FinderEngine engine, String configurationFileName ) {
		init(engine, readReplacements(configurationFileName));
	}

	private static Map<String,String> readReplacements(String configurationFileName) {
		Properties properties =new Properties();
		Map<String,String> map = new HashMap<>();
		try (final InputStream stream =
				MaskFactory.class.getClassLoader().
				getResourceAsStream(configurationFileName)) {
			properties.load(stream);	
			for (final String name: properties.stringPropertyNames()) {
				map.put(name, properties.getProperty(name));	
			}
		}catch (IOException e) {
			LOG.error(e);
		}
		return map;
	}
	
	@Override
	public String mask(String input) {
		String temp = input;
		List<Finder> finders = engine.getFinders();
		for (Finder finder:finders) {
			String replacement = replacements.get(finder.getName());
			if (replacement != null) {
				if (finder instanceof Replacer) {
					temp = ((Replacer)finder).replace(temp, replacement);
				} else {
					List<String> values = finder.find(temp);
					for (String value:values) {
						temp = temp.replaceAll(value, replacement);
					}
				}
			}
		}
		return temp;
	}
	
	public Map<String, String> getReplacements() {
		return replacements;
	}
	public void setReplacements(Map<String, String> replacements) {
		this.replacements = replacements;
	}
	public FinderEngine getEngine() {
		return engine;
	}
	public void setEngine(FinderEngine engine) {
		this.engine = engine;
	}
	
	
}
