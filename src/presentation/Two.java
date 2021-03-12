package presentation;

/*
 * @(#)One.java	
 *
 * Copyright (c) 1996 Andrei Grigoriev. All Rights Reserved.
 *
 */

/**
 * A selected element plus stripe index.
 *
 * @see One
 * @see Stripe
 * @version 	12 Feb 1997
 * @author 	Andrei Grigoriev
 */

public class Two {
    /**
     * Element.
     */
   One one;
    /**
     * Stripe Index.
     */
  int stripeIndex;

    /**
     * Constructs an element from One.
     */
  Two(One one){
    this.one=one;
  }
    /**
     * Constructs an element from One and stripe index.
     */
  Two(One one, int index){
    this(one);
    stripeIndex = index;
  }

}

