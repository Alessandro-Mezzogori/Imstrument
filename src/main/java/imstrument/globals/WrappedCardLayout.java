package imstrument.globals;

import javax.swing.*;
import java.awt.*;
import java.lang.invoke.WrongMethodTypeException;

/**
 * this classes is a wrapper of CardLayout for easier manipulation of active card
 * especially from inside the card itself
 */
public class WrappedCardLayout extends CardLayout {
    /**
     * CardName of the current showing card in the parent container
     */
    String currentCardName;

    /**
     * contains a a set of attributes for easier change of card name
     */
    public static class CardNames{
        public static final String HOMEPAGE = "Homepage";
        public static final String IMAGEPAGE = "Imagepage";
    }

    public WrappedCardLayout(){
        super();
    }

    @Override
    public void show(Container parent, String name) {
        super.show(parent, name);
        this.currentCardName = name;
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if(constraints instanceof String){
            this.currentCardName = constraints.toString();
            super.addLayoutComponent(comp, this.currentCardName);
        }
        else{
            throw new IllegalArgumentException("The passed constraints isn't an instance of String");
        }
    }

    /**
     * returns name of current showing card in the parent container
     * @return String
     */
    public String getCurrentCardName() {
        return this.currentCardName;
    }
}
