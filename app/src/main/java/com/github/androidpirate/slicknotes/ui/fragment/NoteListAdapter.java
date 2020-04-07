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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class NoteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int EMPTY_LIST_SIZE = 0;
    private static final int MINIMUM_INDEX_NO = 0;
    private static final int TYPE_CARD = 100;
    private static final int TYPE_HEADER = 110;
    private static final String PINNED_HEADER_TAG = "PINNED";
    private static final String OTHERS_HEADER_TAG = "OTHERS";

    private List<Note> notes;
    private List<Object> contentList;
    private List<Integer> selectedNoteIds;
    private boolean isLinearLayout = true;
    private NoteClickListener listener;

    interface NoteClickListener {
        void onNoteClick(int noteId, boolean notePinStatus, int position);
        void onLongNoteClick(Note note, boolean isAdded);
    }

    NoteListAdapter(List<Note> notes, NoteClickListener listener) {
        this.notes = notes;
        this.listener = listener;
        selectedNoteIds = new ArrayList<>();
        contentList = new ArrayList<>();
        initializeContentList();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;
        if (viewType == TYPE_CARD) {
            if(isLinearLayout) {
                itemView = inflater.inflate(R.layout.note_list_item, parent, false);
            } else {
                itemView = inflater.inflate(R.layout.note_grid_item, parent, false);
            }
            return new NoteCardHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            itemView = inflater.inflate(R.layout.note_list_header, parent, false);
            if(!isLinearLayout) {
                StaggeredGridLayoutManager.LayoutParams layoutParams =
                        (StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams();
                layoutParams.setFullSpan(true);
            }
            return new NoteListHeaderHolder(itemView);
        }
        throw new IllegalArgumentException("Unsupported view type is used as an argument");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NoteCardHolder) {
            NoteCardHolder h = (NoteCardHolder) holder;
            h.bindNote((Note) contentList.get(position));
        } else if (holder instanceof NoteListHeaderHolder) {
            NoteListHeaderHolder h = (NoteListHeaderHolder) holder;
            h.setListHeader((String) contentList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if(contentList != null) {
            return contentList.size();
        }
        return EMPTY_LIST_SIZE;
    }

    @Override
    public int getItemViewType(int position) {
        if(contentList.get(position) instanceof Note) {
            return TYPE_CARD;
        } else if (contentList.get(position) instanceof String) {
            return TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }

    void loadNotes(List<Note> notes) {
        this.notes = notes;
        initializeContentList();
        notifyDataSetChanged();
    }

    void loadSelectedNoteIds(List<Integer> selectedNoteIds) {
        this.selectedNoteIds = selectedNoteIds;
    }

    void setLayoutStyle(boolean linearLayout) {
        isLinearLayout = linearLayout;
    }

    private void initializeContentList() {
        if(contentList.size() != 0) {
            contentList.clear();
        }
        boolean isPinnedHeaderCreated = false;
        boolean isOthersHeaderCreated = false;
        for (Note note: notes) {
            if(note.isPinned() && !isPinnedHeaderCreated ) {
                    contentList.add(PINNED_HEADER_TAG);
                    isPinnedHeaderCreated = true;
            } else if (isPinnedHeaderCreated && !note.isPinned() && !isOthersHeaderCreated) {
                    contentList.add(OTHERS_HEADER_TAG);
                    isOthersHeaderCreated = true;
            }
            contentList.add(note);
        }
    }

    class NoteCardHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView details;
        private FrameLayout cardBorder;
        private CardView cardView;

        NoteCardHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            details = itemView.findViewById(R.id.tv_details);
            cardBorder = itemView.findViewById(R.id.card_view_border);
            cardView = itemView.findViewById(R.id.card_view);
        }

        private void bindNote(final Note note) {
            // Set card background color
            setCardBackgroundColor(note.getColorId());
            // Set card fields
            title.setText(note.getTitle());
            details.setText(note.getDetails());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onNoteClick(note.getNoteId(), note.isPinned(), getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongNoteClick(note, switchCardBorderVisibility());
                    return true;
                }
            });
        }

        private boolean switchCardBorderVisibility() {
            if(cardBorder.getVisibility() == View.GONE) {
                cardBorder.setVisibility(View.VISIBLE);
                return true;
            } else {
                cardBorder.setVisibility(View.GONE);
                return false;
            }
        }

        private void setCardBackgroundColor(int colorId) {
            cardView.setBackgroundColor(colorId);
        }
    }

    class NoteListHeaderHolder extends RecyclerView.ViewHolder {
        private TextView listHeader;

        NoteListHeaderHolder(@NonNull View itemView) {
            super(itemView);
            listHeader = itemView.findViewById(R.id.tv_list_header);
        }

        private void setListHeader(String header) {
            listHeader.setText(header);
        }
    }

}
