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

package name.livitski.tools.springlet;

import java.util.ListIterator;

/**
 * Extend this class to add a command with optional arguments to
 * the application's command line, or provide the default handler
 * for unclaimed arguments. Subclasses interpret and apply
 * command-line options within the {@link #process(ListIterator)}
 * method. They also provide descriptions of implemented commands
 * or options via {@link #getSummary()} method and list the
 * acceptable arguments via {@link #getArgSpec()} method.
 * These classes don't have to contain full names or shortcuts to
 * the commands they implement, since command names and switches
 * are encoded within the Spring configuration. Each class that
 * implements a command should be declared as a bean in 
 * {@link Launcher#MAIN_BEAN_CONFIG_FILE the framework's configuration}
 * and should have either a name or alias prefixed with
 * {@link Launcher#BEAN_NAME_PREFIX_COMMAND} if it can be entered
 * using the {@link #COMMAND_PREFIX long name} (command) or
 * {@link Launcher#BEAN_NAME_PREFIX_SWITCH} if it can be entered
 * using the {@link #SWITCH_PREFIX short name} (switch). Most commands
 * require an {@link ApplicationBean} to operate on, so they should
 * have it injected using the same configuration file(s). It is not
 * recommended to inject direct references to
 * {@link Launcher#BEAN_NAME_MAIN the main bean}, though, since
 * that bean may be different in dependent projects. 
 */
public abstract class Command extends Logging
{
 /**
  * Processes the command with arguments from the command line.
  * This method must consume the arguments pertaining to the
  * command, if any, from the supplied iterator. 
  * @param args the iterator positioned at the first argument
  * to this command, if any
  * @throws RuntimeException if there is an error within the
  * arguments supplied
  * @throws ApplicationBeanException if there is a problem performing an
  * immediate action requested by the command
  */
 public abstract void process(ListIterator<String> args)
 	throws ApplicationBeanException;

 /**
  * Implementors should return a string that lists the command's
  * arguments in the proper order and informs the user which
  * arguments are optional, which ones are repeatable, and how
  * many times they can be repeated. Typically, the return value
  * string of a loose BNF form, no more than 140 characters long.
  * @return a string with arguments' info, or <code>null</code>
  * if this command accepts no arguments 
  */
 public abstract String getArgSpec();

 /**
  * Implementors should return a brief description of what the 
  * command does, when it is used, and what arguments it takes.
  * This string will be displayed to the user on of the help
  * screen. The recommended length of returned text is 60 - 600
  * characters.
  */
 public abstract String getSummary();

 /**
  * Defines a command that may be given to an application via
  * command line.
  * @param applicationBean the application class addressed by
  * this command
  */
 public Command(ApplicationBean applicationBean)
 {
  appBean = applicationBean;
 }

 /**
  * Prefix of a long option (command) on the command line.
  */
 public static final String COMMAND_PREFIX = "--";

 /**
  * Prefix of a short option (switch) on the command line.
  */
 public static final String SWITCH_PREFIX = "-";

 /**
  * Returns the application class configured by
  * this command.
  * @see #Command(ApplicationBean)
  */
 protected ApplicationBean getApplicationBean()
 {
  return appBean;
 }

 /**
  * Attempts to obtain next argument for this command from the
  * command line. The supplied iterator is advanced if a candidate
  * argument is available, otherwise it is left as-is. 
  * @param args iterator over the command line
  * @return next argument offered or <code>null</code> if there
  * is none
  * @see #process(ListIterator)
  */
 protected static String fetchArgument(ListIterator<String> args)
 {
  if (!args.hasNext())
   return null;
  String arg = args.next();
  if (arg.startsWith(SWITCH_PREFIX) || arg.startsWith(COMMAND_PREFIX))
  {
   args.previous();
   arg = null;
  }
  return arg;
 }
 
 private final ApplicationBean appBean;
}