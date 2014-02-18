/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yary.realhexgen.model.map;

public class Player {
    private int id;
    public boolean turn;
    private int baseId;
    
    public Player(int playerId){
        this.id = playerId;
        this.turn=false;
    }
    
    public int getId() {
        return id;
    }
    
    public int getBaseId() { // id bazy gracza
        return baseId;
    }

    public void setBaseId(int baseId) {
        this.baseId = baseId;
    }
    
    void switchTurn() {
        turn = (turn==false) ? true:false;
        System.out.println("Player"+id+" turn: "+turn);
    }
    
}
