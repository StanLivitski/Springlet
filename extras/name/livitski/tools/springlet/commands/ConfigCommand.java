/**
 *    Copyright © 2013, 2014 Konstantin Livitski
 *     
 *    This file is part of Springlet. Springlet is
 *    licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package name.livitski.tools.springlet.commands;

import java.io.File;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import name.livitski.tools.springlet.ApplicationBean;
import name.livitski.tools.springlet.Command;

/**
 * Handles the <code>--config</code> command line switch and
 * stores the file argument inside this object.
 * @see #getFile()
 */
public class ConfigCommand extends Command
{
 /**
  * @param manager the data manager that may receive this
  * command
  */
 public ConfigCommand(ApplicationBean manager)
 {
  super(manager);
 }

 @Override
 public void process(ListIterator<String> args)
 {
  if (!args.hasNext())
   throw new NoSuchElementException("config-file argument missing");
  file = new File(args.next());
 }

 /**
  * Returns the location of the configuration file, if any, otherwise
  * returns <code>null</code>.
  */
 public File getFile()
 {
  return file;
 }

 public String getArgSpec()
 {
  return "config-file";
 }

 public String getSummary()
 {
  return "Points to the configuration file to use.";
 }

 private File file;
}
