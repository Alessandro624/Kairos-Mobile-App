# io.swagger.client - Kotlin client library for Kairos Backend API

## Requires

* Kotlin 1.4.30
* Gradle 5.3

## Build

First, create the gradle wrapper script:

```
gradle wrapper
```

Then, run:

```
./gradlew check assemble
```

This runs all tests and packages the library.

## Features/Implementation Notes

* Supports JSON inputs/outputs, File inputs, and Form inputs.
* Supports collection formats for query parameters: csv, tsv, ssv, pipes.
* Some Kotlin and Java types are fully qualified to avoid conflicts with types defined in Swagger definitions.
* Implementation of ApiClient is intended to reduce method counts, specifically to benefit Android targets.

<a name="documentation-for-api-endpoints"></a>
## Documentation for API Endpoints

All URIs are relative to *http://localhost:8080*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*AuthenticationApi* | [**forgotPassword**](docs/AuthenticationApi.md#forgotpassword) | **POST** /v1/auth/forgot-password | Request password reset
*AuthenticationApi* | [**login**](docs/AuthenticationApi.md#login) | **POST** /v1/auth/login | Login user
*AuthenticationApi* | [**oAuth2LoginFailure**](docs/AuthenticationApi.md#oauth2loginfailure) | **GET** /v1/auth/oauth2/login/failure | OAuth2 login failure message
*AuthenticationApi* | [**refresh**](docs/AuthenticationApi.md#refresh) | **POST** /v1/auth/refresh | Refresh JWT token
*AuthenticationApi* | [**register**](docs/AuthenticationApi.md#register) | **POST** /v1/auth/register | Register new user
*AuthenticationApi* | [**resetPassword**](docs/AuthenticationApi.md#resetpassword) | **POST** /v1/auth/reset-password | Confirm password reset
*EventControllerApi* | [**createEvent**](docs/EventControllerApi.md#createevent) | **POST** /v1/events | Create a new event
*EventControllerApi* | [**deleteEvent**](docs/EventControllerApi.md#deleteevent) | **DELETE** /v1/events/{id} | Delete an event
*EventControllerApi* | [**getAllEvents**](docs/EventControllerApi.md#getallevents) | **GET** /v1/events | Get all events
*EventControllerApi* | [**getEventById**](docs/EventControllerApi.md#geteventbyid) | **GET** /v1/events/{id} | Get an event by ID
*EventControllerApi* | [**updateEvent**](docs/EventControllerApi.md#updateevent) | **PUT** /v1/events/{id} | Update an event
*SectorControllerApi* | [**createSector**](docs/SectorControllerApi.md#createsector) | **POST** /api/structures/sectors | 
*SectorControllerApi* | [**deleteSector**](docs/SectorControllerApi.md#deletesector) | **DELETE** /api/structures/sectors/{id} | 
*SectorControllerApi* | [**getAllSectors**](docs/SectorControllerApi.md#getallsectors) | **GET** /api/structures/sectors | 
*SectorControllerApi* | [**getSectorById**](docs/SectorControllerApi.md#getsectorbyid) | **GET** /api/structures/sectors/{id} | 
*StructureControllerApi* | [**createStructure**](docs/StructureControllerApi.md#createstructure) | **POST** /api/structures | 
*StructureControllerApi* | [**deleteStructure**](docs/StructureControllerApi.md#deletestructure) | **DELETE** /api/structures/{id} | 
*StructureControllerApi* | [**getAllPreviewStructures**](docs/StructureControllerApi.md#getallpreviewstructures) | **GET** /api/structures | 
*StructureControllerApi* | [**getSectorsByStructureId**](docs/StructureControllerApi.md#getsectorsbystructureid) | **GET** /api/structures/{id}/sectors | 
*StructureControllerApi* | [**getStructureDetails**](docs/StructureControllerApi.md#getstructuredetails) | **GET** /api/structures/{id} | 
*UsersApi* | [**changePassword**](docs/UsersApi.md#changepassword) | **PUT** /v1/users/change-password | Change user password
*UsersApi* | [**deleteUser**](docs/UsersApi.md#deleteuser) | **DELETE** /v1/users/{userId} | Delete a user
*UsersApi* | [**getAllUsers**](docs/UsersApi.md#getallusers) | **GET** /v1/users | Get all users
*UsersApi* | [**getAllUsersAdmin**](docs/UsersApi.md#getallusersadmin) | **GET** /v1/users/admin | Get all admin users
*UsersApi* | [**getCurrentUser**](docs/UsersApi.md#getcurrentuser) | **GET** /v1/users/me | Get current user's information
*UsersApi* | [**makeUserAdmin**](docs/UsersApi.md#makeuseradmin) | **PUT** /v1/users/{userId}/make-admin | Make a user an admin
*UsersApi* | [**makeUserOrganizer**](docs/UsersApi.md#makeuserorganizer) | **PUT** /v1/users/{userId}/make-organizer | Make a user an organizer
*UsersApi* | [**updateUser**](docs/UsersApi.md#updateuser) | **PUT** /v1/users/{userId} | Update a user
*WishlistControllerApi* | [**addEventToWishlist**](docs/WishlistControllerApi.md#addeventtowishlist) | **POST** /v1/auth/{wishlistId}/events/{eventId} | 
*WishlistControllerApi* | [**addUserToWishlist**](docs/WishlistControllerApi.md#addusertowishlist) | **POST** /v1/auth/{wishlistId}/users/{userId} | 
*WishlistControllerApi* | [**countWishlistsByCreator**](docs/WishlistControllerApi.md#countwishlistsbycreator) | **GET** /v1/auth/creators/{creatorId}/count | 
*WishlistControllerApi* | [**createWishlist**](docs/WishlistControllerApi.md#createwishlist) | **POST** /v1/auth | 
*WishlistControllerApi* | [**deleteWishlist**](docs/WishlistControllerApi.md#deletewishlist) | **DELETE** /v1/auth/{wishlistId} | 
*WishlistControllerApi* | [**getWishlistById**](docs/WishlistControllerApi.md#getwishlistbyid) | **GET** /v1/auth/{wishlistId} | 
*WishlistControllerApi* | [**getWishlists**](docs/WishlistControllerApi.md#getwishlists) | **POST** /v1/auth/filter | 
*WishlistControllerApi* | [**removeEventFromWishlist**](docs/WishlistControllerApi.md#removeeventfromwishlist) | **DELETE** /v1/auth/{wishlistId}/events/{eventId} | 
*WishlistControllerApi* | [**removeUserFromWishlist**](docs/WishlistControllerApi.md#removeuserfromwishlist) | **DELETE** /v1/auth/{wishlistId}/users/{userId} | 
*WishlistControllerApi* | [**updateWishlist**](docs/WishlistControllerApi.md#updatewishlist) | **PUT** /v1/auth/{wishlistId} | 
*WishlistControllerApi* | [**wishlistAlreadyExists**](docs/WishlistControllerApi.md#wishlistalreadyexists) | **GET** /v1/auth/exists | 

<a name="documentation-for-models"></a>
## Documentation for Models

 - [io.swagger.client.models.Address](docs/Address.md)
 - [io.swagger.client.models.AuthRequest](docs/AuthRequest.md)
 - [io.swagger.client.models.AuthResponse](docs/AuthResponse.md)
 - [io.swagger.client.models.EventCreateDTO](docs/EventCreateDTO.md)
 - [io.swagger.client.models.EventDTO](docs/EventDTO.md)
 - [io.swagger.client.models.EventImageCreateDTO](docs/EventImageCreateDTO.md)
 - [io.swagger.client.models.EventUpdateDTO](docs/EventUpdateDTO.md)
 - [io.swagger.client.models.Filter](docs/Filter.md)
 - [io.swagger.client.models.Page](docs/Page.md)
 - [io.swagger.client.models.PageEventDTO](docs/PageEventDTO.md)
 - [io.swagger.client.models.PageStructureDTO](docs/PageStructureDTO.md)
 - [io.swagger.client.models.PageWishlistDTO](docs/PageWishlistDTO.md)
 - [io.swagger.client.models.Pageable](docs/Pageable.md)
 - [io.swagger.client.models.PageableObject](docs/PageableObject.md)
 - [io.swagger.client.models.PasswordResetConfirmation](docs/PasswordResetConfirmation.md)
 - [io.swagger.client.models.PasswordResetRequest](docs/PasswordResetRequest.md)
 - [io.swagger.client.models.Sector](docs/Sector.md)
 - [io.swagger.client.models.SectorDTO](docs/SectorDTO.md)
 - [io.swagger.client.models.ServiceError](docs/ServiceError.md)
 - [io.swagger.client.models.SortObject](docs/SortObject.md)
 - [io.swagger.client.models.Structure](docs/Structure.md)
 - [io.swagger.client.models.StructureCreateDTO](docs/StructureCreateDTO.md)
 - [io.swagger.client.models.StructureDTO](docs/StructureDTO.md)
 - [io.swagger.client.models.StructureDetailsDTO](docs/StructureDetailsDTO.md)
 - [io.swagger.client.models.StructureFilterDTO](docs/StructureFilterDTO.md)
 - [io.swagger.client.models.StructureImage](docs/StructureImage.md)
 - [io.swagger.client.models.StructureImageDTO](docs/StructureImageDTO.md)
 - [io.swagger.client.models.StructureSector](docs/StructureSector.md)
 - [io.swagger.client.models.UserCreateDTO](docs/UserCreateDTO.md)
 - [io.swagger.client.models.UserDTO](docs/UserDTO.md)
 - [io.swagger.client.models.UserImageDTO](docs/UserImageDTO.md)
 - [io.swagger.client.models.UserPasswordUpdateDTO](docs/UserPasswordUpdateDTO.md)
 - [io.swagger.client.models.UserUpdateDTO](docs/UserUpdateDTO.md)
 - [io.swagger.client.models.WishlistCreateDTO](docs/WishlistCreateDTO.md)
 - [io.swagger.client.models.WishlistDTO](docs/WishlistDTO.md)
 - [io.swagger.client.models.WishlistFilterDTO](docs/WishlistFilterDTO.md)
 - [io.swagger.client.models.WishlistUpdateDTO](docs/WishlistUpdateDTO.md)

<a name="documentation-for-authorization"></a>
## Documentation for Authorization

<a name="bearerAuth"></a>
### bearerAuth


