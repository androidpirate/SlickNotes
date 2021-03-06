/*
 * <!--
 *  Copyright (C) 2016 The Android Open Source Project
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 * -->
 */

package com.github.androidpirate.slicknotes.data;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface LabelDao {

    @Query("SELECT * FROM labels")
    LiveData<List<Label>> getAllLabels();

    @Query("UPDATE labels SET labelTitle = :newTitle WHERE labelTitle = :originalLabelTitle")
    void updateLabel(String originalLabelTitle, String newTitle);

    @Query("UPDATE NoteLabelCrossRef SET labelTitle = :newTitle WHERE labelTitle = :originalLabelTitle")
    void updateNoteLabelCrossRef(String originalLabelTitle, String newTitle);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLabel(@NonNull Label label);

    @Delete
    void deleteLabel(@NonNull Label label);
}