package it.tiwiz.chargeHistory;

/**
 * Created by Roby on 20/12/13.
 */
public class SafeCast {

    public static final int long2int(long inputValue) throws IllegalArgumentException{

        if(inputValue < Integer.MIN_VALUE || inputValue > Integer.MAX_VALUE) throw new IllegalArgumentException();
        return (int) inputValue;

    }
}
