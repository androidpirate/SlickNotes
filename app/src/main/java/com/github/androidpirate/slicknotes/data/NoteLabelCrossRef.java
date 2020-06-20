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

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"noteId", "labelTitle"})
public class NoteLabelCrossRef {
    private int noteId;
    @NonNull
    private String labelTitle;

    public NoteLabelCrossRef(int noteId, @NonNull String labelTitle) {
        this.noteId = noteId;
        this.labelTitle = labelTitle;
    }

    public int getNoteId() {
        return noteId;
    }

    @NonNull
    public String getLabelTitle() {
        return labelTitle;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public void setLabelId(String labelTitle) {
        this.labelTitle = labelTitle;
    }
}
