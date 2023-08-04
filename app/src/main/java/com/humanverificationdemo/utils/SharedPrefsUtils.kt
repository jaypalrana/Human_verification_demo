package com.humanverificationdemo.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPrefsUtils {
    private const val NAME = "MediapipeDemo"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val KEY_LANDMARKS = Pair("key_landmarks", null)
    private val KEY_WIDTH = Pair("key_width", 0)
    private val KEY_HEIGHT = Pair("key_height", 0)

    private val LEFT_CHEEK_X = Pair("left_cheek_x", 0F)
    private val LEFT_CHEEK_Y = Pair("left_cheek_y", 0F)
    private val RIGHT_CHEEK_X = Pair("right_cheek_x", 0F)
    private val RIGHT_CHEEK_Y = Pair("right_cheek_y", 0F)
    private val FOREHEAD_Y = Pair("forehead_y", 0F)
    private val FOREHEAD_X = Pair("forehead_x", 0F)
    private val CHIN_X = Pair("chin_x", 0F)
    private val CHIN_Y = Pair("chin_y", 0F)



    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(
        operation:
            (SharedPreferences.Editor) -> Unit
    ) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var landmarks: String?
        get() = preferences.getString(KEY_LANDMARKS.first, KEY_LANDMARKS.second)
        set(value) = preferences.edit() {
            it.putString(KEY_LANDMARKS.first, value)
        }

    var width: Int
        get() = preferences.getInt(KEY_WIDTH.first, 0)
        set(value) = preferences.edit {
            it.putInt(KEY_WIDTH.first, value)
        }

    var height: Int
        get() = preferences.getInt(KEY_HEIGHT.first, 0)
        set(value) = preferences.edit {
            it.putInt(KEY_HEIGHT.first, value)
        }

    var leftCheekX: Float
        get() = preferences.getFloat(LEFT_CHEEK_X.first, LEFT_CHEEK_X.second)
        set(value) = preferences.edit {
            it.putFloat(LEFT_CHEEK_X.first, value)
        }

    var leftCheekY: Float
        get() = preferences.getFloat(LEFT_CHEEK_Y.first, LEFT_CHEEK_Y.second)
        set(value) = preferences.edit {
            it.putFloat(LEFT_CHEEK_Y.first, value)
        }

    var rightCheekY: Float
        get() = preferences.getFloat(RIGHT_CHEEK_Y.first, RIGHT_CHEEK_Y.second)
        set(value) = preferences.edit {
            it.putFloat(RIGHT_CHEEK_Y.first, value)
        }

    var rightCheekX: Float
        get() = preferences.getFloat(RIGHT_CHEEK_X.first, RIGHT_CHEEK_X.second)
        set(value) = preferences.edit {
            it.putFloat(RIGHT_CHEEK_X.first, value)
        }

    var foreheadX: Float
        get() = preferences.getFloat(FOREHEAD_X.first, FOREHEAD_X.second)
        set(value) = preferences.edit {
            it.putFloat(FOREHEAD_X.first, value)
        }

    var foreheadY: Float
        get() = preferences.getFloat(FOREHEAD_Y.first, FOREHEAD_Y.second)
        set(value) = preferences.edit {
            it.putFloat(FOREHEAD_Y.first, value)
        }

    var chinY: Float
        get() = preferences.getFloat(CHIN_Y.first, CHIN_Y.second)
        set(value) = preferences.edit {
            it.putFloat(CHIN_Y.first, value)
        }

    var chinX: Float
        get() = preferences.getFloat(CHIN_X.first, CHIN_X.second)
        set(value) = preferences.edit {
            it.putFloat(CHIN_X.first, value)
        }

}