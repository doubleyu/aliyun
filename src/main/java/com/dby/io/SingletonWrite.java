package com.dby.io;

/**
 * Created by Admin on 2016/5/18.
 */
public class SingletonWrite {
    private static final SingletonWrite inst= new SingletonWrite();

    private SingletonWrite() {
        super();
    }

    public synchronized void writeToFile(String str) {
        // Do whatever
    }

    public SingletonWrite getInstance() {
        return inst;
    }

}
