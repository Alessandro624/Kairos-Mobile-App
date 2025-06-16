package it.unical.demacs.informatica.kairosapp.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import io.swagger.client.models.SectorDTO

class Converters {
    @TypeConverter
    fun fromSectorDTOList(value: List<SectorDTO>?): String? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<SectorDTO>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toSectorDTOList(value: String?): List<SectorDTO>? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<SectorDTO>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }
}
