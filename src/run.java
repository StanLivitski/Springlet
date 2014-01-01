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

import name.livitski.tools.springlet.Launcher;

/**
 * Starts a Springlet application from the command line.
 * This is the special class that has only one method: {@link #main}.
 * It delegates the launch to Springlet framework.
 * @see Launcher
 */
public class run
{
 /**
  * Creates an instance of the Springlet framework and delegates the
  * application's launch to it.
  * @see Launcher
  */
 public static void main(String[] args)
 {
  Launcher launcher = new Launcher().withArguments(args);
  int status = launcher.getStatusCode();
  if (0 == status)
  {
   launcher.run();
   status = launcher.getStatusCode();
  }
  if (0 != status)
   System.exit(status);
 }
}
