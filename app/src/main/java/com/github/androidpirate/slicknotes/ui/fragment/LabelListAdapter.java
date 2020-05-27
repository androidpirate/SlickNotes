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
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Label;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LabelListAdapter extends RecyclerView.Adapter<LabelListAdapter.LabelHolder> {
    private static final int EMPTY_LIST_SIZE = 0;

    private List<Label> labels;
    private ArrayList<String> noteLabels;

    public LabelListAdapter(List<Label> labels) {
        this.labels = labels;
    }

    @NonNull
    @Override
    public LabelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.label_list_item, parent, false);
        return new LabelHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LabelHolder holder, int position) {
        holder.onBindLabel(labels.get(position));
    }

    @Override
    public int getItemCount() {
        if(labels == null || labels.size() == 0) {
            return EMPTY_LIST_SIZE;
        }
        return labels.size();
    }

    void loadLabels(List<Label> labels) {
        this.labels = labels;
        notifyDataSetChanged();
    }

    void loadNoteLabels(ArrayList<String> noteLabels) {
        this.noteLabels = noteLabels;
    }

    class LabelHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private CheckBox checkBox;

        public LabelHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_label_title);
            checkBox = itemView.findViewById(R.id.cb_label);
        }

        private void onBindLabel(Label label) {
            title.setText(label.getLabelTitle());
            checkNoteLabel(label);
        }

        private void checkNoteLabel(Label label) {
            if(noteLabels.contains(label.getLabelTitle())) {
                checkBox.setChecked(true);
            }
        }
    }
}
