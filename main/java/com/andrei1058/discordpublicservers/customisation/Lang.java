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

public enum Lang {
    NONE, ENGLISH, ROMANIAN, ITALIAN, SPANISH, FRENCH, RUSSIAN, GERMAN, POLISH, PORTUGUESE, BULGARIAN, HUNGARIAN, DUTCH, CROATIAN, CZECH, DANISH, ESTONIAN, FINNISH, GREEK,
    IRISH, LATVIAN, LITHUANIAN, MALTESE, SLOVAK, SLOVENIAN, SWEDISH;

    public static String getDesc(Lang l) {
        String v = "";
        switch (l) {
            case ENGLISH:
                v = "English";
                break;
            case ROMANIAN:
                v = "Română";
                break;
            case ITALIAN:
                v = "Italiano";
                break;
            case SPANISH:
                v = "Español";
                break;
            case FRENCH:
                v = "Français";
                break;
            case GERMAN:
                v = "Deutsche";
                break;
            case POLISH:
                v = "Polskie";
                break;
            case HUNGARIAN:
                v = "Magyar";
                break;
            case BULGARIAN:
                v = "български";
                break;
            case DUTCH:
                v = "Nederlands";
                break;
            case PORTUGUESE:
                v = "Português";
                break;
            case RUSSIAN:
                v = "Русский язык";
                break;
            case NONE:
                v = "Other";
                break;
            case CROATIAN:
                v = "Hrvatski";
                break;
            case CZECH:
                v = "Čeština";
                break;
            case GREEK:
                v = "Ελληνικά";
                break;
            case IRISH:
                v = "Gaeilge";
                break;
            case DANISH:
                v = "Dansk";
                break;
            case SLOVAK:
                v = "Slovák";
                break;
            case SWEDISH:
                v = "Svenska";
                break;
            case FINNISH:
                v = "Suomalainen";
                break;
            case LATVIAN:
                v = "Latviešu";
                break;
            case MALTESE:
                v = "Malti";
                break;
            case ESTONIAN:
                v = "Eesti keel";
            case SLOVENIAN:
                v = "Slovenščina";
                break;
            case LITHUANIAN:
                v = "Lietuviškai";
                break;
        }
        return v;
    }
}
