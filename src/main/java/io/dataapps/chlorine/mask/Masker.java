package io.dataapps.chlorine.mask;

import io.dataapps.chlorine.finder.FinderEngine;

public interface Masker {
	void init (FinderEngine engine, String configuration);
	String mask(String input);
}
