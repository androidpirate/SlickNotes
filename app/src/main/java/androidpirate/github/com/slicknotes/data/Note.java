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

package androidpirate.github.com.slicknotes.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int noteId;
    @ColumnInfo(name = "note_title")
    String title;
    @ColumnInfo(name = "note_detail")
    String detail;
    @ColumnInfo(name = "note_create_date")
    Date dateCreated;
    @ColumnInfo(name = "note_edit_date")
    Date dateEdited;
    @ColumnInfo(name = "note_trash_status")
    boolean isTrash;
    @ColumnInfo(name = "note_pin_status")
    boolean isPinned;
    int colorId;
    List<String> labels;

    public Note(String title, String detail, Date dateCreated) {
        this.title = title;
        this.detail = detail;
        this.dateCreated = dateCreated;
        this.dateEdited = dateCreated;
        this.isTrash = false;
        this.isPinned = false;
        this.colorId = 0;
        this.labels = new ArrayList<>();
    }

    public int getNoteId() {
        return noteId;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
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

    public int getColorId() {
        return colorId;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
}