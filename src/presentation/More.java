package presentation;

/*
 * @(#)More.java	
 *
 * Copyright (c) 1996 Andrei Grigoriev. All Rights Reserved.
 *
 */

/**
 * A structure to keep data on "About" calls.
 *
 * Values normally read form html file.
 *
 * @version 	11 Feb 1997
 * @author 	Andrei Grigoriev
 */

public class More {
    /**
     * Call name.
     */
  String name;
    /**
     * Call format.
     */
  String call;
    /**
     * URLencode flag - for arguments.
     */
  boolean encode;

    /**
     * Constructs a call.
     */
  More(String call, String name, boolean encode){
    this.call = call;
    this.name = name;
    this.encode = encode;
  }

    /**
     * Converts to String.
     */
  public String toString(){
	return("name "+name+" call "+call+" encode "+ encode);
  }

}

