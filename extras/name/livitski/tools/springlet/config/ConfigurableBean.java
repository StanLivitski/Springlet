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
package name.livitski.tools.springlet.config;

import java.io.File;

import name.livitski.tools.proper2.AbstractSetting;
import name.livitski.tools.proper2.Configuration;
import name.livitski.tools.proper2.ConfigurationException;
import name.livitski.tools.springlet.Logging;

/**
 * A superclass for simple beans that provides logging and access to
 * {@link Configuration proper2-based} configuration. Inject a 
 * {@link ConfigurationHelper} to configure derived beans from
 * the command line.
 * @see #setConfiguration(Configuration)
 */
public class ConfigurableBean extends Logging
{
 public Configuration getConfiguration()
 {
  if (null == config)
   throw new IllegalStateException("Web driver factory is not configured using setConfiguration()");
  return config;
 }

 public void setConfiguration(Configuration config)
 {
  this.config = config;
 }

 public File getConfigFile()
 {
  return getConfiguration().getConfigFile();
 }

 /**
  * Programmatic override for the configuration file location.
  * @param configFile the configuration file with settings for this object
  * @see #getConfigFile()
  */
 public void setConfigFile(File configFile)
 {
  getConfiguration().setConfigFile(configFile);
 }

 protected <T, D extends AbstractSetting<D,T>> T readSetting(Class<D> clazz)
 	throws ConfigurationException
 {
  return getConfiguration().readSetting(clazz);
 }

 protected <T, D extends AbstractSetting<D,T>> T readSetting(AbstractSetting<D,T> handler)
 	throws ConfigurationException
 {
  return getConfiguration().readSetting(handler);
 }

 private Configuration config;
}