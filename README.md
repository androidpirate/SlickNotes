# Slicknotes

Slicknotes is a Java-based note-taking app for Android which is inspired by Google's Keep Notes.
The app uses several Android Architecture Component APIs and a clean domain layer to provide a smooth experience.

# App Architecture

This project uses the Model-View-ViewModel architecture pattern.
The repository is used as a single source of data even though the app only uses a local database in the current version. View models are created and provided to Views via a factory that uses a service locator to inject repository instances. View models observe the latest changes in the data and updates the UI accordingly.

# Front-End Features

## Note List

Displays a list of existing notes using a RecyclerView. Extends a base class **BaseNoteListFragment** which provides basic functionality for displaying a list of items by inheritance.

Data is observed in the LiveData format by **NoteListViewModel** and is displayed by the **NoteListAdapter**. Any user interaction over data is handled by **NoteListViewModel** and the results are observed immediately.

**NoteListAdapter** is responsible for displaying proper header and data items in the list and used to handle mass selection operations in the list.

### Key features:

- Switch between list and grid views
- Pin important notes on top of the list
- Display notes in ascending/descending order based on the date of creation
- Allow mass selection and editing

### What would be cooler?

- Implement a real SearchView interface to search keywords in both note and label databases and filtering the notes simultaneously.
- Implement drag and drop functionality to change the order of notes.
- Implement adding labels to multiple notes at the same time.

## Create Note

Displays an interface to create a new note. Extends a base class **BaseEditableNoteFragment**.

Data operations such as pinning the note, updating note background color, or insertion of data into the database is handled by **NoteCreateViewModel**.

### Key features:

- Discard empty note on navigating to list view
- Insert new note to the database
- Update pin status of the note
- Update background color of the note

### What would be cooler?

- Implement adding labels to the note when creating it

## Note Details

Displays details of existing notes and allows users to edit/delete note details such as note content, adding/removing labels, pin status, etc. Extends a base class **NaseEditableNoteFragment** which provides basic functionality for CRUD(Create, Read, Update, Delete) operations by inheritance.

Data is observed in LiveData format by **NoteDetailViewModel** and any interaction for updating data is also handled by the view model.

### Key features:

- Display note details
- Edit note details
- Send the note to trash
- Assign/update note background-color
- Add/remove/search note labels
- Update pin status
- Set reminders for note
- Share note

## Trash

Displays a list of notes that are sent to trash but not yet deleted completely of the database. One of the main functionality of this fragment is that it gives the user to restore the note before it is deleted forever. Extends a base class **BaseNoteListFragment** which provides basic functionality for displaying a list of items by inheritance.

**NoteListAdapter** is responsible for displaying proper data in the list and used to handle mass selection operations in the list.

### **Key features:**

- Restore notes
- Delete notes forever
- Allow mass selection and editing

### What would be cooler?

- Implement an option to let the trash to clear out its contents within a given interval

## Label List

Displays a list of labels in the database and provides CRUD functionality to add/edit labels.

Label**ListAdapter** is responsible for label items in the list and used to handle overflow menu actions.

### Key features:

- Display a list of labels in the database
- Add/edit labels

## Base Classes and Inheritance

All the above mentioned fragment classes are either extends **BaseNoteListFragment** or **BaseEditableNoteFragment.**

The reason behind having those fragments to have base classes was to make them inherit the common functions that are shared between fragments that represent a list and detail view.

The advantage of this kind of implementation is to keep individual fragment class code pretty clean and straight forward.

- The disadvantage is to make the base class to handle most UI related logic and sometimes requires to distinguish between its child classes to handle a situation-specific to its child fragment.

## Settings Activity and Fragment

Displays app settings and uses preferences to store them.

Since app settings are not related to any other front-end features I would like to keep it as a stand-alone entity that has its activity and fragment life-cycles that are handled separately from the rest of the app.

### **Key features:**

- Switch between light/dark theme
- Change the order of notes displayed by creation date
- Change text size used in notes

---

# Back-End Features

## Model

Consist of POJOs, getters, and setters.

## DAO

Interfaces consist of method signatures that are used to perform database operations on particular model objects.

## Database

Using the Room API database consists of three tables, notes, labels, and a junction table to provide a many to many relationship between them.

## Repository

Handles database queries and CRUD functions on the database for particular model objects.

# App Navigation

Entire app navigation is handled by the Navigation Component API.

---

# **License**

Copyright 2020, The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.
