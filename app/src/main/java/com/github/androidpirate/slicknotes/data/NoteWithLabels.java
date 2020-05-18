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

import java.util.Date;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

public class NoteWithLabels {
    @Embedded private Note note;
    @Relation(
            parentColumn = "noteId",
            entityColumn = "labelId",
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

    public long getNoteId() {
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

// TODO : Following getters and setters are here just in case they needed

//    public String getDetails() {
//        return note.getDetails();
//    }

//    public Date getDateCreated() {
//        return note.getDateCreated();
//    }

//    public Date getDateEdited() {
//        return note.getDateEdited();
//    }

//    public boolean isTrash() {
//        return note.isTrash();
//    }

//    public boolean isPinned() {
//        return note.isPinned();
//    }

//    public String getColor() {
//        return note.getColor();
//    }

//    public void setDetails(String details) {
//        note.setDetails(details);
//    }

//    public void setDateCreated(Date dateCreated) {
//        note.setDateCreated(dateCreated);
//    }

//    public void setDateEdited(Date dateEdited) {
//        note.setDateEdited(dateEdited);
//    }

//    public void setTrash(boolean trash) {
//        note.setTrash(trash);
//    }

//    public void setPinned(boolean pinned) {
//        note.setPinned(pinned);
//    }

//    public void setColor(String color) {
//        note.setColor(color);
//    }
}
