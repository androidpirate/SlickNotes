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

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

public class NoteWithLabels {
    @Embedded private Note note;
    @Relation(
            parentColumn = "noteId",
            entityColumn = "labelTitle",
            associateBy = @Junction(NoteLabelCrossRef.class)
    )
    private List<Label> labels;

    public NoteWithLabels(Note note, List<Label> labels) {
        this.note = note;
        this.labels = labels;
    }

    public Note getNote() {
        return note;
    }

    public int getNoteId() {
        return note.getNoteId();
    }

    public String getTitle() {
        return note.getTitle();
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public void setNoteId(int noteId) {
        note.setNoteId(noteId);
    }

    public void setTitle(String title) {
        note.setTitle(title);
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

}