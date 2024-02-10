package com.ayush.headline.data.local

import androidx.room.TypeConverter
import com.ayush.headline.data.models.Source

class SourceConverter {

    @TypeConverter
    fun toString(source: Source) = "${source.id} ${source.name}"

    @TypeConverter
    fun toSource(str: String): Source {
        val words = str.split(" ")
        return Source(id = words[0], name = words[1])
    }
}