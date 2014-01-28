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
import java.util.Properties;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import name.livitski.tools.proper2.Configuration;
import name.livitski.tools.proper2.ConfigurationException;
import name.livitski.tools.springlet.Launcher;
import name.livitski.tools.springlet.commands.ConfigCommand;

/**
 * A wrapper around {@link Configuration proper2 configuration}
 * that reads settings from file referenced by {@link ConfigCommand}.
 * This object must be instantiated inside the
 * {@link Launcher#MAIN_BEAN_CONFIG_FILE Springlet container}
 * that contains the {@link ConfigCommand} object.
 */
public class ConfigurationHelper extends Configuration
	implements BeanFactoryAware
{
 @Override
 public File getConfigFile()
 {
  File configFile = super.getConfigFile();
  if (null == configFile)
  {
   ConfigCommand cmd = beanFactory.getBean(ConfigCommand.class);
   configFile = cmd.getFile();
   super.setConfigFile(configFile);
  }
  return configFile;
 }

 @Override
 protected Properties readConfigurationFromFile() throws ConfigurationException
 {
  getConfigFile();
  return super.readConfigurationFromFile();
 }

 @Override
 public void setBeanFactory(BeanFactory beanFactory)
 {
  this.beanFactory = beanFactory;
 }

 /**
  * @param host the object being configured
  */
 public ConfigurationHelper(Object host)
 {
  super(host);
 }

 private BeanFactory beanFactory;
}
