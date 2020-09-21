package ru.skillbranch.skillarticles.data.delegates

import ru.skillbranch.skillarticles.data.local.PrefManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PrefDelegate<T>(private val defaultValue: T) {

  private var storedValue: T? = null

  operator fun provideDelegate(
    thisRef: PrefManager,
    prop: KProperty<*>
  ): ReadWriteProperty<PrefManager, T?> {
    val key = prop.name
    return object : ReadWriteProperty<PrefManager, T?> {
      override fun getValue(thisRef: PrefManager, property: KProperty<*>): T? =
        with(thisRef.preferences) {
          @Suppress("UNCHECKED_CAST")
          if (storedValue == null) {
            storedValue = when (defaultValue) {
              is Int -> getInt(key, defaultValue) as T
              is Long -> getLong(key, defaultValue) as T
              is Float -> getFloat(key, defaultValue) as T
              is String -> getString(key, defaultValue) as T
              is Boolean -> getBoolean(key, defaultValue) as T
              else -> throw IllegalArgumentException("This type cannot be saved into Preferences")
            }
          }
          storedValue
        }

      override fun setValue(thisRef: PrefManager, property: KProperty<*>, value: T?) {
        with(thisRef.preferences.edit()) {
          when (value) {
            is Boolean -> putBoolean(key, value)
            is String -> putString(key, value)
            is Float -> putFloat(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            else -> throw IllegalArgumentException("This type cannot be saved into Preferences")
          }
          apply()
        }
        storedValue = value
      }
    }
  }
}
