/*
 * MIT License
 *
 * Copyright (c) 2018 Andrei Dascalu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.andrei1058.discordpublicservers.customisation;

public enum Tag {
    GAMING, WEBSITE, LIFESTYLE, LANGUAGES, ANIME, MOVIES, SERVICES, PROGRAMMING, MUSIC, EDUCATION, YOUTUBE;

    public static String getDesc(Tag t) {
        String v = "";
        switch (t) {
            case GAMING:
                v = "Play games";
                break;
            case WEBSITE:
                v = "Website community";
                break;
            case LIFESTYLE:
                v = "Way of living";
                break;
            case LANGUAGES:
                v = "Learn languages";
                break;
            case ANIME:
                v = "Anime talk";
                break;
            case MOVIES:
                v = "Movies talk";
                break;
            case SERVICES:
                v = "Offering services";
                break;
            case PROGRAMMING:
                v = "Programming related";
                break;
            case MUSIC:
                v = "Music bot";
                break;
            case EDUCATION:
                v = "Learn something";
                break;
            case YOUTUBE:
                v = "Youtube channel";
                break;
        }
        return v;
    }
}
