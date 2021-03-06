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

import name.livitski.tools.springlet.AbstractApplicationBean;

public class Hello extends AbstractApplicationBean<Hello.Status>
{
 public Hello()
 {
  super(Status.OK);
 }

 @Override
 public void run()
 {
  log().info("Hello, world!");
 }

 @Override
 public Status getLocalStatus()
 {
  return Status.OK;
 }

 public enum Status
 {
  OK
 }
}
