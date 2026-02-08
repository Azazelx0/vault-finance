package com.example.vault.data.local.database

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // Example Enum Converter (if needed explicitly, otherwise Room might default to name)
    // For cleaner DB values, we often persist String
}
