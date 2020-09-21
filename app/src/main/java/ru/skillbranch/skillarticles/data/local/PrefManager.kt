package ru.skillbranch.skillarticles.data.local

import android.content.Context
import android.content.SharedPreferences
import ru.skillbranch.skillarticles.data.delegates.PrefDelegate

class PrefManager(context: Context) {

  internal val preferences: SharedPreferences by lazy {
    context.getSharedPreferences("default", Context.MODE_PRIVATE)
  }

  var storedInt by PrefDelegate(Int.MAX_VALUE)
  var storedLong by PrefDelegate(Long.MAX_VALUE)
  var storedFloat by PrefDelegate(100f)
  var storedString by PrefDelegate("test")
  var storedBoolean by PrefDelegate(false)

  fun clearAll() {
    preferences.edit().clear().apply()
  }
}
