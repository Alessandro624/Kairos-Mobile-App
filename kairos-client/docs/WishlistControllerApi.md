# WishlistControllerApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addEventToWishlist**](WishlistControllerApi.md#addEventToWishlist) | **POST** /v1/auth/{wishlistId}/events/{eventId} | 
[**addUserToWishlist**](WishlistControllerApi.md#addUserToWishlist) | **POST** /v1/auth/{wishlistId}/users/{userId} | 
[**countWishlistsByCreator**](WishlistControllerApi.md#countWishlistsByCreator) | **GET** /v1/auth/creators/{creatorId}/count | 
[**createWishlist**](WishlistControllerApi.md#createWishlist) | **POST** /v1/auth | 
[**deleteWishlist**](WishlistControllerApi.md#deleteWishlist) | **DELETE** /v1/auth/{wishlistId} | 
[**getWishlistById**](WishlistControllerApi.md#getWishlistById) | **GET** /v1/auth/{wishlistId} | 
[**getWishlists**](WishlistControllerApi.md#getWishlists) | **POST** /v1/auth/filter | 
[**removeEventFromWishlist**](WishlistControllerApi.md#removeEventFromWishlist) | **DELETE** /v1/auth/{wishlistId}/events/{eventId} | 
[**removeUserFromWishlist**](WishlistControllerApi.md#removeUserFromWishlist) | **DELETE** /v1/auth/{wishlistId}/users/{userId} | 
[**updateWishlist**](WishlistControllerApi.md#updateWishlist) | **PUT** /v1/auth/{wishlistId} | 
[**wishlistAlreadyExists**](WishlistControllerApi.md#wishlistAlreadyExists) | **GET** /v1/auth/exists | 

<a name="addEventToWishlist"></a>
# **addEventToWishlist**
> WishlistDTO addEventToWishlist(wishlistId, eventId)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = WishlistControllerApi()
val wishlistId : kotlin.Any =  // kotlin.Any | 
val eventId : kotlin.Any =  // kotlin.Any | 
try {
    val result : WishlistDTO = apiInstance.addEventToWishlist(wishlistId, eventId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling WishlistControllerApi#addEventToWishlist")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling WishlistControllerApi#addEventToWishlist")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **wishlistId** | [**kotlin.Any**](.md)|  |
 **eventId** | [**kotlin.Any**](.md)|  |

### Return type

[**WishlistDTO**](WishlistDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*

<a name="addUserToWishlist"></a>
# **addUserToWishlist**
> WishlistDTO addUserToWishlist(wishlistId, userId)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = WishlistControllerApi()
val wishlistId : kotlin.Any =  // kotlin.Any | 
val userId : kotlin.Any =  // kotlin.Any | 
try {
    val result : WishlistDTO = apiInstance.addUserToWishlist(wishlistId, userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling WishlistControllerApi#addUserToWishlist")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling WishlistControllerApi#addUserToWishlist")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **wishlistId** | [**kotlin.Any**](.md)|  |
 **userId** | [**kotlin.Any**](.md)|  |

### Return type

[**WishlistDTO**](WishlistDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*

<a name="countWishlistsByCreator"></a>
# **countWishlistsByCreator**
> kotlin.Any countWishlistsByCreator(creatorId)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = WishlistControllerApi()
val creatorId : kotlin.Any =  // kotlin.Any | 
try {
    val result : kotlin.Any = apiInstance.countWishlistsByCreator(creatorId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling WishlistControllerApi#countWishlistsByCreator")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling WishlistControllerApi#countWishlistsByCreator")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **creatorId** | [**kotlin.Any**](.md)|  |

### Return type

**kotlin.Any**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*

<a name="createWishlist"></a>
# **createWishlist**
> WishlistDTO createWishlist(body)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = WishlistControllerApi()
val body : WishlistCreateDTO =  // WishlistCreateDTO | 
try {
    val result : WishlistDTO = apiInstance.createWishlist(body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling WishlistControllerApi#createWishlist")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling WishlistControllerApi#createWishlist")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**WishlistCreateDTO**](WishlistCreateDTO.md)|  |

### Return type

[**WishlistDTO**](WishlistDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*

<a name="deleteWishlist"></a>
# **deleteWishlist**
> deleteWishlist(wishlistId)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = WishlistControllerApi()
val wishlistId : kotlin.Any =  // kotlin.Any | 
try {
    apiInstance.deleteWishlist(wishlistId)
} catch (e: ClientException) {
    println("4xx response calling WishlistControllerApi#deleteWishlist")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling WishlistControllerApi#deleteWishlist")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **wishlistId** | [**kotlin.Any**](.md)|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="getWishlistById"></a>
# **getWishlistById**
> WishlistDTO getWishlistById(wishlistId)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = WishlistControllerApi()
val wishlistId : kotlin.Any =  // kotlin.Any | 
try {
    val result : WishlistDTO = apiInstance.getWishlistById(wishlistId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling WishlistControllerApi#getWishlistById")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling WishlistControllerApi#getWishlistById")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **wishlistId** | [**kotlin.Any**](.md)|  |

### Return type

[**WishlistDTO**](WishlistDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*

<a name="getWishlists"></a>
# **getWishlists**
> PageWishlistDTO getWishlists(body, page, size)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = WishlistControllerApi()
val body : WishlistFilterDTO =  // WishlistFilterDTO | 
val page : kotlin.Any =  // kotlin.Any | 
val size : kotlin.Any =  // kotlin.Any | 
try {
    val result : PageWishlistDTO = apiInstance.getWishlists(body, page, size)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling WishlistControllerApi#getWishlists")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling WishlistControllerApi#getWishlists")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**WishlistFilterDTO**](WishlistFilterDTO.md)|  |
 **page** | [**kotlin.Any**](.md)|  |
 **size** | [**kotlin.Any**](.md)|  |

### Return type

[**PageWishlistDTO**](PageWishlistDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*

<a name="removeEventFromWishlist"></a>
# **removeEventFromWishlist**
> removeEventFromWishlist(wishlistId, eventId)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = WishlistControllerApi()
val wishlistId : kotlin.Any =  // kotlin.Any | 
val eventId : kotlin.Any =  // kotlin.Any | 
try {
    apiInstance.removeEventFromWishlist(wishlistId, eventId)
} catch (e: ClientException) {
    println("4xx response calling WishlistControllerApi#removeEventFromWishlist")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling WishlistControllerApi#removeEventFromWishlist")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **wishlistId** | [**kotlin.Any**](.md)|  |
 **eventId** | [**kotlin.Any**](.md)|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="removeUserFromWishlist"></a>
# **removeUserFromWishlist**
> removeUserFromWishlist(wishlistId, userId)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = WishlistControllerApi()
val wishlistId : kotlin.Any =  // kotlin.Any | 
val userId : kotlin.Any =  // kotlin.Any | 
try {
    apiInstance.removeUserFromWishlist(wishlistId, userId)
} catch (e: ClientException) {
    println("4xx response calling WishlistControllerApi#removeUserFromWishlist")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling WishlistControllerApi#removeUserFromWishlist")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **wishlistId** | [**kotlin.Any**](.md)|  |
 **userId** | [**kotlin.Any**](.md)|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="updateWishlist"></a>
# **updateWishlist**
> WishlistDTO updateWishlist(body, wishlistId)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = WishlistControllerApi()
val body : WishlistUpdateDTO =  // WishlistUpdateDTO | 
val wishlistId : kotlin.Any =  // kotlin.Any | 
try {
    val result : WishlistDTO = apiInstance.updateWishlist(body, wishlistId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling WishlistControllerApi#updateWishlist")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling WishlistControllerApi#updateWishlist")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**WishlistUpdateDTO**](WishlistUpdateDTO.md)|  |
 **wishlistId** | [**kotlin.Any**](.md)|  |

### Return type

[**WishlistDTO**](WishlistDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*

<a name="wishlistAlreadyExists"></a>
# **wishlistAlreadyExists**
> kotlin.Any wishlistAlreadyExists(creatorId, name)



### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = WishlistControllerApi()
val creatorId : kotlin.Any =  // kotlin.Any | 
val name : kotlin.Any =  // kotlin.Any | 
try {
    val result : kotlin.Any = apiInstance.wishlistAlreadyExists(creatorId, name)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling WishlistControllerApi#wishlistAlreadyExists")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling WishlistControllerApi#wishlistAlreadyExists")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **creatorId** | [**kotlin.Any**](.md)|  |
 **name** | [**kotlin.Any**](.md)|  |

### Return type

**kotlin.Any**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*

