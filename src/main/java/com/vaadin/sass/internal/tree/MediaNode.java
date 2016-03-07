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

package com.vaadin.sass.internal.tree;

import com.vaadin.sass.internal.ScssContext;
import com.vaadin.sass.internal.parser.*;
import org.w3c.css.sac.SACMediaList;

import java.util.*;

public class MediaNode extends Node implements IVariableNode {
    private static final long serialVersionUID = 2502097081457509523L;

    MediaListImpl media;

    public MediaNode(MediaListImpl media) {
        super();
        this.media = media;
    }

    private MediaNode(MediaNode nodeToCopy) {
        super(nodeToCopy);
        media = nodeToCopy.media;
    }

    public SACMediaList getMedia() {
        return media;
    }

    public void setMedia(MediaListImpl media) {
        this.media = media;
    }

    @Override
    public String printState() {
        return buildString(PRINT_STRATEGY, true);
    }

    @Override
    public String toString() {
        return buildString(TO_STRING_STRATEGY, true);
    }

    @Override
    public Collection<Node> traverse(ScssContext context) {
        replaceVariables(context);
        traverseChildren(context);
        return Collections.singleton((Node) this);
    }

    private String buildString(BuildStringStrategy strategy, boolean indent) {
        StringBuilder builder = new StringBuilder("@media ");
        if (media != null) {
            for (int i = 0; i < media.getLength(); i++) {
                if (i > 0) {
					//builder.append(", ");
                }
                builder.append(media.getSassListItem(i).buildString(strategy));
            }
        }
        builder.append(" {\n");
        for (Node child : getChildren()) {
            builder.append('\t');
            if (child instanceof BlockNode) {
                if (PRINT_STRATEGY.equals(strategy)) {
                    builder.append(((BlockNode) child).buildString(indent));
                } else {
                    builder.append(strategy.build(child));

                }
            } else {
                builder.append(strategy.build(child));
            }
            builder.append('\n');
        }
        builder.append("}");
        return builder.toString();
    }

    public void wrapInBlockNode(BlockNode blockNode) {
        for (Node child : new ArrayList<Node>(getChildren())) {
            blockNode.appendChild(child);
        }
        appendChild(blockNode);
    }

    @Override
    public void replaceVariables(ScssContext context) {

        MediaListImpl newMedia = new MediaListImpl();
        for (SassListItem sassListItem : media.getSassListItems()) {
			StringInterpolationSequence is = new StringInterpolationSequence(
					Collections.singletonList(sassListItem));
			StringInterpolationSequence rs = is.replaceVariables(context);
			List<SassListItem> sl = rs.getItems();
			for (SassListItem it : sl) {
				it = it.replaceVariables(context);
				newMedia.addItem(it);
        }

			// newMedia.addAllItems(rs.getItems());
		}
        media = newMedia;
    }

    @Override
    public MediaNode copy() {
        return new MediaNode(this);
    }
}
