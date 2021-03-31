package imstrument.globals;

import javax.swing.*;
import java.awt.*;
import java.lang.invoke.WrongMethodTypeException;

public class WrapperCardLayout extends CardLayout {
    CardId currentCardId;
    public enum CardId{
        /* enum values*/
        HOMEPAGE("Homepage"),
        IMAGEPAGE("Imagepage");

        /* enumer logic */
        private final String text;
        CardId(final String text){
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public WrapperCardLayout(){
        super();
    }

    public void show(Container parent, CardId cardId) {
        super.show(parent, cardId.toString());
        this.currentCardId = cardId;
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if(constraints instanceof CardId){
            this.currentCardId = (CardId) constraints;
            super.addLayoutComponent(comp, this.currentCardId.toString());
        }
        else{
            throw new IllegalArgumentException("The passed constraints isn't an instance of CardId");
        }
    }

    public CardId getCurrentCardId() {
        return currentCardId;
    }
}
