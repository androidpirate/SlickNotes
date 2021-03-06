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

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int noteId;
    @ColumnInfo(name = "note_title")
    private String title;
    @ColumnInfo(name = "note_detail")
    private String details;
    @ColumnInfo(name = "note_create_date")
    private Date dateCreated;
    @ColumnInfo(name = "note_edit_date")
    private Date dateEdited;
    @ColumnInfo(name = "note_trash_status")
    private boolean isTrash;
    @ColumnInfo(name = "note_pin_status")
    private boolean isPinned;
    @ColumnInfo(name = "note_color")
    private String color;

    private static final String NOTE_DEFAULT_COLOR = "DEFAULT_BACKGROUND_COLOR";

    public Note(String title, String details, Date dateCreated) {
        this.title = title;
        this.details = details;
        this.dateCreated = dateCreated;
        this.dateEdited = dateCreated;
        this.isTrash = false;
        this.isPinned = false;
        this.color = NOTE_DEFAULT_COLOR;
    }

    public int getNoteId() {
        return noteId;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateEdited() {
        return dateEdited;
    }

    public boolean isTrash() {
        return isTrash;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public String getColor() {
        return color;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateEdited(Date dateEdited) {
        this.dateEdited = dateEdited;
    }

    public void setTrash(boolean trash) {
        isTrash = trash;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }

    public void setColor(String color) {
        this.color = color;
    }
}