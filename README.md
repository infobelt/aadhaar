Aadhaar
=======

This is a collection of useful utilities that Infobelt leverages when building Spring Boot/Angular based applications as
part of their suite of technologies.

The tools can be used to simplify building applications and provide some out-of-the-box ways to handle standard REST APIs.

In the following sections, we walk through how some of the components can be used in your application.

Installation
============

In your Spring Boot application add a dependency on:

```xml
<dependency>
    <groupId>com.infobelt.aadhaar</groupId>
    <artifactId>aadhaar-spring-boot</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

Domain Objects, Services and Web Resources
==========================================

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
@Getter
@Setter
@Entity
public class Widget extends AbstractEntity {

    private String widgetName;

}
```

You can see an example of this in our [Example project](aadhaar-example)

If you have a situation where you have named the ID column in your database to be something other than ID, then you would
need to override that in your domain object (the same applies to naming the table) this is done as shown below:


```java
@Data
@Entity
@Table(name="my_widgets")
@AttributeOverride(name = "id", column = @Column(name = "widget_id"))
public class Widget extends AbstractEntity {

    private String widgetName;

}
```

AbstractEntity is core underlying concept, once you have ensured that your domain objects all extend this object and you have
created standard JpaRepositories for each of your domain objects then you can move on to the next step.

AbstractEntityService
---------------------             

We will always create a service layer - this is a place where we can implement additional functionality above the entity
while keeping this isolated from the web layer and the JPA repository.

Since our domain object has been extended from AbstractEntity we can not implement a our service by extending the 
AbstractEntityService with the generic of the type, and due to erasure we also need to provide the type itself.


```java
@Service
public class WidgetService extends AbstractEntityService<Widget> {

    @Override
    protected Class<Widget> getEntityClass() {
        return Widget.class;
    }

}
```

You can see an example of this in our [Example project](aadhaar-example)


The service provides all the basic operations as a layer of abstract from the JpaRepository and should be used in preference
to the JpaRepository directly since it provides a point where we can add functionality as needed.  By default we can leave it
empty to start with.

Then we can move on to the web layer.

AbstractEntityResource
----------------------

In order to allow us to surface the domain object as a standard RESTful resource we will simply need to implement an MVC controller.
Since our domain object extends AbstractEntity it is easy for us to leverage the AbstractEntityResource object to do this.

This class leverages the AbstractEntityService, so you must create one of these first.

```java
@RestController
@RequestMapping("/api/widgets")
public class WidgetResource extends AbstractEntityResource<Widget> {
}
```

This will provide standard REST endpoints for listing, get, update, create and delete.  

**Important Node**


It is an important note the default list method (in this case /api/widgets/) will have pagination, sorting and
filtering built in.  If you application was previously assuming that /api/widgets/ would simply return a list of all
the objects then you need to change to use /api/widget/list.

Once you have these basics in place it is worth reading the (API documentation)[docs/api.md] to learn what is exposed to the 
client and how it works.


Testing
=======

More soon...

License
=======

[See license](LICENSE.md)
