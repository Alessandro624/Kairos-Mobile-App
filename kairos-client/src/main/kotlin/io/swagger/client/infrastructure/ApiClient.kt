package io.swagger.client.infrastructure

import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.Locale

open class ApiClient(val baseUrl: String) {
    companion object {
        protected const val ContentType = "Content-Type"
        protected const val Accept = "Accept"
        protected const val JsonMediaType = "application/json"
        protected const val FormDataMediaType = "multipart/form-data"
        protected const val XmlMediaType = "application/xml"
        protected const val PlainTextMediaType = "text/plain"

        @JvmStatic
        val client: OkHttpClient = OkHttpClient()

        @JvmStatic
        var defaultHeaders: Map<String, String> by ApplicationDelegates.setOnce(
            mapOf(
                ContentType to JsonMediaType,
                Accept to JsonMediaType
            )
        )

        @JvmStatic
        val jsonHeaders: Map<String, String> =
            mapOf(ContentType to JsonMediaType, Accept to JsonMediaType)
    }

    @androidx.annotation.RequiresApi(Build.VERSION_CODES.N)
    protected inline fun <reified T> requestBody(
        content: T,
        mediaType: String = JsonMediaType
    ): RequestBody =
        when {
            content is File -> content.asRequestBody(mediaType.toMediaTypeOrNull())

            mediaType == FormDataMediaType -> {
                var builder = FormBody.Builder()
                // content's type *must* be Map<String, Any>
                @Suppress("UNCHECKED_CAST")
                (content as Map<String, String>).forEach { (key, value) ->
                    builder = builder.add(key, value)
                }
                builder.build()
            }

            mediaType == JsonMediaType -> Serializer.moshi.adapter(T::class.java).toJson(content)
                .toRequestBody(mediaType.toMediaTypeOrNull())

            mediaType == XmlMediaType -> TODO("xml not currently supported.")

            mediaType == PlainTextMediaType -> content.toString().toRequestBody(
                mediaType.toMediaTypeOrNull()
            )

            // TODO: this should be extended with other serializers
            else -> TODO("requestBody currently only supports JSON body and File body.")
        }

    protected inline fun <reified T : Any?> responseBody(
        body: ResponseBody?,
        mediaType: String = JsonMediaType
    ): T? {
        if (body == null) return null
        return when (mediaType) {
            JsonMediaType ->
                try {
                    Serializer.moshi.adapter(T::class.java).fromJson(body.source())
                } catch (_: Exception) {
                    null
                }

            else -> TODO()
        }
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    protected inline fun <reified T : Any?> request(
        requestConfig: RequestConfig,
        body: Any? = null
    ): ApiInfrastructureResponse<T?> {
        val httpUrl =
            baseUrl.toHttpUrlOrNull() ?: throw IllegalStateException("baseUrl is invalid.")

        var urlBuilder = httpUrl.newBuilder()
            .addPathSegments(requestConfig.path.trimStart('/'))

        requestConfig.query.forEach { query ->
            query.value.forEach { queryValue ->
                urlBuilder = urlBuilder.addQueryParameter(query.key, queryValue)
            }
        }

        val url = urlBuilder.build()
        val headers = requestConfig.headers.toMutableMap()
        defaultHeaders.forEach { (key, value) ->
            if (!headers.containsKey(key)) headers[key] = value
        }

        if (headers[ContentType] ?: "" == "") {
            throw kotlin.IllegalStateException("Missing Content-Type header. This is required.")
        }

        if (headers[Accept] ?: "" == "") {
            throw kotlin.IllegalStateException("Missing Accept header. This is required.")
        }

        // TODO: support multiple contentType,accept options here.
        val contentType = (headers[ContentType] as String).substringBefore(";")
            .lowercase(Locale.ROOT)
        val accept = (headers[Accept] as String).substringBefore(";").lowercase(Locale.ROOT)

        var request: Request.Builder = when (requestConfig.method) {
            RequestMethod.DELETE -> Request.Builder().url(url).delete()
            RequestMethod.GET -> Request.Builder().url(url)
            RequestMethod.HEAD -> Request.Builder().url(url).head()
            RequestMethod.PATCH -> Request.Builder().url(url).patch(requestBody(body, contentType))
            RequestMethod.PUT -> Request.Builder().url(url).put(requestBody(body, contentType))
            RequestMethod.POST -> Request.Builder().url(url).post(requestBody(body, contentType))
            RequestMethod.OPTIONS -> Request.Builder().url(url).method("OPTIONS", null)
        }

        headers.forEach { header ->
            request = request.addHeader(header.key, header.value.toString())
        }

        val realRequest = request.build()
        val response = client.newCall(realRequest).execute()

        // TODO: handle specific mapping types. e.g. Map<int, Class<?>>
        when {
            response.isRedirect -> return Redirection(
                response.code,
                response.headers.toMultimap()
            )

            response.isInformational -> return Informational(
                response.message,
                response.code,
                response.headers.toMultimap()
            )

            response.isSuccessful -> return Success(
                responseBody(response.body, accept),
                response.code,
                response.headers.toMultimap()
            )

            response.isClientError -> return ClientError(
                response.body?.string(),
                response.code,
                response.headers.toMultimap()
            )

            else -> return ServerError(
                null,
                response.body?.string(),
                response.code,
                response.headers.toMultimap()
            )
        }
    }
}