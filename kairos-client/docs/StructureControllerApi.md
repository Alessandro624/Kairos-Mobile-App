# StructureControllerApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createStructure**](StructureControllerApi.md#createStructure) | **POST** /api/structures | 
[**deleteStructure**](StructureControllerApi.md#deleteStructure) | **DELETE** /api/structures/{id} | 
[**getAllPreviewStructures**](StructureControllerApi.md#getAllPreviewStructures) | **GET** /api/structures | 
[**getSectorsByStructureId**](StructureControllerApi.md#getSectorsByStructureId) | **GET** /api/structures/{id}/sectors | 
[**getStructureDetails**](StructureControllerApi.md#getStructureDetails) | **GET** /api/structures/{id} | 

<a name="createStructure"></a>
# **createStructure**
> StructureDTO createStructure(body)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = StructureControllerApi()
val body : StructureCreateDTO =  // StructureCreateDTO | 
try {
    val result : StructureDTO = apiInstance.createStructure(body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling StructureControllerApi#createStructure")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling StructureControllerApi#createStructure")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**StructureCreateDTO**](StructureCreateDTO.md)|  |

### Return type

[**StructureDTO**](StructureDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="deleteStructure"></a>
# **deleteStructure**
> deleteStructure(id)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = StructureControllerApi()
val id : kotlin.Any =  // kotlin.Any | 
try {
    apiInstance.deleteStructure(id)
} catch (e: ClientException) {
    println("4xx response calling StructureControllerApi#deleteStructure")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling StructureControllerApi#deleteStructure")
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

<a name="getAllPreviewStructures"></a>
# **getAllPreviewStructures**
> PageStructureDTO getAllPreviewStructures(structureFilterDTO, page, size, sortBy, direction)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = StructureControllerApi()
val structureFilterDTO : StructureFilterDTO =  // StructureFilterDTO | 
val page : kotlin.Any =  // kotlin.Any | 
val size : kotlin.Any =  // kotlin.Any | 
val sortBy : kotlin.Any =  // kotlin.Any | 
val direction : kotlin.Any =  // kotlin.Any | 
try {
    val result : PageStructureDTO = apiInstance.getAllPreviewStructures(structureFilterDTO, page, size, sortBy, direction)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling StructureControllerApi#getAllPreviewStructures")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling StructureControllerApi#getAllPreviewStructures")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **structureFilterDTO** | [**StructureFilterDTO**](.md)|  |
 **page** | [**kotlin.Any**](.md)|  | [optional] [default to 0]
 **size** | [**kotlin.Any**](.md)|  | [optional] [default to 10]
 **sortBy** | [**kotlin.Any**](.md)|  | [optional] [default to id]
 **direction** | [**kotlin.Any**](.md)|  | [optional] [default to DESC]

### Return type

[**PageStructureDTO**](PageStructureDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="getSectorsByStructureId"></a>
# **getSectorsByStructureId**
> kotlin.Any getSectorsByStructureId(id)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = StructureControllerApi()
val id : kotlin.Any =  // kotlin.Any | 
try {
    val result : kotlin.Any = apiInstance.getSectorsByStructureId(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling StructureControllerApi#getSectorsByStructureId")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling StructureControllerApi#getSectorsByStructureId")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**kotlin.Any**](.md)|  |

### Return type

**kotlin.Any**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="getStructureDetails"></a>
# **getStructureDetails**
> StructureDetailsDTO getStructureDetails(id)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = StructureControllerApi()
val id : kotlin.Any =  // kotlin.Any | 
try {
    val result : StructureDetailsDTO = apiInstance.getStructureDetails(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling StructureControllerApi#getStructureDetails")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling StructureControllerApi#getStructureDetails")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**kotlin.Any**](.md)|  |

### Return type

[**StructureDetailsDTO**](StructureDetailsDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

