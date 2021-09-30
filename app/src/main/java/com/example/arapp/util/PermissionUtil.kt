package com.example.arapp.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.SparseArray
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow

class PermissionUtil {

    private var requestCode = 0
    private val waitingList = SparseArray<MutableSharedFlow<List<Boolean>>>()

    private val incrementedRequestCode: Int
        get() {
            val code = requestCode
            requestCode++
            return code
        }

    @Synchronized
    fun checkAndRequestPermission(activity: Activity, vararg permissions: String): Flow<List<Boolean>> {
        val code = incrementedRequestCode
        return checkAndRequestPermission(
            requestPermission = { activity.requestPermissions(permissions, code) },
            code = code,
            context = activity,
            permissions = permissions
        )
    }

    private fun checkAndRequestPermission(
        requestPermission: () -> Unit,
        code: Int,
        context: Context,
        vararg permissions: String
    ): Flow<List<Boolean>> {
        return if (hasPermissions(context, *permissions)) {
            flow { emit(permissions.map { true }) }
        } else {
            val flow = MutableSharedFlow<List<Boolean>>(replay = 1)
            requestPermission()
            waitingList.append(code, flow)
            flow
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}
