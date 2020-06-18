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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Label;

import java.util.List;

public class LabelListAdapter extends RecyclerView.Adapter<LabelListAdapter.LabelHolder> {
    private static final int EMPTY_LIST_SIZE = 0;
    private List<Label> labels;
    private OnLabelClickListener listener;

    interface OnLabelClickListener {
        void onEditLabelClick(Label label);
        void onDeleteLabelClick(Label label);
    }

    LabelListAdapter(List<Label> labels, OnLabelClickListener listener) {
        this.labels = labels;
        this.listener = listener;
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

    class LabelHolder extends RecyclerView.ViewHolder {
        private TextView labelTitle;
        private ImageView optionsMenu;

        LabelHolder(@NonNull View itemView) {
            super(itemView);
            labelTitle = itemView.findViewById(R.id.tv_label_title);
            optionsMenu = itemView.findViewById(R.id.iv_options);
        }

        void onBindLabel(final Label label) {
            labelTitle.setText(label.getLabelTitle());
            optionsMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setupPopUpMenu(label);
                }
            });
        }

        private void setupPopUpMenu(final Label label) {
            PopupMenu popupMenu = new PopupMenu(itemView.getContext(), optionsMenu);
            popupMenu.inflate(R.menu.label_list_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_edit:
                            listener.onEditLabelClick(label);
                            return true;
                        case R.id.action_delete:
                            listener.onDeleteLabelClick(label);
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popupMenu.show();
        }
    }
}
