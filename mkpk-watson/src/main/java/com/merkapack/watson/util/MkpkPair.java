package com.merkapack.watson.util;

import java.io.Serializable;

public class MkpkPair<L, R> implements Serializable {

	private static final long serialVersionUID = 9208539290697311361L;

	private L l;
	private R r;
	
    public static <L, R> MkpkPair<L, R> of(L left, R right) {
        return new MkpkPair<L, R>(left, right);
    }

    public MkpkPair(L l, R r) {
    	this.l = l;	
    	this.r = r;
    }

    public L getLeft() {
    	return l;
    }
    public R getRight() {
    	return r;
    }

    public final L getKey() {
        return getLeft();
    }
    public R getValue() {
        return getRight();
    }
}
