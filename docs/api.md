Standard API
============

One of the key aims of Aadhaar is to provide a standard way to present a REST API with a set of
out of the box capabilities that allow you to work quickly without writing any code.

All these capabilities are exposed at two levels:

1) The Service Layer
2) The Web Layer

Lets discuss each of these.

Service Layer
-------------

More to follow

Web Layer
---------

The web layer aims to provide a standard REST API that is consistent across entities and rich enough
to help with servicing about 80% of the requirements you might have for data access.

Your Entity must have a Service Layer (ie. have an implementation of an AbstractEntityService) in order
to allow you to serve up the Resource (or Web) layer.  You must also have implemented a JpaRepository in 
Spring Boot Data to ensure the Service Layer works (see above in Service Layer).

Our REST API is based on standards as much as possible and therefore you have created a Resource as follows:

```java
@RestController
@RequestMapping("/api/widgets")
public class WidgetResource extends AbstractEntityResource<Widget> {
}
```

You can see an example of API that is generated [in this Swaagger Documentation](aadaar-example/docs/swagger.md)
