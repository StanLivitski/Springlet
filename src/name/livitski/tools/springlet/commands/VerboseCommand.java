/**
 *    Copyright © 2013 Konstantin "Stan" Livitski
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

import java.util.ListIterator;
import java.util.logging.Level;

import name.livitski.tools.springlet.ApplicationBean;
import name.livitski.tools.springlet.Command;
import name.livitski.tools.springlet.Logging;

/**
 * Handles the <code>--verbose</code> command line switch
 * for an {@link ApplicationBean application}. Note that
 * settings imposed by this command will not apply to logging
 * that occurred before it is processed.
 */
public class VerboseCommand extends Command
{
 /**
  * @param app the application's main bean
  */
 public VerboseCommand(ApplicationBean app)
 {
  super(app);
 }

 @Override
 public void process(ListIterator<String> args)
 {
  Level level = Level.FINE; 
  String arg = fetchArgument(args);
  String subsystem = null;
  if (null != arg)
  {
   level = Level.parse(arg.toUpperCase());
   arg = fetchArgument(args);
   if (null != arg)
   {
    arg = arg.trim();
    if (".".equals(arg))
     subsystem = "";
    else
     subsystem = arg;
   }
  }
  // null == subsystem means unspecified
  if (null == subsystem)
  {
   final Package pkg = getApplicationBean().getClass().getPackage();
   subsystem = null == pkg ? null : pkg.getName();
  }
  else if (0 == subsystem.length())
  {
   subsystem = null;
  }
  // null == subsystem means root
  Logging.setJDKLogLevel(level, subsystem);
 }

 public String getArgSpec()
 {
  return "[level [subsystem]]";
 }

 public String getSummary()
 {
  return "Controls the amount of logging produced by the application."
  	+ " The level argument is either a number or a name of a java.util.logging.Level"
  	+ " constant, case-insensitive. When omitted, FINE is assumed. The subsystem"
  	+ " argument is either a package name, a class name, or a period. The period"
  	+ " denotes root logger. When subsystem is omitted, the package of"
  	+ " application bean's class is assumed. A command line may contain more than "
  	+ " one --verbose command to apply different levels to different subsystems.";
 }
}
