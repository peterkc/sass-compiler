/*
 * Copyright 2000-2014 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * (c) COPYRIGHT 1999 World Wide Web Consortium
 * (Massachusetts Institute of Technology, Institut National de Recherche
 *  en Informatique et en Automatique, Keio University).
 * All Rights Reserved. http://www.w3.org/Consortium/Legal/
 *
 * $Id: MediaListImpl.java,v 1.4 2000/04/26 13:40:19 plehegar Exp $
 */
package com.vaadin.sass.internal.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.w3c.css.sac.SACMediaList;

/**
 * @version $Revision: 1.4 $
 * @author Philippe Le Hegaret
 */
public class MediaListImpl implements SACMediaList, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<SassListItem> items = new ArrayList<SassListItem>();

    public List<SassListItem> getSassListItems() {
        return items;
    }

    public SassListItem getSassListItem(int index) {
        if ((index < 0) || (index >= getLength())) {
            return null;
        }
        return items.get(index);
    }

    @Override
    public int getLength() {
        return items.size();
    }

    @Override
    public String item(int index) {
        if ((index < 0) || (index >= getLength())) {
            return null;
        }
        return items.get(index).toString();
    }

    public void addItem(String medium) {
        if (medium.equals("all")) {
            items.clear();
        }
        addItem(new StringItem(medium));
    }

    public void addAllItems(Iterable<SassListItem> items) {
        for (SassListItem item : items) {
            addItem(item);
        }
    }

    public void addItem(SassListItem item) {
        if (!items.contains(item)) {
            items.add(item);
        }
    }

    /**
     * Returns a string representation of this object.
     */
    @Override
    public String toString() {
        switch (items.size()) {
        case 0:
            return "";
        case 1:
            return items.get(0).toString();
        default:
            boolean first = true;
            StringBuilder buffer = new StringBuilder(50);
            for (SassListItem item : items) {
                if (first) {
                    first = false;
                } else {
                    buffer.append(", ");
                }
                buffer.append(item.toString());
            }
            return buffer.toString();
        }
    }
}
