# AuthenticationApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**forgotPassword**](AuthenticationApi.md#forgotPassword) | **POST** /v1/auth/forgot-password | Request password reset
[**login**](AuthenticationApi.md#login) | **POST** /v1/auth/login | Login user
[**oAuth2LoginFailure**](AuthenticationApi.md#oAuth2LoginFailure) | **GET** /v1/auth/oauth2/login/failure | OAuth2 login failure message
[**refresh**](AuthenticationApi.md#refresh) | **POST** /v1/auth/refresh | Refresh JWT token
[**register**](AuthenticationApi.md#register) | **POST** /v1/auth/register | Register new user
[**resetPassword**](AuthenticationApi.md#resetPassword) | **POST** /v1/auth/reset-password | Confirm password reset

<a name="forgotPassword"></a>
# **forgotPassword**
> kotlin.Any forgotPassword(body)

Request password reset

Initiates the password reset process by sending a reset link to the user&#x27;s email if the user exists.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = AuthenticationApi()
val body : PasswordResetRequest =  // PasswordResetRequest | User's username or email
try {
    val result : kotlin.Any = apiInstance.forgotPassword(body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AuthenticationApi#forgotPassword")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthenticationApi#forgotPassword")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**PasswordResetRequest**](PasswordResetRequest.md)| User&#x27;s username or email |

### Return type

**kotlin.Any**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*

<a name="login"></a>
# **login**
> AuthResponse login(body)

Login user

Authenticates a user and returns JWT and refresh tokens.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = AuthenticationApi()
val body : AuthRequest =  // AuthRequest | Authentication request with username/email and password
try {
    val result : AuthResponse = apiInstance.login(body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AuthenticationApi#login")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthenticationApi#login")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**AuthRequest**](AuthRequest.md)| Authentication request with username/email and password |

### Return type

[**AuthResponse**](AuthResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*

<a name="oAuth2LoginFailure"></a>
# **oAuth2LoginFailure**
> kotlin.Any oAuth2LoginFailure()

OAuth2 login failure message

Endpoint to indicate a failed OAuth2 login attempt.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = AuthenticationApi()
try {
    val result : kotlin.Any = apiInstance.oAuth2LoginFailure()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AuthenticationApi#oAuth2LoginFailure")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthenticationApi#oAuth2LoginFailure")
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
 - **Accept**: application/json, text/plain, */*

<a name="refresh"></a>
# **refresh**
> AuthResponse refresh(body)

Refresh JWT token

Refreshes the JWT token using a valid refresh token.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = AuthenticationApi()
val body : kotlin.Any =  // kotlin.Any | Refresh token
try {
    val result : AuthResponse = apiInstance.refresh(body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AuthenticationApi#refresh")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthenticationApi#refresh")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**kotlin.Any**](kotlin.Any.md)| Refresh token |

### Return type

[**AuthResponse**](AuthResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: text/plain
 - **Accept**: application/json, */*

<a name="register"></a>
# **register**
> kotlin.Any register(body)

Register new user

Registers a new user account.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = AuthenticationApi()
val body : UserCreateDTO =  // UserCreateDTO | User registration details (username, email, password)
try {
    val result : kotlin.Any = apiInstance.register(body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AuthenticationApi#register")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthenticationApi#register")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**UserCreateDTO**](UserCreateDTO.md)| User registration details (username, email, password) |

### Return type

**kotlin.Any**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*

<a name="resetPassword"></a>
# **resetPassword**
> kotlin.Any resetPassword(body)

Confirm password reset

Resets the user&#x27;s password using a valid reset token.

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*;

val apiInstance = AuthenticationApi()
val body : PasswordResetConfirmation =  // PasswordResetConfirmation | New password and the reset token
try {
    val result : kotlin.Any = apiInstance.resetPassword(body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AuthenticationApi#resetPassword")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthenticationApi#resetPassword")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**PasswordResetConfirmation**](PasswordResetConfirmation.md)| New password and the reset token |

### Return type

**kotlin.Any**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*

