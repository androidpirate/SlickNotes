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

package com.github.androidpirate.slicknotes.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.room.TypeConverter;

public class Converters {
    private static final String COMMA_SEPARATOR = ",";

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
    @TypeConverter
    public static List<String> fromStringToLabelsList(String labels) {
        List<String> labelsList = new ArrayList<>();
        if (labels != null) {
            String[] labelsArray = labels.split(COMMA_SEPARATOR);
            labelsList = Arrays.asList(labelsArray);
        }
        return labelsList;
    }
    @TypeConverter
    public static String labelsListToString(List<String> labels) {
        String labelsList = "";
        for (String label:
             labels) {
            labelsList = labelsList.concat(label + COMMA_SEPARATOR);
        }
        return labelsList;
    }
}
