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

import java.util.List;

import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.repo.NoteRepository;
import androidx.lifecycle.LiveData;

public class NoteListViewModel extends BaseListViewModel {

    public NoteListViewModel(NoteRepository noteRepository) {
        repo = noteRepository;
    }

    public LiveData<List<Note>> getDatabaseNotes(boolean addNewItemsOnTop) {
        if(addNewItemsOnTop) {
            uiModel = repo.getDatabaseNotesOrderedByAscendingDate();
        } else {
            uiModel = repo.getDatabaseNotesOrderedByDescendingDate();
        }
        return uiModel;
    }

    public void sendNotesToTrash(List<Note> notes) {
        for (Note note:
             notes) {
            repo.updateNoteTrashStatus(note.getNoteId(), true);
        }
        clearSelections();
    }

    public void setNotePinStatus(List<Note> notes) {
        for (Note note:
             notes) {
            repo.updateNotePinStatus(note.getNoteId(), !note.isPinned());
        }
        clearSelections();
    }

    public void restoreNote(int noteId) {
       repo.updateNoteTrashStatus(noteId, false);
    }
}