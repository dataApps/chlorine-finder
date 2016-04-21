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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.dataapps.chlorine.pattern.RegexFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TestAddFinders {
	private static String TEXT_PART1 = "text containing some stuff - ";
	private static String TEXT_PART2 = " - end of text";
	
	 static class SantaClaraZipCodeFinder implements Finder {

		@Override
		public String getName() {
			return "Santa Clara ZipCode";
		}

		@Override
		public FinderResult find(String input) {
			List<String> zipsFound = new ArrayList<>();
			if (input.contains("95050")) {
				zipsFound.add ("95050");
			}
			if (input.contains("95051")) {
				zipsFound.add ("95051");
			}
			if (input.contains("95054")) {
				zipsFound.add ("95054");
			}
			return new FinderResult (zipsFound,input.replaceAll("(95050|95051|95054)", ""));
		}
	}
	
	 static class DummyFinderProvider implements FinderProvider {

		List<Finder> finders = new ArrayList<>();

		public DummyFinderProvider() {
			Finder reservedWordFinder = new RegexFinder("Reserved Word Finder" ,
					"\\b(?:class|interface|void|static|public|private)\\b");
			SantaClaraZipCodeFinder scZipFinder = new SantaClaraZipCodeFinder();
			finders.add(scZipFinder);
			finders.add(reservedWordFinder);
		}

		@Override
		public List<Finder> getFinders() {
			return finders;
		}

	}

	@Test
	public void testAdd() {
		FinderEngine engine = new FinderEngine();
		Finder scFinder = new SantaClaraZipCodeFinder();
		engine.add(scFinder);
		
		List<String> matches = engine.find(TEXT_PART1 + "b@b.com" + TEXT_PART2).getMatches();
		assertTrue(matches.size()==1);
		assertEquals("b@b.com", matches.get(0));
		matches = engine.find(TEXT_PART1 + "95050" + TEXT_PART2).getMatches();
		assertEquals(1, matches.size());
		assertEquals("95050", matches.get(0));
		matches = engine.find(TEXT_PART1 + "94090" + TEXT_PART2).getMatches();
		assertEquals(0, matches.size());

		Map<String, List<String>> matchesByType = engine.findWithType(TEXT_PART1 + "95050" + TEXT_PART2);
		assertEquals(1, matchesByType.size());
		matchesByType.containsKey(scFinder.getName());
	}
	
	@Test
	public void testAddList() {
		List<Finder> lstFinders = new ArrayList<>();
		Finder scFinder = new SantaClaraZipCodeFinder();
		Finder reservedWordFinder = new RegexFinder("Reserved Word Finder" ,
				"\\b(?:class|interface|void|static|public|private)\\b");
		lstFinders.add(reservedWordFinder);
		lstFinders.add(scFinder);
		
		FinderEngine engine = new FinderEngine(lstFinders, false);
		
		List<String> matches = engine.find(TEXT_PART1 + "b@b.com" + TEXT_PART2).getMatches();
		assertEquals(0, matches.size());
		
		matches = engine.find(TEXT_PART1 + "95050" + TEXT_PART2).getMatches();
		assertEquals(1, matches.size());
		assertEquals("95050", matches.get(0));
		matches = engine.find(TEXT_PART1 + "94090" + TEXT_PART2).getMatches();
		assertEquals(0, matches.size());

		Map<String, List<String>> matchesByType = engine.findWithType(TEXT_PART1 + "95050" + TEXT_PART2);
		assertEquals(1, matchesByType.size());
		matchesByType.containsKey(scFinder.getName());
		
		matches = engine.find(TEXT_PART1 + "public" + TEXT_PART2).getMatches();
		assertEquals(1, matches.size());
		assertEquals("public", matches.get(0));

		matchesByType = engine.findWithType(TEXT_PART1 + "class" + TEXT_PART2);
		assertEquals(1, matchesByType.size());
		matchesByType.containsKey(reservedWordFinder.getName());
	}
	
	@Test
	public void testAddProvider() {
		
		FinderEngine engine = new FinderEngine(new DummyFinderProvider() , false);
		
		List<String> matches = engine.find(TEXT_PART1 + "b@b.com" + TEXT_PART2).getMatches();
		assertEquals(0, matches.size());
		
		matches = engine.find(TEXT_PART1 + "95050" + TEXT_PART2).getMatches();
		assertEquals(1, matches.size());
		assertEquals("95050", matches.get(0));
		matches = engine.find(TEXT_PART1 + "94090" + TEXT_PART2).getMatches();
		assertEquals(0, matches.size());

		Map<String, List<String>> matchesByType = engine.findWithType(TEXT_PART1 + "95050" + TEXT_PART2);
		assertEquals(1, matchesByType.size());
		matchesByType.containsKey("Santa Clara ZipCode");
		
		matches = engine.find(TEXT_PART1 + "public" + TEXT_PART2).getMatches();
		assertEquals(1, matches.size());
		assertEquals("public", matches.get(0));

		matchesByType = engine.findWithType(TEXT_PART1 + "class" + TEXT_PART2);
		assertEquals(1, matchesByType.size());
		matchesByType.containsKey("Reserved Word Finder");
	}
	
	@Test 
	public void testReadFromAFileUsingClassPath () {
		FinderEngine engine = new FinderEngine("testfinders.xml", true);
		assertEquals (TestDefaultFinders.FINDERNAMES.length+2, engine.getFinders().size());
	}
	
	@Test 
	public void testReadFromAFileUsingPath () {
		String fileName = this.getClass().getClassLoader()
		.getResource("testfinders.xml").getFile();
		FinderEngine engine = new FinderEngine(fileName);
		assertEquals (TestDefaultFinders.FINDERNAMES.length+2, engine.getFinders().size());
	}

}
