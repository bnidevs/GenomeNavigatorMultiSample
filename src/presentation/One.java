package presentation;

/*
 * @(#)One.java	
 *
 * Copyright (c) 1996 Andrei Grigoriev. All Rights Reserved.
 *
 */

/**
 * A basic element.
 *
 * Values normally read form a datafile.
 *
 * @see Stripe
 * @version 	03 Dec 1996
 * @author 	Andrei Grigoriev
 */

public class One {
	int areaFrom;
	int areaTo;

    /**
     * Start coordinate (datafile units).
     */
  int from;
    /**
     * End coordinate (datafile units).
     */
  int  to;
    /**
     * ID (for external ref).
     */
  String  id;
    /**
     * X coordinate (pixels). Not used
     */
  int x;
    /**
     * Y coordinate (pixels) - element top relative to stripe top.
     */
  int y;
    /**
     * Element name.
     */
  String name;
    /**
     * Element type.
     */
  String type;
	
  java.awt.Color color;
    /**
     * Placement flag - for arrangement purposes.
     */
  boolean placed;

    /**
     * Constructs an empty element.
     */
  One(){
    placed = false;
  }

    /**
     * Converts to String.
     */
  public String toString(){
	return("name "+name+" from "+ from+" to "+to);
  }

}

