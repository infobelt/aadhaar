Aadhaar
=======

This is a collection of useful utilities that Infobelt leverages when building Spring Boot/Angular based applications as
part of their suite of technologies.

The tools can be used to simplify building applications and provide some out-of-the-box ways to handle standard REST APIs.

In the following sections, we walk through how some of the components can be used in your application.

Installation
============

In your Spring Boot application add a dependency on:

        <dependency>
            <groupId>com.infobelt.aadhaar</groupId>
            <artifactId>aadhaar-spring-boot</artifactId>
            <version>0.1.0-SNAPSHOT</version>
        </dependency>


Core Concepts
=============

AbstractEntity
--------------

At the heart of our model of building RESTful API's and Service layers is the concept of an AbstractEntity.

The AbstractEntity allows us to encapsulate a few common patterns for how to build a database object and then leverage
it in Spring Boot.  It ensures that all tables have an ID, UUID and then created/modified timestamps.





License
=======

See [LICENSE.md]
