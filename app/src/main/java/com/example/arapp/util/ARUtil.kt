package com.example.arapp.util

import android.app.Activity
import android.content.Context
import com.google.ar.core.ArCoreApk
import kotlinx.coroutines.delay
import timber.log.Timber

class ARUtil(private val context: Context) {

    suspend fun isARSupported(): Boolean {
        val availability = ArCoreApk.getInstance().checkAvailability(context)
        if (availability.isTransient) {
            // Continue to query availability at 5Hz while compatibility is checked in the background.
            delay(200)
            return isARSupported()
        }
        return availability.isSupported
    }

    fun isARServicesInstalled(activity: Activity, onErrorAction: () -> Unit): Boolean {
        // Ensure that Google Play Services for AR and ARCore device profile data are
        // installed and up to date.
        return try {
            when (ArCoreApk.getInstance().requestInstall(activity, true)) {
                ArCoreApk.InstallStatus.INSTALLED -> {
                    // Success: Safe to create the AR session.
                    true
                }
                ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                    // When this method returns `INSTALL_REQUESTED`:
                    // 1. ARCore pauses this activity.
                    // 2. ARCore prompts the user to install or update Google Play
                    //    Services for AR (market://details?id=com.google.ar.core).
                    // 3. ARCore downloads the latest device profile data.
                    // 4. ARCore resumes this activity. The next invocation of
                    //    requestInstall() will either return `INSTALLED` or throw an
                    //    exception if the installation or update did not succeed.
                    false
                }
                else -> {
                    onErrorAction()
                    false
                }
            }
        } catch (e: Throwable) {
            Timber.e(e)
            onErrorAction()
            false
        }
    }
}