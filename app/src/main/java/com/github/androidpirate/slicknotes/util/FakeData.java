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

import com.github.androidpirate.slicknotes.data.Label;
import com.github.androidpirate.slicknotes.data.Note;
import com.github.androidpirate.slicknotes.data.NoteWithLabels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Provides fake data for debugging.
 */
public class FakeData {
    public static List<NoteWithLabels> getNotes() {
        List<NoteWithLabels> notes = new ArrayList<>();
        List<Label> labels = new ArrayList<>(Arrays.asList(
                new Label("groceries"), new Label("band"), new Label("misc"),
                new Label("business"), new Label("art"), new Label("productivity"),
                new Label("party")));

        notes.add(new NoteWithLabels(
                new Note("Grocery List",
                "-Eggs\n-Bread\n-Milk\n-Butter\n-Bell peppers\n-Tomatoes\n-Sweet potatoes" +
                        "\n-Toilet paper\n-Dish soap\n-Tooth paste\n-Shampoo",
                new Date()),
                new ArrayList<>(Collections.singletonList(labels.get(0)))));
        notes.add(new NoteWithLabels(
                new Note( "Band Practice @Studio Live",
                "Songs:\n-Machine Head(Bush)\n-King Nothing(Metallica)\n-Creep(Radiohead)\n" +
                        "-Song2(Blur)\n-Erase n Rewind(The Cardigans)\n-Bohemian Rhapsody(Queen)\n" +
                        "-American Woman(Lenny Kravitz)\n-Wish You Were Here(Pink Floyd)",
                new Date()),
                new ArrayList<>(Arrays.asList(labels.get(1), labels.get(4)))));
        notes.add(new NoteWithLabels(
                new Note(
                "A Note To Pin",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                        "tempor incididunt ut labore et dolore magna aliqua. Est ullamcorper eget" +
                        " nulla facilisi etiam dignissim diam quis enim. Cursus euismod quis " +
                        "viverra nibh cras pulvinar mattis. Eu tincidunt tortor aliquam nulla " +
                        "facilisi cras. At erat pellentesque adipiscing commodo elit.",
                new Date()),
                new ArrayList<>(Arrays.asList(labels.get(2), labels.get(5)))));
        notes.add(new NoteWithLabels(
                new Note(
                "A Note To Trash",
                "Rhoncus dolor purus non enim praesent elementum facilisis. In nisl nisi\n\n" +
                        "Scelerisque eu ultrices vitae auctor eu augue. Risus in hendrerit gravida" +
                        " rutrum quisque non tellus. Mauris cursus mattis molestie a.\n" +
                        "Risus in hendrerit gravida rutrum quisque. Vitae et leo duis ut diam " +
                        "quam nulla porttitor. Id venenatis a condimentum vitae.",
                new Date()),
                new ArrayList<>(Collections.singletonList(labels.get(2)))));
        notes.add(new NoteWithLabels(
                new Note(
                "Mom's Bday Pary",
                "Netus et malesuada fames ac turpis egestas maecenas pharetra convallis." +
                        " Vestibulum lectus mauris ultrices eros. Nunc aliquet bibendum enim " +
                        "facilisis gravida neque convallis a. Diam phasellus vestibulum lorem sed" +
                        " risus. Iaculis nunc sed augue lacus viverra vitae congue.",
                new Date()),
                new ArrayList<>(Arrays.asList(labels.get(3), labels.get(6)))));
        notes.add(new NoteWithLabels(
                new Note(
                "A Note To Self",
                "Love yourself!",
                new Date()),
                new ArrayList<>(Collections.singletonList(labels.get(2)))));
        notes.add(new NoteWithLabels(
                new Note(
                "By Order of The Peaky Blinders",
                "Peaky Blinders is a gangster family epic set in Birmingham, England, in 1919" +
                        ", several months after the end of the First World War in November 1918. " +
                        "The story centres on the Peaky Blinders gang and their ambitious and " +
                        "highly cunning boss Tommy Shelby. The gang comes to the " +
                        "attention of Major Chester Campbell, a Detective Chief " +
                        "Inspector in the Royal Irish Constabulary(R.I.C.) sent over by Winston " +
                        "Churchill from Belfast, where he had been sent to clean up the city of the" +
                        " Irish Republican Army(I.R.A.), Communists, gangs and common criminals." +
                        "Winston Churchill charged him with suppressing disorder and uprising " +
                        "in Birmingham and recovering a stolen cache of arms meant to be shipped " +
                        "to Libya.The first series concludes on 3 December 1919 – " +
                        "\"Black Star Day\", the event where the Peaky Blinders plan to take over " +
                        "Billy Kimber's betting pitches at the Worcester Races.\n" +
                        "\n" +
                        "The second series sees the Shelby family expand their criminal organisation" +
                        " in the \"South and North while maintaining a stronghold in their " +
                        "Birmingham heartland.\"It begins in 1921 and ends with a climax at " +
                        "Epsom racecourse on 31 May 1922 – Derby Day.\n" +
                        "\n" +
                        "The third series starts and ends in 1924 as it follows Tommy and his family" +
                        " entering an even more dangerous world as they once again expand, " +
                        "this time internationally. The third series also features Father " +
                        "John Hughes, who is involved in an anti-communist " +
                        "organization; Ruben Oliver, a painter whom Polly " +
                        "enlists to paint her portrait; Russian Duchess Tatiana Petrovna " +
                        "; and Linda Shelby, new wife of Arthur.\n" +
                        "\n" +
                        "The fourth series begins on Christmas Eve 1925 and ends following the " +
                        "general strike of May 1926 with Tommy being elected as a Member of " +
                        "Parliament in 1927. The fifth series begins two years later on 29 October" +
                        " 1929(Black Tuesday) and ends on 7 December 1929, the morning after a " +
                        "rally led by fascist leader Oswald Mosley.",
                new Date()),
                new ArrayList<>(Arrays.asList(labels.get(2), labels.get(4)))));
        notes.add(new NoteWithLabels(
                new Note(
                "Bank Appointment",
                "-Check accounts status\n\n-Questions to ask:\n" +
                        "-Min amount to leave in savings account?\n" +
                        "-Min interest rates on mortgage?\n" +
                        "-Investment plans? Stocks, bonds"
                , new Date()),
                new ArrayList<>(Collections.singletonList(labels.get(3)))));
        notes.add(new NoteWithLabels(
                new Note(
                "Self Destructive Note",
                "This note will destruct itself in\n" +
                        "9...\n8..\n7..\n6...\n\n5..\n4..\n3..\n\n2..\n\n1...",
                new Date()),
                new ArrayList<>(Collections.singletonList(labels.get(2)))));
        notes.add(new NoteWithLabels(
                new Note("Note 10", "Note details",
                new Date()),
                new ArrayList<>(Collections.singletonList(labels.get(2)))));
        return notes;
    }
}