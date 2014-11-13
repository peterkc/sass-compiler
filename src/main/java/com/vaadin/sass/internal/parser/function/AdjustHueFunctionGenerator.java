package com.vaadin.sass.internal.parser.function;

import com.vaadin.sass.internal.parser.FormalArgumentList;
import com.vaadin.sass.internal.parser.LexicalUnitImpl;
import com.vaadin.sass.internal.parser.SassListItem;
import com.vaadin.sass.internal.util.ColorUtil;

/**
 * Created by y6935 on 13.11.2014
 */
public class AdjustHueFunctionGenerator extends AbstractFunctionGenerator {

    private static String[] argumentNames = { "color", "amount" };

    public AdjustHueFunctionGenerator() {
        super(createArgumentList(argumentNames, false), "adjust-hue");
    }

    @Override
    protected SassListItem computeForArgumentList(LexicalUnitImpl function,
                                                  FormalArgumentList actualArguments) {
        LexicalUnitImpl color = getParam(actualArguments, "color")
                .getContainedValue();
        float amount = getParam(actualArguments, "amount").getContainedValue()
                .getFloatValue();
        return ColorUtil.adjustHue(color, amount);
    }
}
