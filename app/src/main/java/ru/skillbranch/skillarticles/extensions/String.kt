package ru.skillbranch.skillarticles.extensions

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText

fun String?.indexesOf(substr: String, ignoreCase: Boolean = true): List<Int> {
    val result: MutableList<Int> = mutableListOf()
    var currentIndex = 0

    if (this.isNullOrEmpty() || substr == "") {
        return result
    } else {
        while (currentIndex <= this.length) {
            val index = this.indexOf(substr, currentIndex, ignoreCase)
            if (index == -1) break
            result.add(index)
            currentIndex = this.indexOf(substr, index, ignoreCase) + 1
        }
    }
    return result
}

fun String.isValidEmail(): Boolean =
    this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun TextInputEditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}