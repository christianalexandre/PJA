package com.example.todo.utils.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "isArchived") var isArchived: Boolean,
    @ColumnInfo(name = "image") var image: ByteArray?,
    @ColumnInfo(name = "conclusionDate") var conclusionDate: Long?
) : Parcelable {
    init {
        if(title.length > 50) throw(IllegalArgumentException("TITLE length cannot exceed 50 characters"))
        if(content.length > 300) throw(IllegalArgumentException("CONTENT length cannot exceed 300 characters"))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (id != other.id) return false
        if (title != other.title) return false
        if (content != other.content) return false
        if (isArchived != other.isArchived) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + isArchived.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}