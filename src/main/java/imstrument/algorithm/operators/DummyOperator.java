package imstrument.algorithm.operators;

import java.awt.*;

public class DummyOperator implements Operator{
    public static String name = "Dummy operator";
    @Override
    public double compute(Color[] pixels) {
        return 0;
    }

    @Override
    public String getName() {
        return name;
    }
}
