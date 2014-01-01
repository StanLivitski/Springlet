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

/**
 * Convenience class that implements functionality common to
 * Springlet application beans. This includes poolable enumerated
 * {@link #getLocalStatus() status codes}, dependency management,
 * and {@link Logging}.
 * @param <Status> enumerated type that lists
 * {@link #getLocalStatus() local status values} this application
 * bean may return
 */
public abstract class AbstractApplicationBean<Status extends Enum<Status>> extends Logging
	implements ApplicationBean, WithPoolableStatusCode
{
 @Override
 public abstract void run();

 /**
  * Signals the last error condition this application bean has 
  * encountered, <em>except for errors that might have been reported
  * by its dependency application beans</em>.
  * @return status of this application bean
  */
 public abstract Status getLocalStatus();

 /**
  * Override to define dependencies for this application. Each
  * invocation of this method may return a new array, but the
  * contents of returned arrays must be the same once this object
  * is initialized. Default implementation returns an empty array.
  */
 public ApplicationBean[] getDependencies()
 {
  return new ApplicationBean[0];
 }

 /**
  * Converts a status constant returned by {@link #getLocalStatus()}
  * of this bean or any {@link #getDependencies() dependency bean} into
  * status code returned to the framework. First, this bean's
  * {@link #getLocalStatus() local status code} is checked. If it is
  * non-null and has a non-zero ordinal value, that status code is
  * returned. Otherwise, the status
  * code base is set to {@link #getMaxLocalStatusCode()}, and the
  * {@link #getDependencies() dependency beans} are polled for their
  * {@link ApplicationBean#getStatusCode() status codes} in the order
  * those beans are listed. Any dependency with a zero status increments
  * the base by its {@link WithPoolableStatusCode#getMaxLocalStatusCode()}
  * if it implements {@link WithPoolableStatusCode}, otherwise the base is
  * incremented by one for that bean. If there is a dependency with a
  * non-zero status and it implements {@link WithPoolableStatusCode},
  * the {@link #getStatusCode()} returned by that dependency is added
  * to the status code base and returned. If the dependency with a
  * non-zero status does not implement {@link WithPoolableStatusCode},
  * the status code base incremented by one is returned. If all
  * dependencies have zero status codes, zero is returned.
  * @return status code computed as explained above
  */
 @Override
 public int getStatusCode()
 {
  final Status local = getLocalStatus();
  if (null != local && 0 < local.ordinal())
   return local.ordinal();
  int base = getMaxLocalStatusCode();
  final ApplicationBean[] deps = getDependencies();
  for (int i = 0; deps.length > i; i++)
  {
   final int code = deps[i].getStatusCode();
   assert 0 <= code;
   if (0 == code)
    base += getMaxStatusCodeFor(deps, i);
   else if (deps[i] instanceof WithPoolableStatusCode)
    return base + code;
   else
    return base + 1;
  }
  return 0;
 }

 /**
  * Override to implement exception to status code mappings.
  * Default implementation throws an {@link UnsupportedOperationException}.
  */
 @Override
 public void updateStatus(ApplicationBeanException ex)
 {
  throw new UnsupportedOperationException("Application "
    + this + " does not implement exception to exit code mapping.");
 }

 /**
  * Returns the number of enumerated status code values defined for
  * this bean and its dependencies.
  * @see #getStatusCode()
  */
 public int getMaxStatusCode()
 {
  int sum = getMaxLocalStatusCode();
  final ApplicationBean[] deps = getDependencies();
  for (int i = 0; deps.length > i; i++)
   sum += getMaxStatusCodeFor(deps, i);
  return sum;
 }

 /**
  * Returns the number of enumerated status code values defined for
  * this bean only.
  * @see #getLocalStatus()
  */
 @Override
 public int getMaxLocalStatusCode()
 {
  return stata.length;
 }

 /**
  * Initializes the bean with its status values enumeration.
  * Note that implementing beans must have a no-args constructor. 
  * @param okStatus the {@link #getLocalStatus() status value} that indicates
  * success (normal execution) of this application bean. This value
  * <strong>must have</strong> the {@link Enum#ordinal() ordinal number}
  * of zero.
  */
 @SuppressWarnings("unchecked")
 protected AbstractApplicationBean(Enum<Status> okStatus)
 {
  final Class<Status> enumClass = okStatus.getDeclaringClass();
  assert 0 == okStatus.ordinal();
  try
  {
   this.stata = (Status[])(enumClass.getMethod("values").invoke(null));
  }
  catch (Exception e)
  {
   throw new IllegalArgumentException(
     "Cannot extract application status values from " + okStatus, e);
  }
 }

 private int getMaxStatusCodeFor(final ApplicationBean[] deps, final int i)
 {
  if (deps[i] instanceof WithPoolableStatusCode)
   return ((WithPoolableStatusCode)deps[i]).getMaxLocalStatusCode();
  else
   return 1;
 }

 private Status[] stata; 
}
