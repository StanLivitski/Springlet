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
 * Defines operations required from a bean that implements a command-line application.
 * Application's main bean must implement the {@link Runnable#run()} operation
 * that SHOULD NOT throw any exceptions. It must also implement an accessor method
 * for the application's {@link #getStatusCode() status code}. 
 * In addition, implementing beans must have a no-args constructor. 
 */
public interface ApplicationBean extends Runnable
{
 /**
  * Returns the current status code of this application.
  * The status is used to determine to the application's
  * {@link System#exit(int) exit code}. A zero status
  * means that the application is proceeding or has completed
  * without error. The framework obtains an exit code by adding
  * the number of {@link Launcher#RESERVED_EXITCODE_MAX reserved codes}
  * to any positive status code it receives. 
  * Any non-zero status obtained after initialization shall prevent
  * the framework from {@link Runnable#run() running} the application.
  * Note that the operating system may truncate the exit code
  * returned by the application, e.g. to the lower 8 bits on Unix.
  * @return an exit code to be returned to the
  * system, zero on success, cannot be negative
  */
 public int getStatusCode();

 /**
  * Updates the status of this bean using information reported
  * via an exception thrown while configuring or running this bean.
  * @throws UnsupportedOperationException if no mapping
  * is implemented for this bean. This is caught by the
  * framework, but does not have to be caught by
  * {@link #run()} implementations.
  */
 public void updateStatus(ApplicationBeanException ex);
}
