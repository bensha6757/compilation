package IR;

import java.util.Objects;

public class Interference_Node {
    int number;
    public boolean inStack;
    public int color;

    public Interference_Node(int number) {
        this.number = number;
        inStack = false;
        color = -1;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Interference_Node that = (Interference_Node) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
