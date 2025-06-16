# EventControllerApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createEvent**](EventControllerApi.md#createEvent) | **POST** /v1/events | Create a new event
[**deleteEvent**](EventControllerApi.md#deleteEvent) | **DELETE** /v1/events/{id} | Delete an event
[**getAllEvents**](EventControllerApi.md#getAllEvents) | **GET** /v1/events | Get all events
[**getEventById**](EventControllerApi.md#getEventById) | **GET** /v1/events/{id} | Get an event by ID
[**updateEvent**](EventControllerApi.md#updateEvent) | **PUT** /v1/events/{id} | Update an event

<a name="createEvent"></a>
# **createEvent**
> EventDTO createEvent(body)

Create a new event

Allows an authorized user to create a new event by providing details such as name, date, location, and description.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = EventControllerApi()
val body : EventCreateDTO =  // EventCreateDTO | 
try {
    val result : EventDTO = apiInstance.createEvent(body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling EventControllerApi#createEvent")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling EventControllerApi#createEvent")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**EventCreateDTO**](EventCreateDTO.md)|  |

### Return type

[**EventDTO**](EventDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*

<a name="deleteEvent"></a>
# **deleteEvent**
> deleteEvent(id)

Delete an event

Delete an existing event by its ID. Only authorized users can delete events.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = EventControllerApi()
val id : kotlin.Any =  // kotlin.Any | 
try {
    apiInstance.deleteEvent(id)
} catch (e: ClientException) {
    println("4xx response calling EventControllerApi#deleteEvent")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling EventControllerApi#deleteEvent")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**kotlin.Any**](.md)|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="getAllEvents"></a>
# **getAllEvents**
> PageEventDTO getAllEvents(pageable, filter)

Get all events

Retrieve a list of all events.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = EventControllerApi()
val pageable : Pageable =  // Pageable | 
val filter : Filter =  // Filter | 
try {
    val result : PageEventDTO = apiInstance.getAllEvents(pageable, filter)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling EventControllerApi#getAllEvents")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling EventControllerApi#getAllEvents")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **pageable** | [**Pageable**](.md)|  |
 **filter** | [**Filter**](.md)|  | [optional]

### Return type

[**PageEventDTO**](PageEventDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*

<a name="getEventById"></a>
# **getEventById**
> EventDTO getEventById(id)

Get an event by ID

Retrieve detailed information about a specific event, including name, date, location, description, and associated details.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = EventControllerApi()
val id : kotlin.Any =  // kotlin.Any | 
try {
    val result : EventDTO = apiInstance.getEventById(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling EventControllerApi#getEventById")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling EventControllerApi#getEventById")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**kotlin.Any**](.md)|  |

### Return type

[**EventDTO**](EventDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*

<a name="updateEvent"></a>
# **updateEvent**
> EventDTO updateEvent(body, id)

Update an event

Update an existing event by ID. Only authorized users can perform this operation.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = EventControllerApi()
val body : EventUpdateDTO =  // EventUpdateDTO | 
val id : kotlin.Any =  // kotlin.Any | 
try {
    val result : EventDTO = apiInstance.updateEvent(body, id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling EventControllerApi#updateEvent")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling EventControllerApi#updateEvent")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**EventUpdateDTO**](EventUpdateDTO.md)|  |
 **id** | [**kotlin.Any**](.md)|  |

### Return type

[**EventDTO**](EventDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*

