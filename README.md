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

Therefore it is best to leverage Liquibase to create the entity as so:
    
```YAML     
- createTable:
    columns:
    - column:
        autoIncrement: true
        constraints:
          nullable: false
          primaryKey: true
        name: ID
        type: BIGINT
    - column:
        name: UUID
        type: VARCHAR2(65 BYTE)
    - column:
        name: CREATED_ON
        type: TIMESTAMP(6)
    - column:
        name: UPDATED_ON
        type: TIMESTAMP(6)
    - column:
        name: CREATED_BY
        type: VARCHAR2(200 BYTE)
    - column:
        name: UPDATED_BY
        type: VARCHAR2(200 BYTE)
```
                
This ensures that we the base functionality such as create/update timestamps, ID handling and UUID (to allow comparing
objects that have not been persisted is in place).

Your domain object simply needs to contain the fields that are beyond this base, for example:

```java
@Data
@Entity
public class Widget extends AbstractEntity {

    private String widgetName;

}
```

You can see an example of this in our [Example project](aadhaar-example)

AbstractEntity is core underlying concept, once you have ensured that your domain objects all extend this object and you have
created standard JpaRepositories for each of your domain objects then you can move on to the next step.

AbstractEntityService
---------------------             

We will always create a service layer - this is a place where we can implement additional functionality above the entity
while keeping this isolated from the web layer and the JPA repository.

Since our domain object has been extended from AbstractEntity we can not implement a our service by extending the 
AbstractEntityService with the generic of the type, and due to erasure we also need to provide the type itself.


    

--- Link to example of service in example



The service provides all the basic operations as a layer of abstract from the JpaRepository and should be used in preference
to the JpaRepository directly since it provides a point where we can add functionality as needed.  By default we can leave it
empty to start with.

Then we can move on to the web layer.

AbstractEntityResource
----------------------

In order to allow us to handle the   


License
=======

[See license](LICENSE.md)
