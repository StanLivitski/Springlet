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
 * A superclass for exceptions that report errors within
 * an {@link ApplicationBean} that change that bean's
 * {@link ApplicationBean#getStatusCode() status}. Originator
 * beans should catch these exceptions within their
 * {@link Runnable#run()} methods and call
 * {@link #updateBeanStatus()} to update their status codes.
 * When the framework catches this exception during the
 * configuration phase, it calls {@link #updateBeanStatus()}
 * automatically.
 */
@SuppressWarnings("serial")
public abstract class ApplicationBeanException extends Exception
{
 /**
  * @return may be <code>null</code> when running outside of a
  * Springlet application 
  */
 public ApplicationBean getApplicationBean()
 {
  return source;
 }

 /**
  * Call this method when catching this exception to update the
  * source bean's status.
  * @see ApplicationBean#getStatusCode()
  */
 public void updateBeanStatus()
 {
  if (null != source)
   source.updateStatus(this);
 }

 /**
  * @param source may be <code>null</code> when running outside of an
  * application 
  */
 public ApplicationBeanException(ApplicationBean source, String message)
 {
  super(message);
  this.source = source;
 }

 /**
  * @param source may be <code>null</code> when running outside of an
  * application 
  */
 public ApplicationBeanException(ApplicationBean source, Throwable cause)
 {
  super(cause);
  this.source = source;
 }

 /**
  * @param source may be <code>null</code> when running outside of an
  * application 
  */
 public ApplicationBeanException(ApplicationBean source, String message, Throwable cause)
 {
  super(message, cause);
  this.source = source;
 }

 private final ApplicationBean source;
}
