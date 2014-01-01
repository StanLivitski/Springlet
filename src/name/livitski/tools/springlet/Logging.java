/**
 *    Copyright © 2010, 2013 Konstantin "Stan" Livitski
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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base for classes that do their own logging.
 * Uses the Apache Commons logging framework over JUL.
 */
public abstract class Logging
{
 /**
  * Reads the JDK logging configuration from an input stream.
  * @param configDocumentStream the stream containing logging
  * configuration properties
  * @throws IOException
  */
 public static void configureJDKLogs(InputStream configDocumentStream)
 	throws IOException
 {
  LogManager.getLogManager().readConfiguration(configDocumentStream);
 }

 /**
  * Changes the log level applied to a subsystem by the JDK logging.
  * The new level will apply to the subsystem itself and to its
  * descendants that haven't been assigned severity levels explicitly.
  * @param level the lowest severity level of messages that will
  * be logged for selected subsystem(s) from now on
  * @param subsystem name of the subsystem that the new logging level
  * applies to, usually the name of a Java package or class, use
  * <code>null</code> to obtain the root logger from the JUL hierarchy
  * @see Logger#setLevel(Level)
  * @see LogManager
  */
 public static void setJDKLogLevel(Level level, String subsystem)
 {
  final String key = null == subsystem ? "" : subsystem;
  Logger logger = jdkLoggers.get(key);
  if (null == logger)
  {
   logger = Logger.getLogger(key);
   jdkLoggers.put(key, logger);
  }
  logger.setLevel(level);
 }

 public static Log logForClass(Class<?> clazz)
 {
  return LogFactory.getLog(clazz);
 }

 /**
  * Provides the logger to the implementing class.
  */
 protected Log log()
 {
  return logger;
 }

 protected Logging()
 {
  logger = logForClass(getClass());
 }

 private Log logger;
 private static Map<String,Logger> jdkLoggers = new HashMap<String, Logger>();
}
