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

import java.io.InputStream;
import java.util.Arrays;
import java.util.ListIterator;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * Instantiates and runs the application bean as configured.
 * The framework reads the application's configuration from
 * {@link #MAIN_BEAN_CONFIG_FILE}. First, it tries to
 * {@link #withArguments(String[]) parse the command line}.
 * After successful parsing, the framework looks for the
 * {@link #BEAN_NAME_MAIN main application bean} there and tries
 * to {@link #run() launch it}.
 */
public class Launcher extends Logging
{
 /**
  * The name of a file at the root of the project's classpath
  * that configures the main bean to be launched.
  */
 public static final String MAIN_BEAN_CONFIG_FILE = "springlet.xml";

 /**
  * Exit code <code>1</code> indicates the application's linkage
  * problem or an unhandled exception.
  */
 public static final int STATUS_INTERNAL_ERROR = 1;

 /**
  * Exit code <code>2</code> indicates a problem with the command
  * line syntax or invalid argument(s).
  */
 public static final int STATUS_COMMAND_PARSING_FAILURE = 2;

 /**
  * Exit code <code>3</code> indicates that application run has
  * been skipped, e.g. to display the usage information.
  */
 public static final int STATUS_RUN_SKIPPED = 3;

 /**
  * The number of exit codes reserved by the framework.  
  */
 public static final int RESERVED_EXITCODE_MAX = STATUS_RUN_SKIPPED;

 /**
  * Bean name prefix for long commands.  
  */
 public static final String BEAN_NAME_PREFIX_COMMAND = "command-"; 

 /**
  * Bean name prefix for short commands (switches).  
  */
 public static final String BEAN_NAME_PREFIX_SWITCH = "switch-"; 

 /**
  * Bean name of the default handler for unclaimed arguments.
  * The default handler, if any, must be an instance of {@link Command}
  * that will receive an iterator over unrecognized argument(s).
  */
 public static final String BEAN_NAME_DEFAULT_HANDLER = "handler-default"; 

 /**
  * Name of the main {@link ApplicationBean application bean} to launch.
  */
 public static final String BEAN_NAME_MAIN = "main";

 /**
  * The name of a file at the root of the project's classpath
  * that configures the java.util.logging facility.
  */
 public static final String LOG_PROPERTIES_FILE = "jdk-log.default.properties";

 /**
  * Configures the manager using command-line arguments.
  * The arguments are checked in their command line sequence.
  * If an argument begins with {@link Command#COMMAND_PREFIX},
  * its part that follows it is treated as a (long) command's name.
  * If an argument begins with {@link Command#SWITCH_PREFIX},
  * the following character(s) are treated as a switch.
  * The name or switch extracted this way, prepended with
  * a bean name prefix for a {@link #BEAN_NAME_PREFIX_COMMAND command}
  * or a {@link #BEAN_NAME_PREFIX_SWITCH switch}, becomes the name
  * of a bean to look up in the framework's
  * {@link #MAIN_BEAN_CONFIG_FILE configuration file(s)}.
  * The bean that handles a command must extend the
  * {@link Command} class. Once a suitable bean is found,
  * it is called to act upon the command or switch and process
  * any additional arguments associated with it. 
  * If no bean can be found or the argument is not prefixed
  * properly, it is considered unclaimed. You may configure a
  * special subclass of {@link Command} to process such arguments
  * by placing a bean named {@link #BEAN_NAME_DEFAULT_HANDLER} on
  * the configuration. If there is no such bean configured, or when
  * any {@link Command} bean throws an exception while processing
  * a command, a command line error is reported and the application
  * quits.
  * @param args the command line
  * @return this manager object
  */
 public Launcher withArguments(String[] args)
 {
  configureDefaultLogging();
  final Log log = log();
  ListIterator<String> iargs = Arrays.asList(args).listIterator();
  while (iargs.hasNext())
  {
   String arg = iargs.next();
   String prefix = null;
   String beanPrefix = null;
   Command cmd = null;
   if (arg.startsWith(Command.COMMAND_PREFIX))
    prefix = Command.COMMAND_PREFIX;
   else if (arg.startsWith(Command.SWITCH_PREFIX))
    prefix = Command.SWITCH_PREFIX;
   if (null != prefix)
   {
    arg = arg.substring(prefix.length());
    beanPrefix = Command.SWITCH_PREFIX == prefix ?
     BEAN_NAME_PREFIX_SWITCH : BEAN_NAME_PREFIX_COMMAND;
    try
    {
     cmd = getBeanFactory().getBean(beanPrefix + arg, Command.class);
    }
    catch (NoSuchBeanDefinitionException noBean)
    {
     log.debug(
       "Could not find a handler for command-line argument " + prefix + arg,
       noBean);
    }
   }
   if (null == cmd)
   {
    iargs.previous();
    try
    {
     cmd = getBeanFactory().getBean(BEAN_NAME_DEFAULT_HANDLER, Command.class);
    }
    catch (RuntimeException ex)
    {
     log.error("Unknown command line argument: "
       + (null != prefix ? prefix : "") + arg, ex);
     status = STATUS_COMMAND_PARSING_FAILURE;
     break;
    }
   }
   try
   {
    cmd.process(iargs);
   }
   catch (SkipApplicationRunRequest skip)
   {
    if (log.isTraceEnabled())
     log.trace(
       "Handler for argument " + (null != prefix ? prefix : "") + arg
       + " requested to skip the application run", skip);
    status = STATUS_RUN_SKIPPED;
    break;
   }
   catch (RuntimeException err)
   {
    log.error("Invalid command line argument(s) near "
      + (null != prefix ? prefix : "") + arg
      + ": " + err.getMessage(), err);
    status = STATUS_COMMAND_PARSING_FAILURE;
    break;
   }
   catch (ApplicationBeanException err)
   {
    log.error("Error processing command line argument "
      + (null != prefix ? prefix : "") + arg
      + ": " + err.getMessage(), err);
    try
    {
     err.updateBeanStatus();
    }
    catch (RuntimeException noStatus)
    {
     final ApplicationBean appBean = err.getApplicationBean();
     log.warn("Could not obtain status code" +
     	(null != appBean ? " from " + appBean : ""), noStatus);
     status = STATUS_INTERNAL_ERROR;
    }
    break;
   }
  }
  return this;
 }
 
 public void run()
 {
  configureDefaultLogging();
  try
  {
   final ApplicationBean applicationBean = getApplicationBean();
   if (null == applicationBean)
    status = STATUS_INTERNAL_ERROR;
   else
    applicationBean.run();
  }
  catch(RuntimeException error)
  {
   log().error("Unhandled exception: " + error.getMessage(), error);
   status = STATUS_INTERNAL_ERROR;
  }
 }

 public int getStatusCode()
 {
  if (0 == status)
  {
   final ApplicationBean applicationBean = getApplicationBean();
   if (null == applicationBean)
    status = STATUS_INTERNAL_ERROR;
   else
   {
    status = applicationBean.getStatusCode();
    if (0 < status)
     status += RESERVED_EXITCODE_MAX;
    else if (0 > status)
    {
     log().error("Application bean " + applicationBean.getClass()
       + " returned invalid status: " + status);
     status = STATUS_INTERNAL_ERROR;
    }
   }
  }
  return status;
 }

 public Launcher()
 {
 }

 protected ApplicationBean getApplicationBean()
 {
  if (null == appBean)
   try
   {
    appBean = getBeanFactory().getBean(BEAN_NAME_MAIN, ApplicationBean.class);
   }
   catch (RuntimeException error)
   {
    log().fatal("Error initializing the application", error);
   }
  return appBean;
 }

 protected BeanFactory getBeanFactory()
 {
  if (null == beanFactory)
  {
   beanFactory = new DefaultListableBeanFactory();
   new XmlBeanDefinitionReader(beanFactory).loadBeanDefinitions(MAIN_BEAN_CONFIG_FILE);
  }
  return beanFactory;
 }

 protected void configureJDKLogging()
 {
  InputStream cfg;
  if (null == System.getProperty("java.util.logging.config.class")
   && null == System.getProperty("java.util.logging.config.file") 
   && null != (cfg = getClass().getResourceAsStream('/' + LOG_PROPERTIES_FILE))
  )
  {
   try
   {
    configureJDKLogs(cfg);
   }
   catch (Exception e)
   {
    System.err.println(
      "WARNING: could not configure JDK logging. Detailed diagnostics may not be available. "
      + e);
   }
   finally
   {
    try
    {
     cfg.close();
     cfg = null;
    }
    catch (Exception ignored) {}
   }
  }
 }

 /**
  * A check mark to tell this object that it has configured logging
  * for the application or that it should not configure any logging.
  * @see #configureJDKLogging() 
  */
 protected static boolean isLoggingConfigured;

 /**
  * Configures the default logging for this application if necessary.
  */
 private void configureDefaultLogging()
 {
  if (!isLoggingConfigured)
  {
   configureJDKLogging();
   isLoggingConfigured = true;
  }
 }

 private int status;
 private ApplicationBean appBean;
 private DefaultListableBeanFactory beanFactory;
}
