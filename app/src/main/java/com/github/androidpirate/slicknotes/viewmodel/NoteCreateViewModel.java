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

import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.repo.NoteRepository;

import java.util.Date;

public class NoteCreateViewModel extends BaseNoteViewModel {
    private static final String DEFAULT_NOTE_TITLE = "";
    private static final String DEFAULT_NOTE_DETAILS = "";
    private NoteRepository repo;

    public NoteCreateViewModel(NoteRepository noteRepository) {
        repo = noteRepository;
        databaseModel = new Note(DEFAULT_NOTE_TITLE, DEFAULT_NOTE_DETAILS, new Date());
    }

    public void insertNote(String title, String details) {
        updateDatabaseModel(title, details);
        repo.insertDatabaseNote(databaseModel);
    }

    private void updateDatabaseModel(String title, String details) {
        databaseModel.setTitle(title);
        databaseModel.setDetails(details);
    }
}
