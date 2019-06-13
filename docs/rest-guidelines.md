REST Guidelines
===

Originally taken from https://blog.restcase.com/5-basic-rest-api-design-guidelines/

Introduction
----

As soon as we start working on an API, design issues arise. A robust and strong design is a key factor for API success. A poorly designed API will indeed lead to misuse or – even worse – no use at all by its intended clients: application developers.
Creating and providing a state of the art API requires taking into account:

    RESTful API principles as described in the literature (Roy Fielding, Martin Fowler, HTTP specification…)
    The API practices of the Web Giants like Google, Microsoft, Facebook, PayPal and other big companies.

Designing a REST API raises questions and issues for which there is no universal answer. REST best practices are still being debated and consolidated, which is what makes this job fascinating.

Here are the 5 basic design guidelines that make a RESTful API:

* Resources (URIs)
* HTTP methods
* HTTP headers
* Query parameters
* Status Codes

REST API Guidelines
---

Let's go over each one and explain a bit.

### 1. Resources (URIs)


#### Names and Verbs

To describe your resources, use concrete names and not action verbs.
For decades, computer scientists used action verbs in order to expose services in an RPC way, for instance:

getUser(1234) createUser(user) deleteAddress(1234)

By contrast, the RESTful approach is to use:

GET /users/1234 
POST /users (with JSON describing a user in the body) 
DELETE /addresses/1234

#### URI case

When it comes to naming resources use spinal-case (sometimes called kebab style). 

##### spinal-case

spinal-case is a variant of snake case which uses hyphens “-” to separate words. The pros and cons are quite similar to those of snake case, with the exception that some languages do not allow hyphens in symbol names (for variable, class, or function naming). You may find it referred to as lisp-case because it is the usual way to name variables and functions in Lisp dialects. It is also the traditional way of naming folders and files in UNIX and Linux systems. Examples: spinal-case, current-user, etc.

According to RFC3986, URLs are “case sensitive” (except for the scheme and the host).
In practice, though, a sensitive case may create dysfunctions with APIs hosted on a Windows system.

It is recommended to use spinal-case (which is highlighted by RFC3986), this case is used by Google, PayPal and other big companies.

### 2. HTTP methods

As stated earlier, one of the key objectives of the REST approach is using HTTP as an application protocol in order to avoid shaping a homemade API.

Hence, we should systematically use HTTP verbs to describe what actions are performed on the resources and facilitate the developer’s work handling recurrent CRUD operations.

The following methods are usually observed:
GET

The GET method is used to retrieve information from the given server using a given URI. Requests using GET should only retrieve data and should have no other effect on the data.
HEAD

Same as GET, but transfers the status line and header section only.
POST

A POST request is used to send data to the server, for example, customer information, file upload, etc. using HTML forms.
PUT

Replaces all current representations of the target resource with the uploaded content.
DELETE

Removes all current representations of the target resource given by a URI.
OPTIONS

Describes the communication options for the target resource.

### 3. HTTP headers

HTTP header fields provide required information about the request or response, or about the object sent in the message body.

There are 4 types of HTTP message headers:

#### General Header

These header fields have general applicability for both request and response messages.

#### Client Request Header

These header fields have applicability only for request messages.

####Server Response Header

These header fields have applicability only for response messages.

####Entity Header

These header fields define meta information about the entity-body or, if no BODY is present, about the resource identified by the request.


### 4. Query parameters

#### Paging

It is necessary to anticipate the paging of your resources in the early design phase of your API. It is indeed difficult to foresee precisely the progression of the amount of data that will be returned. Therefore, we recommend paginating your resources with default values when they are not provided by the calling client, for example with a range of values [0-25].

#### Filtering

Filtering consists in restricting the number of queried resources by specifying some attributes and their expected values. It is possible to filter a collection on several attributes at the same time and to allow several values for one filtered attribute.

#### Sorting

Sorting the result of a query on a collection of resources. A sort parameter should contain the names of the attributes on which the sorting is performed, separated by a comma.

#### Searching

A search is a sub-resource of a collection. As such, its results will have a different format than the resources and the collection itself. This allows us to add suggestions, corrections, and information related to the search.
Parameters are provided the same way as for a filter, through the query-string, but they are not necessarily exact values, and their syntax permits approximate matching.

### 5. Status Codes

It is very important that as a RESTful API, you make use of the proper HTTP Status Codes especially when mocking RESTful API.

The mostly used status codes:
200 – OK

Everything is working
201 – CREATED

New resource has been created
204 – NO CONTENT

The resource was successfully deleted, no response body
304 – NOT MODIFIED

The date returned is cached data (data has not changed)
400 – BAD REQUEST

The request was invalid or cannot be served. The exact error should be explained in the error payload. E.g. „The JSON is not valid “.
401 – UNATHORIZED

The request requires user authentication.
403 – FORBIDDEN

The server understood the request but is refusing it or the access is not allowed.
404 – NOT FOUND

There is no resource behind the URI.
500 – INTERNAL SERVER ERROR

API developers should avoid this error. If an error occurs in the global catch blog, the stack trace should be logged and not returned as a response.