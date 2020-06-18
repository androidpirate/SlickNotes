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

import com.github.androidpirate.slicknotes.data.Label;
import com.github.androidpirate.slicknotes.data.NoteWithLabels;
import com.github.androidpirate.slicknotes.repo.LabelRepository;
import com.github.androidpirate.slicknotes.repo.NoteRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class NoteLabelViewModel extends ViewModel {
    private NoteRepository noteRepository;
    private LabelRepository labelRepository;
    private NoteWithLabels databaseNote;
    private List<Label> labels;
    private ArrayList<String> noteLabels;

    public NoteLabelViewModel(NoteRepository noteRepository, LabelRepository labelRepository) {
        this.noteRepository = noteRepository;
        this.labelRepository = labelRepository;
    }

    public LiveData<List<Label>> getDatabaseLabels() {
        return labelRepository.getDatabaseLabels();
    }

    public LiveData<NoteWithLabels> getDatabaseNote(int noteId) {
        return noteRepository.getDatabaseNote(noteId);
    }

    public void setDatabaseNote(NoteWithLabels note) {
        databaseNote = note;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public ArrayList<String> getNoteLabels() {
        return noteLabels;
    }

    public void setNoteLabels(ArrayList<String> noteLabels) {
        this.noteLabels = noteLabels;
    }

    public void createNewLabel(String labelTitle) {
        Label label = new Label(labelTitle);
        noteLabels.add(label.getLabelTitle());
        labelRepository.insertLabel(label);
        insertNoteLabel(label);
    }

    public void insertNoteLabel(Label label) {
        noteRepository.insertNoteLabelCrossRef(
                databaseNote.getNoteId(),
                label.getLabelTitle()
        );
    }

    public void removeNoteLabel(Label label) {
        noteRepository.deleteNoteLabelCrossRef(
                databaseNote.getNoteId(),
                label.getLabelTitle()
        );
    }
}
