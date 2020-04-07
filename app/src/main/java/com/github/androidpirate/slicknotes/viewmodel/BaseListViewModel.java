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

package com.github.androidpirate.slicknotes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.repo.NoteRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListViewModel extends ViewModel {
    NoteRepository repo;
    LiveData<List<Note>> uiModel;
    List<Note> databaseModel;
    List<Integer> selectedNoteIds;
    List<Note> selectedNotes;
    private boolean hasAlternateMenu;
    private int itemPosition = 0;

    BaseListViewModel() {
        selectedNotes = new ArrayList<>();
        selectedNoteIds = new ArrayList<>();
    }

    public boolean hasAlternateMenu() {
        return hasAlternateMenu;
    }

    public void addToSelectedNotes(Note note) {
        selectedNotes.add(note);
        selectedNoteIds.add(note.getNoteId());
        setHasAlternateMenu();
    }

    public void removeFromSelectedNotes(Note note) {
        selectedNotes.remove(note);
        // Casting Integer is necessary, otherwise treats note id as index number
        selectedNoteIds.remove((Integer)note.getNoteId());
        setHasAlternateMenu();
    }

    public List<Note> getSelectedNotes() {
        return selectedNotes;
    }

    public List<Integer> getSelectedNoteIds() {
        return selectedNoteIds;
    }

    public void setItemPosition(int position) {
        itemPosition = position;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    public void clearSelections() {
        clearSelectedNotes();
        clearSelectedNotesIds();
        setHasAlternateMenu();
    }

    private void setHasAlternateMenu() {
        hasAlternateMenu = selectedNotes.size() > 0;
    }

    private void clearSelectedNotes() {
        selectedNotes.clear();
    }

    private void clearSelectedNotesIds() {
        selectedNoteIds.clear();
    }
}
