# UsersApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**changePassword**](UsersApi.md#changePassword) | **PUT** /v1/users/change-password | Change user password
[**deleteUser**](UsersApi.md#deleteUser) | **DELETE** /v1/users/{userId} | Delete a user
[**getAllUsers**](UsersApi.md#getAllUsers) | **GET** /v1/users | Get all users
[**getAllUsersAdmin**](UsersApi.md#getAllUsersAdmin) | **GET** /v1/users/admin | Get all admin users
[**getCurrentUser**](UsersApi.md#getCurrentUser) | **GET** /v1/users/me | Get current user&#x27;s information
[**makeUserAdmin**](UsersApi.md#makeUserAdmin) | **PUT** /v1/users/{userId}/make-admin | Make a user an admin
[**makeUserOrganizer**](UsersApi.md#makeUserOrganizer) | **PUT** /v1/users/{userId}/make-organizer | Make a user an organizer
[**updateUser**](UsersApi.md#updateUser) | **PUT** /v1/users/{userId} | Update a user

<a name="changePassword"></a>
# **changePassword**
> changePassword(body)

Change user password

Allows an authenticated user to change their password.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = UsersApi()
val body : UserPasswordUpdateDTO =  // UserPasswordUpdateDTO | Old and new passwords
try {
    apiInstance.changePassword(body)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#changePassword")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#changePassword")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**UserPasswordUpdateDTO**](UserPasswordUpdateDTO.md)| Old and new passwords |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*

<a name="deleteUser"></a>
# **deleteUser**
> deleteUser(userId)

Delete a user

Deletes a specific user by their ID. Requires ADMIN role.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = UsersApi()
val userId : kotlin.Any =  // kotlin.Any | ID of the user to delete
try {
    apiInstance.deleteUser(userId)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#deleteUser")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#deleteUser")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | [**kotlin.Any**](.md)| ID of the user to delete |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*, application/json

<a name="getAllUsers"></a>
# **getAllUsers**
> Page getAllUsers(page, size, sortBy, direction)

Get all users

Retrieves a paginated list of all users. Requires ADMIN role.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = UsersApi()
val page : kotlin.Any =  // kotlin.Any | Page number (default: 0)
val size : kotlin.Any =  // kotlin.Any | Number of items per page (default: 10, max: 30)
val sortBy : kotlin.Any =  // kotlin.Any | Field to sort by (default: username)
val direction : kotlin.Any =  // kotlin.Any | Sort direction (default: ASC)
try {
    val result : Page = apiInstance.getAllUsers(page, size, sortBy, direction)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#getAllUsers")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#getAllUsers")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **page** | [**kotlin.Any**](.md)| Page number (default: 0) | [optional] [default to 0]
 **size** | [**kotlin.Any**](.md)| Number of items per page (default: 10, max: 30) | [optional] [default to 10]
 **sortBy** | [**kotlin.Any**](.md)| Field to sort by (default: username) | [optional] [default to username]
 **direction** | [**kotlin.Any**](.md)| Sort direction (default: ASC) | [optional] [default to ASC]

### Return type

[**Page**](Page.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*

<a name="getAllUsersAdmin"></a>
# **getAllUsersAdmin**
> Page getAllUsersAdmin(page, size, sortBy, direction)

Get all admin users

Retrieves a paginated list of all users with the ADMIN role. Requires ADMIN role.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = UsersApi()
val page : kotlin.Any =  // kotlin.Any | Page number (default: 0)
val size : kotlin.Any =  // kotlin.Any | Number of items per page (default: 10, max: 30)
val sortBy : kotlin.Any =  // kotlin.Any | Field to sort by (default: username)
val direction : kotlin.Any =  // kotlin.Any | Sort direction (default: ASC)
try {
    val result : Page = apiInstance.getAllUsersAdmin(page, size, sortBy, direction)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#getAllUsersAdmin")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#getAllUsersAdmin")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **page** | [**kotlin.Any**](.md)| Page number (default: 0) | [optional] [default to 0]
 **size** | [**kotlin.Any**](.md)| Number of items per page (default: 10, max: 30) | [optional] [default to 10]
 **sortBy** | [**kotlin.Any**](.md)| Field to sort by (default: username) | [optional] [default to username]
 **direction** | [**kotlin.Any**](.md)| Sort direction (default: ASC) | [optional] [default to ASC]

### Return type

[**Page**](Page.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*

<a name="getCurrentUser"></a>
# **getCurrentUser**
> UserDTO getCurrentUser()

Get current user&#x27;s information

Retrieves the information of the currently authenticated user.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = UsersApi()
try {
    val result : UserDTO = apiInstance.getCurrentUser()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#getCurrentUser")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#getCurrentUser")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**UserDTO**](UserDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*

<a name="makeUserAdmin"></a>
# **makeUserAdmin**
> UserDTO makeUserAdmin(userId)

Make a user an admin

Elevates a specific user&#x27;s role to ADMIN. Requires ADMIN role.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = UsersApi()
val userId : kotlin.Any =  // kotlin.Any | ID of the user to make admin
try {
    val result : UserDTO = apiInstance.makeUserAdmin(userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#makeUserAdmin")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#makeUserAdmin")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | [**kotlin.Any**](.md)| ID of the user to make admin |

### Return type

[**UserDTO**](UserDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*

<a name="makeUserOrganizer"></a>
# **makeUserOrganizer**
> UserDTO makeUserOrganizer(userId)

Make a user an organizer

Elevates a specific user&#x27;s role to ORGANIZER. Requires ADMIN role.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = UsersApi()
val userId : kotlin.Any =  // kotlin.Any | ID of the user to make organizer
try {
    val result : UserDTO = apiInstance.makeUserOrganizer(userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#makeUserOrganizer")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#makeUserOrganizer")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | [**kotlin.Any**](.md)| ID of the user to make organizer |

### Return type

[**UserDTO**](UserDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*

<a name="updateUser"></a>
# **updateUser**
> UserDTO updateUser(body, userId)

Update a user

Updates a specific user&#x27;s information. Only the user themselves can perform this action.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = UsersApi()
val body : UserUpdateDTO =  // UserUpdateDTO | User details to update
val userId : kotlin.Any =  // kotlin.Any | ID of the user to update
try {
    val result : UserDTO = apiInstance.updateUser(body, userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#updateUser")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#updateUser")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**UserUpdateDTO**](UserUpdateDTO.md)| User details to update |
 **userId** | [**kotlin.Any**](.md)| ID of the user to update |

### Return type

[**UserDTO**](UserDTO.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*

