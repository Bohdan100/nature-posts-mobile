package com.terra.natureposts.helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyboardHelper {
    @SuppressLint("ClickableViewAccessibility")
    fun Activity.setupHideKeyboardOnTouchOutside(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    hideKeyboard()
                    clearFocus(view)
                }
                false
            }
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupHideKeyboardOnTouchOutside(innerView)
            }
        }
    }

    private fun Activity.hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun Activity.clearFocus(view: View) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                clearFocus(innerView)
            }
        } else {
            view.clearFocus()
        }
    }
}