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
package com.vaadin.sass.internal.visitor;

import java.io.Serializable;
import java.util.Collection;

import com.vaadin.sass.internal.selector.Selector;
import com.vaadin.sass.internal.selector.SimpleSelectorSequence;

/**
 * An immutable data object for an @extend, describing the mapping from the
 * parameter of @extend to the selector of the block containing the @extend.
 */
public class Extension implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * The parameter of @extend, e.g. "b" in "a { @extend b; ... }".
     * 
     * This is the selector whose occurrences will be augmented with new entries
     * generated by replacing {@link #extendSelector} with
     * {@link #replacingSelectors}.
     */
    public final SimpleSelectorSequence extendSelector;

    /**
     * A selectors that is extended by {@link #extendSelector}, e.g. "a" in
     * "a { @extend b; ... }".
     */
    public final Selector replacingSelector;

    /**
     * The context of the extending selector. This is used to handle @extend in
     * nested blocks correctly.
     * 
     * Can be null for empty context.
     */
    public final Collection<Selector> context;

    public Extension(SimpleSelectorSequence extendSelector,
            Selector replacingSelector, Collection<Selector> context) {
        this.extendSelector = extendSelector;
        this.replacingSelector = replacingSelector;
        this.context = context;
    }

    @Override
    public String toString() {
        return "(" + String.valueOf(replacingSelector) + " { @extend "
                + extendSelector + "; } in context " + context + ")";
    }

}