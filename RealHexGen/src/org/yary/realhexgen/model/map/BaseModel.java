/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yary.realhexgen.model.map;

import java.util.LinkedList;

/**
 *
 * @author Milia
 */
public class BaseModel extends TileModel{
    public BaseModel(int row, int column, int id, int playerId){
        super(row, column, id);
        this.playerId=playerId;
    }
    
    public int playerId;
    private int nextUnit; // licznik (do Deploy())
    LinkedList baseNeighbors;
    
    public void setNextUnit(int nu) {
        this.nextUnit=nu;
    }
    
    public int getNextUnit() {
        return nextUnit;
    }
    
    public void setNeighbors() { // hexy dookoła bazy
        baseNeighbors = new LinkedList();

        baseNeighbors.add(getId()-15);
        baseNeighbors.add(getId()-7);
        baseNeighbors.add(getId()+8);
        baseNeighbors.add(getId()+15);
        baseNeighbors.add(getId()+7);
        baseNeighbors.add(getId()-8);
        setNextUnit(0);
    }
    
}
