package com.content.mercy.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.content.mercy.model.entity.FeelCard

/**
 * Created by rapsealk on 2019-11-06..
 */
@Dao
interface FeelCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg feelcards: FeelCard)
}