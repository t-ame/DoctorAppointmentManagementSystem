package com.java;

import java.io.Serializable;

public class Tester implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1046525898100461127L;

	private Tester() {
		super();
	}

	protected Tester readResolve() {
		return getSingle();
	}

	private static class InnerTest {
		private static final Tester INSTANCE = new Tester();
	}

	public static Tester getSingle() {
		return InnerTest.INSTANCE;
	}

}
