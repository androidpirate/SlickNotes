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

package com.github.androidpirate.slicknotes;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.github.androidpirate.slicknotes.data.Label;
import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.data.NoteDao;
import com.github.androidpirate.slicknotes.data.NoteDatabase;
import com.github.androidpirate.slicknotes.data.NoteLabelCrossRef;
import com.github.androidpirate.slicknotes.data.NoteWithLabels;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NotesWithLabelsTest {

    private NoteDatabase db;
    private NoteDao dao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, NoteDatabase.class).build();
        dao = db.dao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void writeAndReadNotesWithLabels() throws Exception {

        // create some labels ...
        Label label1 = new Label("one");
        Label label2 = new Label("two");
        Label label3 = new Label("three");

        // ... and save them to the database
        int label1Id = new Long(dao.insertLabel(label1)).intValue();
        int label2Id = new Long(dao.insertLabel(label2)).intValue();
        int label3Id = new Long(dao.insertLabel(label3)).intValue();

        // create some notes ...
        Note noteA = new Note("Note A", "This is A note.", new Date());
        Note noteB = new Note("Note B", "This is B note.", new Date());

        // ... and save them to the database
        int noteAId = new Long(dao.insertDatabaseNote(noteA)).intValue();
        int noteBId = new Long(dao.insertDatabaseNote(noteB)).intValue();

        // now, we can associate notes with labels:
        //  noteA with labels "one" and "two"
        //  noteB with labels "two" and "three"
        dao.insertNoteLabelCrossRef(new NoteLabelCrossRef(noteAId, label1Id));
        dao.insertNoteLabelCrossRef(new NoteLabelCrossRef(noteAId, label2Id));
        dao.insertNoteLabelCrossRef(new NoteLabelCrossRef(noteBId, label2Id));
        dao.insertNoteLabelCrossRef(new NoteLabelCrossRef(noteBId, label3Id));

        // query for all notes with their labels
        List<NoteWithLabels> notesWithLabels = dao.getAllNotesWithLabels();

        assertEquals("There should be 2 notes", 2, notesWithLabels.size());

        assertEquals("First note should be Note A", noteAId, notesWithLabels.get(0).getNoteId());
        assertEquals("First note should have 2 labels", 2, notesWithLabels.get(0).getLabels().size());
        assertEquals("First note should have label 'one'", label1Id, notesWithLabels.get(0).getLabels().get(0).getLabelId());
        assertEquals("First note should have label 'two'", label2Id, notesWithLabels.get(0).getLabels().get(1).getLabelId());

        assertEquals("Second note should be Note B", noteBId, notesWithLabels.get(1).getNote().getNoteId());
        assertEquals("Second note should have 2 labels", 2, notesWithLabels.get(1).getLabels().size());
        assertEquals("Second note should have label 'two'", label2Id, notesWithLabels.get(1).getLabels().get(0).getLabelId());
        assertEquals("Second note should have label 'three'", label3Id, notesWithLabels.get(1).getLabels().get(1).getLabelId());
    }
}
