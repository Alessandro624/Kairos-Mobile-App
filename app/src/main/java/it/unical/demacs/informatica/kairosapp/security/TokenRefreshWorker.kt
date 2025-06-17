package it.unical.demacs.informatica.kairosapp.security

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import io.swagger.client.apis.AuthenticationApi
import io.swagger.client.infrastructure.ClientException
import io.swagger.client.models.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class TokenRefreshWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val _authManager = AuthManager.getInstance(appContext)
    private val _authApi = AuthenticationApi()

    companion object {
        const val WORK_TAG = "token_refresh_work"

        fun schedule(context: Context, delayMillis: Long) {
            WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG)

            if (delayMillis <= 0) {
                Log.w(
                    "TokenRefreshWorker",
                    "Not valid delay ($delayMillis ms), worker not scheduled."
                )
                return
            }

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val refreshRequest = OneTimeWorkRequestBuilder<TokenRefreshWorker>()
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .addTag(WORK_TAG)
                .build()

            WorkManager.getInstance(context).enqueue(refreshRequest)
            Log.d(
                "TokenRefreshWorker",
                "TokenRefreshWorker scheduled ${delayMillis / 1000} seconds."
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG)
            Log.d("TokenRefreshWorker", "TokenRefreshWorker killed.")
        }
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val refreshToken = _authManager.getRefreshToken()
            val accessToken = _authManager.getAccessToken()

            if (refreshToken == null) {
                Log.d("TokenRefreshWorker", "Refresh token not available.")
                _authManager.clearTokens()
                return@withContext Result.failure()
            }

            try {
                val refreshResponse: AuthResponse = _authApi.refresh(refreshToken, accessToken)

                _authManager.saveTokens(
                    accessToken = refreshResponse.token.toString(),
                    refreshToken = refreshResponse.refreshToken.toString()
                )
                Log.d("TokenRefreshWorker", "Token refreshed.")

                val nextDelay = _authManager.getNextRefreshDelayMillis()
                schedule(applicationContext, nextDelay)

                Result.success()

            } catch (e: Exception) {
                Log.e("TokenRefreshWorker", "Error during refresh: ${e.message}", e)
                if (e is ClientException) {
                    Log.d(
                        "TokenRefreshWorker",
                        "Refresh token expired or not valid. Cleaning tokens."
                    )
                    _authManager.clearTokens()
                    Result.failure()
                } else {
                    Result.retry()
                }
            }
        }
    }
}
