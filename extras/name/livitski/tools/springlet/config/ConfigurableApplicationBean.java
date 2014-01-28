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

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import name.livitski.tools.proper2.AbstractSetting;
import name.livitski.tools.proper2.Configuration;
import name.livitski.tools.proper2.ConfigurationException;
import name.livitski.tools.springlet.AbstractApplicationBean;

/**
 * An application bean that manages its own {@link Configuration proper2-based}
 * configuration.
 * @see ConfigurationHelper
 */
public abstract class ConfigurableApplicationBean<Status extends Enum<Status>>
	extends AbstractApplicationBean<Status> implements BeanFactoryAware
{
 public Configuration getConfig()
 {
  if (null == config)
  {
   config = new ConfigurationHelper(this);
   ((ConfigurationHelper)config).setBeanFactory(beanFactory);
  }
  return config;
 }

 public void setConfig(Configuration config)
 {
  this.config = config;
 }

 /**
  * Returns the configuration file location. 
  * @return the configFile or <code>null</code> if that
  * file is not specified 
  */
 public File getConfigFile()
 {
  final Configuration config = getConfig();
  File configFile = config.getConfigFile();
  return configFile;
 }

 /**
  * Programmatic override for the configuration file location.
  * @param configFile the configuration file with settings for this object
  * @see #getConfigFile()
  */
 public void setConfigFile(File configFile)
 {
  final Configuration config = getConfig();
  config.setConfigFile(configFile);
 }

 public String getDefaultsResource()
 {
  return getConfig().getDefaultsResource();
 }

 public void setDefaultsResource(String defaultsResource)
 {
  getConfig().setDefaultsResource(defaultsResource);
 }

 @Override
 public void setBeanFactory(BeanFactory beanFactory)
 {
  this.beanFactory = beanFactory;
  if (config instanceof ConfigurationHelper)
   ((ConfigurationHelper)config).setBeanFactory(beanFactory);
 }

 protected <T, D extends AbstractSetting<D,T>> T readSetting(Class<D> clazz)
 	throws ConfigurationException
 {
  return getConfig().readSetting(clazz);
 }

 /**
  * Default constructor from the superclass.
  * @see AbstractApplicationBean
  */
 protected ConfigurableApplicationBean(Enum<Status> okStatus)
 {
  super(okStatus);
 }

 private BeanFactory beanFactory;
 private Configuration config;
}
