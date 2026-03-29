package me.lakitu.permissioncontrol

import android.content.ContentResolver
import android.content.Context
import android.provider.Settings

class SystemSettingsController(context: Context) {

    private val contentResolver: ContentResolver = context.contentResolver

    fun isUsbDebuggingEnabled(): Boolean {
        return try {
            Settings.Secure.getInt(contentResolver, Settings.Global.ADB_ENABLED, 0) == 1
        } catch (_: Exception) {
            false
        }
    }

    fun setUsbDebuggingEnabled(enabled: Boolean): Boolean {
        return try {
            Settings.Secure.putInt(
                contentResolver,
                Settings.Global.ADB_ENABLED,
                if (enabled) 1 else 0
            )
        } catch (_: Exception) {
            false
        }
    }

    fun isWirelessDebuggingEnabled(): Boolean {
        return try {
            Settings.Global.getInt(contentResolver, "adb_wifi_enabled", 0) == 1
        } catch (_: Exception) {
            false
        }
    }

    fun setWirelessDebuggingEnabled(enabled: Boolean): Boolean {
        return try {
            Settings.Global.putInt(
                contentResolver,
                "adb_wifi_enabled",
                if (enabled) 1 else 0
            )
            true
        } catch (e: Exception) {
            android.util.Log.e("SettingsController", "Failed to set adb_wifi_enabled", e)
            false
        }
    }

    fun isDevelopmentSettingsEnabled(): Boolean {
        return try {
            Settings.Secure.getInt(contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1
        } catch (_: Exception) {
            false
        }
    }

    fun setDevelopmentSettingsEnabled(enabled: Boolean): Boolean {
        return try {
            Settings.Secure.putInt(
                contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                if (enabled) 1 else 0
            )
        } catch (_: Exception) {
            false
        }
    }

    data class SettingItem(
        val name: String,
        val key: String,
        val isBoolean: Boolean = true,
        val getValue: () -> Any?,
        val setValue: (Boolean) -> Boolean
    )

    fun getAllSettings(): List<SettingItem> {
        return listOf(
            SettingItem(
                name = "USB调试",
                key = "adb_enabled",
                getValue = { isUsbDebuggingEnabled() },
                setValue = { setUsbDebuggingEnabled(it) }
            ),
             SettingItem(
                 name = "无线调试",
                 key = "adb_wifi_enabled",
                 getValue = { isWirelessDebuggingEnabled() },
                 setValue = { setWirelessDebuggingEnabled(it) }
             ),


            SettingItem(
                name = "开发者模式",
                key = "development_settings_enabled",
                getValue = { isDevelopmentSettingsEnabled() },
                setValue = { setDevelopmentSettingsEnabled(it) }
            )
        )
    }
}
