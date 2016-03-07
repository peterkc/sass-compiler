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
package com.vaadin.sass.internal.parser.function;

import com.vaadin.sass.internal.ScssContext;
import com.vaadin.sass.internal.parser.ActualArgumentList;
import com.vaadin.sass.internal.parser.FormalArgumentList;
import com.vaadin.sass.internal.parser.LexicalUnitImpl;
import com.vaadin.sass.internal.parser.ParseException;
import com.vaadin.sass.internal.parser.SassListItem;

public class CallFunctionGenerator extends AbstractFunctionGenerator {

    private static String[] argumentNames = { "name", "args" };

    public CallFunctionGenerator() {
        super(createArgumentList(argumentNames, true), "call");
    }

    @Override
    protected SassListItem computeForArgumentList(LexicalUnitImpl function, FormalArgumentList actualArguments) {

//        SassListItem name = getParam(actualArguments, "name");
//        SassListItem args = getParam(actualArguments, "args");

        //System.err.println(String.format("%s %s", name.printState(), args.printState()));

        return function;
    }

    @Override
    public SassListItem compute(ScssContext context, LexicalUnitImpl function) {
        ActualArgumentList args = function.getParameterList();
        FormalArgumentList functionArguments;
        try {
            functionArguments = getArguments().replaceFormalArguments(args, checkForUnsetParameters());
        } catch (ParseException e) {
            throw new ParseException("Error in parameters of function "
                    + function.getFunctionName() + "(), line "
                    + function.getLineNumber() + ", column "
                    + function.getColumnNumber() + ": [" + e.getMessage() + "]");
        }

//        System.err.println(String.format("%s %s", function.printState(), functionArguments.getArguments()));

        SassListItem callListItem = getParam(functionArguments, "name");


//        System.err.println(callArgs.printState());

        ActualArgumentList actualArguments = new ActualArgumentList(null, getParam(functionArguments, "args"));

        LexicalUnitImpl copied = function.copy();
        copied.setParameterList(actualArguments);
        copied.setFunctionName(callListItem.toString());

        String functionName = callListItem.printState();

        SCSSFunctionGenerator generator = LexicalUnitImpl.getGenerator(functionName);
//        FunctionDefNode fDef = context.getFunctionDefinition(callListItem.printState());
//        MixinDefNode mDef = context.getMixinDefinition(callListItem.printState());

//        System.err.println(generator.getFunctionNames());
//        System.err.println(fDef.printState());
//        System.err.println(mDef.printState());


        if (generator != null) {
            System.err.println(generator.getFunctionNames().length + functionName);
            return generator.compute(context, copied);
        }





        return function;
    }
}
