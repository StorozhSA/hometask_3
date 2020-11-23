package ru.skillbranch.skillarticles.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.*
import androidx.navigation.NavDestination
import com.google.android.material.bottomnavigation.BottomNavigationView

fun View.setMarginOptionally(
    left: Int = marginLeft,
    top: Int = marginTop,
    right: Int = marginRight,
    bottom: Int = marginBottom
) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(left, top, right, bottom)
    layoutParams = params
}

fun View.setPaddingOptionally(
    left: Int = paddingLeft,
    top: Int = paddingTop,
    right: Int = paddingRight,
    bottom: Int = paddingBottom
) {
    setPadding(left, top, right, bottom)
}

fun BottomNavigationView.selectDestination(destination: NavDestination) {
    menu.findItem(destination.id)?.let {
        it.isChecked = true
    } ?: run { menu.children.last().isChecked = true }
}