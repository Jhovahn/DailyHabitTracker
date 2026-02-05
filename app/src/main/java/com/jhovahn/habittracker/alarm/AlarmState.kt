package com.jhovahn.habittracker.alarm

import android.content.Context
import androidx.core.content.edit
import com.jhovahn.habittracker.data.Habit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object AlarmState {
    private const val PREF = "alarm_state"
    private const val KEY_RINGING = "key_ringing"
    private val _isAlarmActiveId =  MutableStateFlow(-1)
    var activeAlarmId = _isAlarmActiveId.asStateFlow()

    fun setRinging(context: Context, ringing: Boolean, habit: Habit?) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit() {
                putBoolean(KEY_RINGING, ringing)
            }
        _isAlarmActiveId.value = habit?.id ?: -1

    }

    fun isRinging(context: Context): Boolean {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getBoolean(KEY_RINGING, false)
    }
}