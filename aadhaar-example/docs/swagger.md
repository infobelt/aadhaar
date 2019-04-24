# Api Documentation


<a name="overview"></a>
## Overview
Api Documentation


### Version information
*Version* : 1.0


### License information
*License* : Apache 2.0  
*License URL* : http://www.apache.org/licenses/LICENSE-2.0  
*Terms of service* : urn:tos


### URI scheme
*Host* : localhost:52101  
*BasePath* : /


### Tags

* basic-error-controller : Basic Error Controller
* widget-resource : Widget Resource




<a name="paths"></a>
## Paths

<a name="createusingpost"></a>
### Create a new instance of the resource
```
POST /api/widgets
```


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**entity**  <br>*required*|entity|[Widget](#widget)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Successfully created the new resource|[Widget](#widget)|
|**201**|Created|No Content|
|**401**|You are not authorized to view the resource|No Content|
|**403**|Accessing the resource you were trying to reach is forbidden|No Content|
|**404**|The resource you were trying to reach is not found|No Content|
|**500**|An internal exception has occured, check the logs for more information|No Content|


#### Consumes

* `application/json`


#### Produces

* `\*/*`


#### Tags

* widget-resource


<a name="listusingget"></a>
### List a page of the resources
```
GET /api/widgets
```


#### Parameters

|Type|Name|Schema|
|---|---|---|
|**Query**|**The filters to apply**  <br>*optional*|string|
|**Query**|**The number of entries in a page**  <br>*optional*|integer (int32)|
|**Query**|**The page number**  <br>*optional*|integer (int32)|
|**Query**|**The sorts to apply**  <br>*optional*|string|
|**Query**|**excludes**  <br>*optional*|< string > array(multi)|
|**Query**|**filters[0].columnName**  <br>*optional*|string|
|**Query**|**filters[0].value**  <br>*optional*|string|
|**Query**|**includes**  <br>*optional*|< string > array(multi)|
|**Query**|**queryComplexFilter.field**  <br>*optional*|string|
|**Query**|**queryComplexFilter.ignoreCase**  <br>*optional*|boolean|
|**Query**|**queryComplexFilter.logic**  <br>*optional*|enum (and, or)|
|**Query**|**queryComplexFilter.operator**  <br>*optional*|enum (contains, doesnotcontain, endswith, startswith, eq, neq, gte, gt, lte, lt, isnull, isnotnull, isempty)|
|**Query**|**queryComplexFilter.value**  <br>*optional*|string|
|**Query**|**sorts[0].columnName**  <br>*optional*|string|
|**Query**|**sorts[0].direction**  <br>*optional*|enum (asc, desc)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Successfully listed a page the resources|[Page«Widget»](#45c18d88e0b9f9be269b2a1dc9d28b3e)|
|**401**|You are not authorized to view the resource|No Content|
|**403**|Accessing the resource you were trying to reach is forbidden|No Content|
|**404**|The resource you were trying to reach is not found|No Content|
|**500**|An internal exception has occurred, check the logs for more information|No Content|


#### Produces

* `\*/*`


#### Tags

* widget-resource


<a name="updateusingput_1"></a>
### Update the given instance (legacy method)
```
PUT /api/widgets
```

Caution : 
operation.deprecated


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**entity**  <br>*required*|entity|[Widget](#widget)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Successfully updated the resource|[Widget](#widget)|
|**201**|Created|No Content|
|**401**|You are not authorized to view the resource|No Content|
|**403**|Accessing the resource you were trying to reach is forbidden|No Content|
|**404**|The resource you were trying to reach is not found|No Content|
|**500**|An internal exception has occurred, check the logs for more information|No Content|


#### Consumes

* `application/json`


#### Produces

* `\*/*`


#### Tags

* widget-resource


<a name="searchusingget"></a>
### Perform a search
```
GET /api/widgets/_search
```


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**offset**  <br>*optional*||integer (int64)|
|**Query**|**pageNumber**  <br>*optional*||integer (int32)|
|**Query**|**pageSize**  <br>*optional*||integer (int32)|
|**Query**|**paged**  <br>*optional*||boolean|
|**Query**|**query**  <br>*required*|query|string|
|**Query**|**sort.sorted**  <br>*optional*||boolean|
|**Query**|**sort.unsorted**  <br>*optional*||boolean|
|**Query**|**unpaged**  <br>*optional*||boolean|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Successfully searched for the resource|[Page«Widget»](#45c18d88e0b9f9be269b2a1dc9d28b3e)|
|**401**|You are not authorized to view the resource|No Content|
|**403**|Accessing the resource you were trying to reach is forbidden|No Content|
|**404**|The resource you were trying to reach is not found|No Content|
|**500**|An internal exception has occurred, check the logs for more information|No Content|


#### Produces

* `\*/*`


#### Tags

* widget-resource


<a name="oldlistallusingget"></a>
### List all of the resources (legacy)
```
GET /api/widgets/list
```

Caution : 
operation.deprecated


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Successfully listed all the resources|< [Widget](#widget) > array|
|**401**|You are not authorized to view the resource|No Content|
|**403**|Accessing the resource you were trying to reach is forbidden|No Content|
|**404**|The resource you were trying to reach is not found|No Content|
|**500**|An internal exception has occurred, check the logs for more information|No Content|


#### Produces

* `\*/*`


#### Tags

* widget-resource


<a name="getusingget"></a>
### Get an resource with the provided ID
```
GET /api/widgets/{id}
```


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**id**  <br>*required*|id|integer (int64)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Successfully got the resource|[Widget](#widget)|
|**401**|You are not authorized to view the resource|No Content|
|**403**|Accessing the resource you were trying to reach is forbidden|No Content|
|**404**|The resource you were trying to reach is not found|No Content|
|**500**|An internal exception has occurred, check the logs for more information|No Content|


#### Produces

* `\*/*`


#### Tags

* widget-resource


<a name="updateusingput"></a>
### Update the given instance
```
PUT /api/widgets/{id}
```


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**id**  <br>*required*|id|integer (int64)|
|**Body**|**entity**  <br>*required*|entity|[Widget](#widget)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Successfully updated the resource|[Widget](#widget)|
|**201**|Created|No Content|
|**401**|You are not authorized to view the resource|No Content|
|**403**|Accessing the resource you were trying to reach is forbidden|No Content|
|**404**|The resource you were trying to reach is not found|No Content|
|**500**|An internal exception has occured, check the logs for more information|No Content|


#### Consumes

* `application/json`


#### Produces

* `\*/*`


#### Tags

* widget-resource


<a name="deleteusingdelete"></a>
### Delete the resource with the provided ID
```
DELETE /api/widgets/{id}
```


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**id**  <br>*required*|id|integer (int64)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Successfully deleted the resource|No Content|
|**204**|No Content|No Content|
|**401**|You are not authorized to view the resource|No Content|
|**403**|Accessing the resource you were trying to reach is forbidden|No Content|
|**404**|The resource you were trying to reach is not found|No Content|
|**500**|An internal exception has occurred, check the logs for more information|No Content|


#### Produces

* `\*/*`


#### Tags

* widget-resource


<a name="errorhtmlusingpost"></a>
### errorHtml
```
POST /error
```


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelAndView](#modelandview)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


#### Consumes

* `application/json`


#### Produces

* `text/html`


#### Tags

* basic-error-controller


<a name="errorhtmlusingget"></a>
### errorHtml
```
GET /error
```


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelAndView](#modelandview)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


#### Produces

* `text/html`


#### Tags

* basic-error-controller


<a name="errorhtmlusingput"></a>
### errorHtml
```
PUT /error
```


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelAndView](#modelandview)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


#### Consumes

* `application/json`


#### Produces

* `text/html`


#### Tags

* basic-error-controller


<a name="errorhtmlusingdelete"></a>
### errorHtml
```
DELETE /error
```


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelAndView](#modelandview)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


#### Produces

* `text/html`


#### Tags

* basic-error-controller


<a name="errorhtmlusingpatch"></a>
### errorHtml
```
PATCH /error
```


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelAndView](#modelandview)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


#### Consumes

* `application/json`


#### Produces

* `text/html`


#### Tags

* basic-error-controller


<a name="errorhtmlusinghead"></a>
### errorHtml
```
HEAD /error
```


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelAndView](#modelandview)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


#### Consumes

* `application/json`


#### Produces

* `text/html`


#### Tags

* basic-error-controller


<a name="errorhtmlusingoptions"></a>
### errorHtml
```
OPTIONS /error
```


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelAndView](#modelandview)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


#### Consumes

* `application/json`


#### Produces

* `text/html`


#### Tags

* basic-error-controller




<a name="definitions"></a>
## Definitions

<a name="modelandview"></a>
### ModelAndView

|Name|Schema|
|---|---|
|**empty**  <br>*optional*|boolean|
|**model**  <br>*optional*|object|
|**modelMap**  <br>*optional*|< string, object > map|
|**reference**  <br>*optional*|boolean|
|**status**  <br>*optional*|enum (100 CONTINUE, 101 SWITCHING_PROTOCOLS, 102 PROCESSING, 103 CHECKPOINT, 200 OK, 201 CREATED, 202 ACCEPTED, 203 NON_AUTHORITATIVE_INFORMATION, 204 NO_CONTENT, 205 RESET_CONTENT, 206 PARTIAL_CONTENT, 207 MULTI_STATUS, 208 ALREADY_REPORTED, 226 IM_USED, 300 MULTIPLE_CHOICES, 301 MOVED_PERMANENTLY, 302 FOUND, 302 MOVED_TEMPORARILY, 303 SEE_OTHER, 304 NOT_MODIFIED, 305 USE_PROXY, 307 TEMPORARY_REDIRECT, 308 PERMANENT_REDIRECT, 400 BAD_REQUEST, 401 UNAUTHORIZED, 402 PAYMENT_REQUIRED, 403 FORBIDDEN, 404 NOT_FOUND, 405 METHOD_NOT_ALLOWED, 406 NOT_ACCEPTABLE, 407 PROXY_AUTHENTICATION_REQUIRED, 408 REQUEST_TIMEOUT, 409 CONFLICT, 410 GONE, 411 LENGTH_REQUIRED, 412 PRECONDITION_FAILED, 413 PAYLOAD_TOO_LARGE, 413 REQUEST_ENTITY_TOO_LARGE, 414 URI_TOO_LONG, 414 REQUEST_URI_TOO_LONG, 415 UNSUPPORTED_MEDIA_TYPE, 416 REQUESTED_RANGE_NOT_SATISFIABLE, 417 EXPECTATION_FAILED, 418 I_AM_A_TEAPOT, 419 INSUFFICIENT_SPACE_ON_RESOURCE, 420 METHOD_FAILURE, 421 DESTINATION_LOCKED, 422 UNPROCESSABLE_ENTITY, 423 LOCKED, 424 FAILED_DEPENDENCY, 426 UPGRADE_REQUIRED, 428 PRECONDITION_REQUIRED, 429 TOO_MANY_REQUESTS, 431 REQUEST_HEADER_FIELDS_TOO_LARGE, 451 UNAVAILABLE_FOR_LEGAL_REASONS, 500 INTERNAL_SERVER_ERROR, 501 NOT_IMPLEMENTED, 502 BAD_GATEWAY, 503 SERVICE_UNAVAILABLE, 504 GATEWAY_TIMEOUT, 505 HTTP_VERSION_NOT_SUPPORTED, 506 VARIANT_ALSO_NEGOTIATES, 507 INSUFFICIENT_STORAGE, 508 LOOP_DETECTED, 509 BANDWIDTH_LIMIT_EXCEEDED, 510 NOT_EXTENDED, 511 NETWORK_AUTHENTICATION_REQUIRED)|
|**view**  <br>*optional*|[View](#view)|
|**viewName**  <br>*optional*|string|


<a name="pageable"></a>
### Pageable

|Name|Schema|
|---|---|
|**offset**  <br>*optional*|integer (int64)|
|**pageNumber**  <br>*optional*|integer (int32)|
|**pageSize**  <br>*optional*|integer (int32)|
|**paged**  <br>*optional*|boolean|
|**sort**  <br>*optional*|[Sort](#sort)|
|**unpaged**  <br>*optional*|boolean|


<a name="45c18d88e0b9f9be269b2a1dc9d28b3e"></a>
### Page«Widget»

|Name|Schema|
|---|---|
|**content**  <br>*optional*|< [Widget](#widget) > array|
|**empty**  <br>*optional*|boolean|
|**first**  <br>*optional*|boolean|
|**last**  <br>*optional*|boolean|
|**number**  <br>*optional*|integer (int32)|
|**numberOfElements**  <br>*optional*|integer (int32)|
|**pageable**  <br>*optional*|[Pageable](#pageable)|
|**size**  <br>*optional*|integer (int32)|
|**sort**  <br>*optional*|[Sort](#sort)|
|**totalElements**  <br>*optional*|integer (int64)|
|**totalPages**  <br>*optional*|integer (int32)|


<a name="sort"></a>
### Sort

|Name|Schema|
|---|---|
|**empty**  <br>*optional*|boolean|
|**sorted**  <br>*optional*|boolean|
|**unsorted**  <br>*optional*|boolean|


<a name="view"></a>
### View

|Name|Schema|
|---|---|
|**contentType**  <br>*optional*|string|


<a name="widget"></a>
### Widget

|Name|Schema|
|---|---|
|**createdBy**  <br>*optional*|string|
|**createdOn**  <br>*optional*|string (date-time)|
|**id**  <br>*optional*|integer (int64)|
|**updatedBy**  <br>*optional*|string|
|**updatedOn**  <br>*optional*|string (date-time)|
|**uuid**  <br>*optional*|string|
|**widgetName**  <br>*optional*|string|





