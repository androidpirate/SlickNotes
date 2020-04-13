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

package com.github.androidpirate.slicknotes.util;

import com.github.androidpirate.slicknotes.data.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FakeData {

    public static List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note("Note 1", "Note details", new Date()));
        notes.add(new Note("Note 2", "Note details", new Date()));
        notes.add(new Note("Note 3", "Note details", new Date()));
        notes.add(new Note("Note 4", "Note details", new Date()));
        notes.add(new Note("Note 5", "Note details", new Date()));
        notes.add(new Note("Note 6", "Note details", new Date()));
        notes.add(new Note("Note 7", "Note details", new Date()));
        notes.add(new Note("Note 8", "Note details", new Date()));
        notes.add(new Note("Note 9", "Note details", new Date()));
        notes.add(new Note("Note 10", "Note details", new Date()));
        return notes;
    }
}
