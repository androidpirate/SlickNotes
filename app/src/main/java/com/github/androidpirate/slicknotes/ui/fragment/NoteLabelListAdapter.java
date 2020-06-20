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

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.github.androidpirate.slicknotes.R;
import com.github.androidpirate.slicknotes.data.Label;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteLabelListAdapter extends RecyclerView.Adapter<NoteLabelListAdapter.LabelHolder>
    implements Filterable {

    private static final int EMPTY_LIST_SIZE = 0;

    private List<Label> labels;
    private List<Label> labelsFull;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();
    private OnLabelClickListener listener;

    interface OnLabelClickListener {
        void onCheckBoxChecked(boolean isChecked, Label label);
    }

    NoteLabelListAdapter(List<Label> labels, OnLabelClickListener listener) {
        this.labels = labels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LabelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_label_list_item, parent, false);
        return new LabelHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LabelHolder holder, int position) {
        holder.onBindLabel(position);
    }

    @Override
    public int getItemCount() {
        if(labels == null || labels.size() == 0) {
            return EMPTY_LIST_SIZE;
        }
        return labels.size();
    }

    @Override
    public Filter getFilter() {
        return labelFilter;
    }

    private Filter labelFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Label> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(labelsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Label label : labelsFull) {
                    if (label.getLabelTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(label);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            labels.clear();
            labels.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    void loadLabels(List<Label> labels) {
        this.labels = labels;
        labelsFull = new ArrayList<>(this.labels);
    }

    void loadNoteLabels(ArrayList<String> noteLabels) {
        if(noteLabels != null) {
            for (int i = 0; i < labels.size(); i++) {
                for (String noteLabel : noteLabels) {
                    if (labels.get(i).getLabelTitle().equals(noteLabel)) {
                        itemStateArray.put(i, true);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    class LabelHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        private TextView title;
        private CheckBox checkBox;

        LabelHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_label_title);
            checkBox = itemView.findViewById(R.id.cb_label);
            itemView.setOnClickListener(this);
        }

        private void onBindLabel(int position) {
            title.setText(labels.get(position).getLabelTitle());
            // TODO: You can not trust adapter position !!!!
            int itemPosition = getLabelPosition(title.getText().toString());
            checkBox.setChecked(itemStateArray.get(itemPosition));
        }

        @Override
        public void onClick(View v) {
            // TODO: We can not trust adapter position!!!
            int itemPosition = getLabelPosition(title.getText().toString());
            if(!itemStateArray.get(itemPosition, false)) {
                checkBox.setChecked(true);
                itemStateArray.put(itemPosition, true);
            } else {
                checkBox.setChecked(false);
                itemStateArray.put(itemPosition, false);
            }
            listener.onCheckBoxChecked(checkBox.isChecked(), labelsFull.get(itemPosition));
        }

        private int getLabelPosition(String title) {
            int itemPosition = 0;
            for(int i = 0; i < labelsFull.size(); i++) {
                if(labelsFull.get(i).getLabelTitle().equals(title)) {
                    itemPosition = i;
                    break;
                }
            }
            return itemPosition;
        }
    }
}