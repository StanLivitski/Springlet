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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * A {@link Launcher} for use in managed environments, such as unit
 * tests, that require access to the application's
 * {@link #getBeanFactory() bean container}. This class implements methods
 * that delegate to the bean container. It does not set up any logging
 * for the application, unless requested to by calling the 
 * {@link #withDefaultLogging()} method.
 */
public class ManagedLauncher extends Launcher implements BeanFactory
{
 public ManagedLauncher()
 {
  isLoggingConfigured = true; 
 }

 public ManagedLauncher withDefaultLogging()
 {
  configureJDKLogging();
  return this; 
 }

 @Override
 public boolean containsBean(String name)
 {
  return getBeanFactory().containsBean(name);
 }

 @Override
 public String[] getAliases(String name)
 {
  return getBeanFactory().getAliases(name);
 }

 @Override
 public <T> T getBean(Class<T> requiredType) throws BeansException
 {
  return getBeanFactory().getBean(requiredType);
 }

 @Override
 public <T> T getBean(String name, Class<T> requiredType) throws BeansException
 {
  return getBeanFactory().getBean(name, requiredType);
 }

 @Override
 public Object getBean(String name, Object... args) throws BeansException
 {
  return getBeanFactory().getBean(name, args);
 }

 @Override
 public Object getBean(String name) throws BeansException
 {
  return getBeanFactory().getBean(name);
 }

 @Override
 public Class<?> getType(String name) throws NoSuchBeanDefinitionException
 {
  return getBeanFactory().getType(name);
 }

 @Override
 public boolean isPrototype(String name) throws NoSuchBeanDefinitionException
 {
  return getBeanFactory().isPrototype(name);
 }

 @Override
 public boolean isSingleton(String name) throws NoSuchBeanDefinitionException
 {
  return getBeanFactory().isSingleton(name);
 }

 @Override
 public boolean isTypeMatch(String name, Class<?> targetType)
   throws NoSuchBeanDefinitionException
 {
  return getBeanFactory().isTypeMatch(name, targetType);
 }

}
