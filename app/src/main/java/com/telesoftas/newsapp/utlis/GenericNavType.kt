package ektif.detectionreaction.utils

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class GenericNavType<T>(
    private val serializer: KSerializer<T>,
    isNullableAllowed: Boolean = false,
) : NavType<T>(isNullableAllowed = isNullableAllowed) {

    override fun put(bundle: Bundle, key: String, value: T) {
        val serializedValue = Json.encodeToString(serializer, value)
        bundle.putString(key, Uri.encode(serializedValue))
    }

    override fun get(bundle: Bundle, key: String): T? {
        val jsonString = bundle.getString(key)?.let { Uri.decode(it) }
        return jsonString?.let { Json.decodeFromString(serializer, it) }
    }

    override fun parseValue(value: String): T {
        return Json.decodeFromString(serializer, Uri.decode(value))
    }

    override fun serializeAsValue(value: T): String {
        return Uri.encode(Json.encodeToString(serializer, value))
    }
}