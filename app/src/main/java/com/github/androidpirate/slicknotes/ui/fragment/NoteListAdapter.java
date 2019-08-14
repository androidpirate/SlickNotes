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

package com.github.androidpirate.slicknotes.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Note;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListHolder> {
    private static final int EMPTY_LIST_SIZE = 0;
    private static final int MINIMUM_INDEX_NO = 0;
    private List<Note> notes;
    private List<Integer> selectedNoteIds;
    private NoteClickListener listener;

    interface NoteClickListener {
        void onNoteClick(int noteId);
        void onLongNoteClick(Note note, boolean isAdded);
    }

    public NoteListAdapter(List<Note> notes, NoteClickListener listener) {
        this.notes = notes;
        this.selectedNoteIds = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_item, parent, false);
        return new NoteListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListHolder holder, int position) {
        holder.bindNote(notes.get(position));
    }

    @Override
    public int getItemCount() {
        if(notes != null) {
            return notes.size();
        }
        return 0;
    }

    void loadNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    void loadSelectedNoteIds(List<Integer> selectedNoteIds) {
        this.selectedNoteIds = selectedNoteIds;
    }

    class NoteListHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView details;
        private FrameLayout cardBorder;

        public NoteListHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            details = itemView.findViewById(R.id.tv_details);
            cardBorder = itemView.findViewById(R.id.card_view_border);
        }

        private void bindNote(final Note note) {
            title.setText(note.getTitle());
            details.setText(note.getDetails());
            if(selectedNoteIds.size() != EMPTY_LIST_SIZE) {
                checkCardIsSelected(note.getNoteId());
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onNoteClick(note.getNoteId());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(cardBorder.getVisibility() == View.GONE) {
                        cardBorder.setVisibility(View.VISIBLE);
                        listener.onLongNoteClick(note, true);
                    } else {
                        cardBorder.setVisibility(View.GONE);
                        listener.onLongNoteClick(note, false);
                    }
                    return true;
                }
            });
        }

        private void checkCardIsSelected(int noteId) {
            int result = Arrays.binarySearch(selectedNoteIds.toArray(), noteId);
            if(result >= MINIMUM_INDEX_NO) {
                cardBorder.setVisibility(View.VISIBLE);
            }
        }
    }
}
