# SectorControllerApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createSector**](SectorControllerApi.md#createSector) | **POST** /api/structures/sectors | 
[**deleteSector**](SectorControllerApi.md#deleteSector) | **DELETE** /api/structures/sectors/{id} | 
[**getAllSectors**](SectorControllerApi.md#getAllSectors) | **GET** /api/structures/sectors | 
[**getSectorById**](SectorControllerApi.md#getSectorById) | **GET** /api/structures/sectors/{id} | 

<a name="createSector"></a>
# **createSector**
> Sector createSector(body)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = SectorControllerApi()
val body : Sector =  // Sector | 
try {
    val result : Sector = apiInstance.createSector(body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SectorControllerApi#createSector")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SectorControllerApi#createSector")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**Sector**](Sector.md)|  |

### Return type

[**Sector**](Sector.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*

<a name="deleteSector"></a>
# **deleteSector**
> deleteSector(id)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = SectorControllerApi()
val id : kotlin.Any =  // kotlin.Any | 
try {
    apiInstance.deleteSector(id)
} catch (e: ClientException) {
    println("4xx response calling SectorControllerApi#deleteSector")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SectorControllerApi#deleteSector")
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

<a name="getAllSectors"></a>
# **getAllSectors**
> kotlin.Any getAllSectors()



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = SectorControllerApi()
try {
    val result : kotlin.Any = apiInstance.getAllSectors()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SectorControllerApi#getAllSectors")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SectorControllerApi#getAllSectors")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**kotlin.Any**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*

<a name="getSectorById"></a>
# **getSectorById**
> Sector getSectorById(id)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = SectorControllerApi()
val id : kotlin.Any =  // kotlin.Any | 
try {
    val result : Sector = apiInstance.getSectorById(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SectorControllerApi#getSectorById")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SectorControllerApi#getSectorById")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**kotlin.Any**](.md)|  |

### Return type

[**Sector**](Sector.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*

