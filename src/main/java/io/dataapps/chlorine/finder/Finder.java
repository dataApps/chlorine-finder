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


/**
 * Interface for Finders
 * To add a new Java Finder, one needs to implement Finder interface.
 *
 */

public interface Finder {

  /**
   * Name of the Finder
   * @return name of the Finder
   */
  public String getName();
  
  /**
   * Accepts an input value and 
   * returns a collection of sensitive values found in the inputs.
   * @param input - String to be scanned.
   * @return List of sensitive values
   */
  public FinderResult find(String input);
    
}
