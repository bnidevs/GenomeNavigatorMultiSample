package presentation;

import java.awt.*;

/* elements give topX|bottomX pairs */

public class Compare extends Stripe{
  private int tip = 5;

  Compare(){
    objHeight = 25; objPad = 3;
  }

  Compare(int pieces, String name){
    numObj = pieces;
    piece = new One[numObj];
    this.name = name;
    objHeight = 25; objPad = 3;
  }

  /* nothing to arrange */

  public int arrangeObjects(int curY){
    setTop(curY);
    curY = objPad;
    for(int i=0;i<numObj;i++){
	piece[i].y = curY;
    }
    curY += objHeight + objPad;
    height = curY;
    return curY+top;
  }

  public void draw(Graphics g, aWindow win, Font font, One selected){
    /* only draw inside visible window */
    int offY = top-win.offY;
    if(offY > win.height-1 || offY + height < 1) return; // add sticky

    for(int i=0;i<numObj;i++){
      int from = (int)(win.scale*piece[i].from)-win.offX, to = (int)(win.scale*piece[i].to)-win.offX;
//System.out.println("1f "+from + " t "+to+" c "+piece[i].color);
//      if(to&from <= 0 || to&from >= win.width) continue; ?? 
//      int len = to-from;

      g.setColor(piece[i].color);
      g.drawLine(from,piece[i].y+offY,from,piece[i].y+offY+tip);
      g.drawLine(to,piece[i].y+offY+objHeight-tip,to,piece[i].y+offY+objHeight);
      g.drawLine(from,piece[i].y+offY+tip,to,piece[i].y+offY+objHeight-tip);
/*
      if(selected.equals(piece[i])){
	g.setColor(Color.red);
	g.drawRect(from-2,piece[i].y+offY-2,len+3,objHeight+3);
      }
*/
    }
  }

  public boolean inside(aWindow win, int y) {
	   return false;
  }
}

