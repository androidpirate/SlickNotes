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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.repo.NoteRepository;
import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class NoteListViewModel extends AndroidViewModel {
    private NoteRepository repo;
    private LiveData<List<Note>> uiModel;
    private List<Note> databaseModel;
    private List<Integer> selectedNoteIds;
    private List<Note> selectedNotes;
    private boolean hasAlternateMenu;

    public NoteListViewModel(@NonNull Application application) {
        super(application);
        repo = new NoteRepository(application);
        initialize();
    }

    public LiveData<List<Note>> getDatabaseNotes() {
        return uiModel;
    }

    public LiveData<List<Note>> getDatabaseTrashNotes() {
        return repo.getDatabaseTrashNotes();
    }

    public void sendNotesToTrash(List<Note> notes) {
        databaseModel = new ArrayList<>(notes);
        for (Note note:
             databaseModel) {
            note.setPinned(false);
            note.setTrash(true);
        }
        repo.updateDatabaseNotes(databaseModel);
        clearSelections();
    }

    public void setNotePinStatus(List<Note> notes) {
        databaseModel = new ArrayList<>(notes);
        for (Note note:
             databaseModel) {
            note.setPinned(!note.isPinned());
        }
        repo.updateDatabaseNotes(databaseModel);
        clearSelections();
    }

    public void deleteNotes(List<Note> notes) {
        repo.deleteDatabaseNotes(notes);
    }

    public void restoreNotes(List<Note> notes) {
        databaseModel = new ArrayList<>(notes);
        for (Note note:
             databaseModel) {
            note.setTrash(false);
        }
        repo.updateDatabaseNotes(databaseModel);
    }

    public void restoreNote(int noteId) {
        // TODO: Implement restoring deleted card here
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

    public void clearSelections() {
        clearSelectedNotes();
        clearSelectedNotesIds();
        setHasAlternateMenu();
    }

    private void initialize() {
        selectedNotes = new ArrayList<>();
        selectedNoteIds = new ArrayList<>();
        LiveData<List<Note>> databaseNotes = repo.getDatabaseNotes();
        uiModel = Transformations.map(databaseNotes, new Function<List<Note>, List<Note>>() {
            @Override
            public List<Note> apply(List<Note> notes) {
                return orderNotes(notes);
            }
        });
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

    private List<Note> orderNotes(List<Note> notes) {
        Collections.sort(notes, new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                if(o1.isPinned() && !o2.isPinned()) {
                    return -1;
                } else if(!o1.isPinned() && o2.isPinned()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return notes;
    }
}