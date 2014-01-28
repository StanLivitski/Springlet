<!--
 |    Copyright © 2013, 2014 Konstantin "Stan" Livitski
 | 
 |    This file is part of Springlet. Springlet is
 |    licensed under the Apache License, Version 2.0 (the "License");
 |    you may not use this file except in compliance with the License.
 |    You may obtain a copy of the License at
 | 
 |      http://www.apache.org/licenses/LICENSE-2.0
 | 
 |    Unless required by applicable law or agreed to in writing, software
 |    distributed under the License is distributed on an "AS IS" BASIS,
 |    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 |    See the License for the specific language governing permissions and
 |    limitations under the License.
 -->

<a name="sec-about"> </a>
What is Springlet?
==================

Springlet is a lightweight [Spring][]-based framework for building command-line
Java applications. Its facilities address basic concerns of a command-line
application, including:

 - Parsing command-line arguments and triggering associated actions
 - Launching the application bean in an [IoC][] container
 - Managing dependency application beans in a composite application
 - Tracking termination status and generating an exit code for a calling script
 - Organizing and configuring the application's logs
 - Displaying the summary of available command-line arguments to the user
 - Simulating the command-line environment during the application testing

<a name="sec-depends"> </a>
Project's dependencies
======================

Springlet uses the libraries listed below. You must have those libraries
on the classpath both when compiling Springlet and using it.

<table width="70%" border="1" cellpadding="0" cellspacing="0">
<col />
<col width="20%" />
<col width="20%" />
<col width="20%" />
<thead>
<tr>
<th>Library or framework</th>
<th>Importance</th>
<th>Module</th>
<th>Tested with version</th>
</tr>
</thead>
<tbody align="center" valign="middle">
<tr>
<td><a href="http://commons.apache.org/proper/commons-logging/download_logging.cgi">
Apache Commons Logging</a></td>
<td>required</td>
<td>-</td>
<td>1.1.1</td>
</tr>
<tr>
<td><a href="http://projects.spring.io/spring-framework/#quick-start">
Spring Framework</a></td>
<td>required</td>
<td>spring-core</td>
<td>3.2.4.RELEASE</td>
</tr>
<tr>
<td><a href="http://projects.spring.io/spring-framework/#quick-start">
Spring Framework</a></td>
<td>required</td>
<td>spring-beans</td>
<td>3.2.4.RELEASE</td>
</tr>
<tr>
<td><a href="https://github.com/StanLivitski/proper2">
proper2</a></td>
<td>optional</td>
<td>-</td>
<td>current stable version</td>
</tr>
</tbody>
</table>

<a name="sec-download"> </a>
Downloading the binary and Javadoc
==================================

The binary and compressed Javadoc pages of the Springlet framework will be available at:

 - <https://github.com/StanLivitski/Springlet/wiki/Download>

<a name="sec-use"> </a>
Using Springlet
=============

The simplest example of a Springlet application is located in the `mock/`
directory of this repository.
For detailed Springlet API information, please consult the project's [javadoc][].

<a name="sec-beans"> </a>
The application bean
--------------------

_This documentation section has yet to be written. If you would like to help writing it,
please [contact the project's team](#sec-contact)_

<a name="sec-args"> </a>
Command line arguments
----------------------

_This documentation section has yet to be written. If you would like to help writing it,
please [contact the project's team](#sec-contact)_

<a name="sec-logging"> </a>
Logging
-------

_This documentation section has yet to be written. If you would like to help writing it,
please [contact the project's team](#sec-contact)_

<a name="sec-composite"> </a>
Composite applications
----------------------

_This documentation section has yet to be written. If you would like to help writing it,
please [contact the project's team](#sec-contact)_

<a name="sec-repo"> </a>
About this repository
=====================

This repository contains the source code of Springlet. Its top-level components are:

        src/           		Springlet's source files
        mock/           	An example "Hello, world!" application built using the
        						Springlet framework
        lib/				an empty directory for placing links or copies of
        					 dependency libraries
        LICENSE		        Document that describes the project's licensing terms
        NOTICE   	        A summary of license terms that apply to Springlet
        build.xml      		Configuration file for the tool (Ant) that builds
                       		 the binary and Javadoc
        README.md			This document

<a name="sec-building"> </a>
Building Springlet
================

To build the framework's binary from this repository, you need:

   - A **Java SDK**, also known as JDK, Standard Edition (SE), version 6 or
   later, available from OpenJDK <http://openjdk.java.net/> or Oracle
   <http://www.oracle.com/technetwork/java/javase/downloads/index.html>.

   Even though a Java runtime may already be installed on your machine
   (check that by running `java --version`), the build will fail if you
   don't have a complete JDK (check that by running `javac`).

   - **Apache Ant** version 1.7.1 or newer, available from the Apache Software
   Foundation <http://ant.apache.org/>.

   - [Dependency libraries](#sec-depends), or links thereto in the `lib/`
   subdirectory of your working copy.

To build the core of Springlet, go to the directory containing a working copy of
Springlet and run:

     ant

The result is a file named `springlet.jar` in the same directory.

<a name="sec-javadoc"> </a>
Building Javadoc
================

To build Javadoc for the project, make sure you have met the prerequisites
[listed above](#sec-building), go to the directory containing a working copy of
project's sources and run:

     ant javadoc

The result will be placed in the `javadoc` subdirectory. 

<a name="sec-contact"> </a>
Contacting the project's team
=============================

You can send a message to the project's team via the
[Contact page](http://www.livitski.com/contact) at <http://www.livitski.com/>
or via *GitHub*. We will be glad to hear from you!

   [Spring]: http://projects.spring.io/spring-framework/
   [IoC]: https://en.wikipedia.org/wiki/Inversion_of_control
   [javadoc]: #sec-download
   