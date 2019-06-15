# Aadhaar Template API Doc


<a name="overview"></a>
## Overview
More description about the API


### Version information
*Version* : 1.0.0


### URI scheme
*Host* : localhost:45683  
*BasePath* : /


### Tags

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
|**500**|An internal exception has occurred, check the logs for more information|[ExceptionReport](#exceptionreport)|


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
|**Query**|**filter**  <br>*optional*|string|
|**Query**|**filters[0].columnName**  <br>*optional*|string|
|**Query**|**filters[0].value**  <br>*optional*|string|
|**Query**|**page**  <br>*optional*|integer (int32)|
|**Query**|**pageSize**  <br>*optional*|integer (int32)|
|**Query**|**queryComplexFilter.field**  <br>*optional*|string|
|**Query**|**queryComplexFilter.ignoreCase**  <br>*optional*|boolean|
|**Query**|**queryComplexFilter.logic**  <br>*optional*|enum (and, or)|
|**Query**|**queryComplexFilter.operator**  <br>*optional*|enum (contains, doesnotcontain, endswith, startswith, eq, neq, gte, gt, lte, lt, gte_date, lte_date, isnull, isnotnull, isempty)|
|**Query**|**queryComplexFilter.value**  <br>*optional*|string|
|**Query**|**sorts**  <br>*optional*|string|
|**Query**|**sorts[0].columnName**  <br>*optional*|string|
|**Query**|**sorts[0].direction**  <br>*optional*|enum (asc, desc)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Successfully listed a page the resources|[Page«Widget»](#45c18d88e0b9f9be269b2a1dc9d28b3e)|
|**401**|You are not authorized to view the resource|No Content|
|**403**|Accessing the resource you were trying to reach is forbidden|No Content|
|**404**|The resource you were trying to reach is not found|No Content|
|**500**|An internal exception has occurred, check the logs for more information|[ExceptionReport](#exceptionreport)|


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
|**500**|An internal exception has occurred, check the logs for more information|[ExceptionReport](#exceptionreport)|


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
|**500**|An internal exception has occurred, check the logs for more information|[ExceptionReport](#exceptionreport)|


#### Produces

* `\*/*`


#### Tags

* widget-resource


<a name="oldlistallusingget"></a>
### List all the resources (legacy)
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
|**500**|An internal exception has occurred, check the logs for more information|[ExceptionReport](#exceptionreport)|


#### Produces

* `\*/*`


#### Tags

* widget-resource


<a name="getusingget"></a>
### Get a resource with the provided ID
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
|**500**|An internal exception has occurred, check the logs for more information|[ExceptionReport](#exceptionreport)|


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
|**500**|An internal exception has occurred, check the logs for more information|[ExceptionReport](#exceptionreport)|


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
|**500**|An internal exception has occurred, check the logs for more information|[ExceptionReport](#exceptionreport)|


#### Produces

* `\*/*`


#### Tags

* widget-resource




<a name="definitions"></a>
## Definitions

<a name="exceptionreport"></a>
### ExceptionReport

|Name|Schema|
|---|---|
|**description**  <br>*optional*|string|
|**exceptionUuid**  <br>*optional*|string|
|**message**  <br>*optional*|string|


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





