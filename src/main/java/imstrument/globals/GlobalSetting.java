package imstrument.globals;

public class GlobalSetting {
    public enum CardId{
        HOMEPAGE("Homepage"),
        IMAGEPAGE("Imagepage");

        private final String text;
        CardId(final String text){
            this.text = text;
        }


        @Override
        public String toString() {
            return text;
        }
    }
}
