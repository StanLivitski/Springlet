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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

import name.livitski.tools.springlet.ApplicationBean;
import name.livitski.tools.springlet.Command;
import name.livitski.tools.springlet.Launcher;
import name.livitski.tools.springlet.SkipApplicationRunRequest;

/**
 * Handles the <code>--help</code> command line switch.
 * Displays the {@link #usage() application's usage summary} and
 * {@link SkipApplicationRunRequest requests the framework to quit}.
 */
public class HelpCommand extends Command implements BeanFactoryAware
{
 /**
  * @param manager the data manager that may receive this
  * command
  */
 public HelpCommand(ApplicationBean manager)
 {
  super(manager);
 }

 @Override
 public void process(ListIterator<String> args)
 {
  usage();
  throw new SkipApplicationRunRequest();
 }

 /**
  * Displays the application's usage summary obtained by polling all
  * {@link Command command beans} defined in its configuration.
  */
 public void usage()
 {
  System.out.println("Usage summary:");
  System.out.printf(" java -cp {classpath} [vm-args] run [args]%n%n");
  System.out.println(" where: args := arg [args] ;");
  System.out.println("  arg is one of the following:");
  Map<String, Command> commands = beanFactory.getBeansOfType(Command.class);
  StringBuilder tagSpec = new StringBuilder(512);
  List<String> cmdsTags = new ArrayList<String>();
  for (final Map.Entry<String, Command> entry : commands.entrySet())
  {
   final Command cmd = entry.getValue();
   final String id = entry.getKey();
   tagSpec.setLength(0);
   cmdsTags.clear();
   cmdsTags.add(id);
   final String[] aliases = beanFactory.getAliases(id);
   for (String alias : aliases)
    cmdsTags.add(alias);
   for (String tag : cmdsTags)
   {
    if (tag.startsWith(Launcher.BEAN_NAME_PREFIX_COMMAND))
     tag = COMMAND_PREFIX + tag.substring(Launcher.BEAN_NAME_PREFIX_COMMAND.length());
    else if (tag.startsWith(Launcher.BEAN_NAME_PREFIX_SWITCH))
     tag = SWITCH_PREFIX + tag.substring(Launcher.BEAN_NAME_PREFIX_SWITCH.length());
    if (0 != tagSpec.length())
     tagSpec.append(", ");
    tagSpec.append(tag);
   }
   String argspec = cmd.getArgSpec();
   System.out.printf(
     "%n %s%s%n",
     tagSpec,
     null == argspec ? "" : " " + argspec
     );
   System.out.printf(
     "%n   %s%n",
     cmd.getSummary()
     );
  }
  System.out.println();
 }

 @Override
 public String getArgSpec()
 {
  return null;
 }

 @Override
 public String getSummary()
 {
  return "Displays this help screen and exits.";
 }

 @Override
 public void setBeanFactory(BeanFactory beanFactory) throws BeansException
 {
  if (!(beanFactory instanceof ListableBeanFactory))
   throw new UnsupportedOperationException("Beans of " + getClass()
     + " require a ListableBeanFactory container");
  this.beanFactory = (ListableBeanFactory)beanFactory;
 }

 private ListableBeanFactory beanFactory;
}
