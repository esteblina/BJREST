package net.ukr.steblina.bj;

import java.io.Serializable;

public class Card implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6084439444152091804L;
	public String rank;
	public String color;
	
	public Card(String rank, String color){
		this.rank=rank;
		this.color=color;
	}
	public String toString(){
		return rank+" "+color;
	}
	public boolean equals(Card obj) {
        return (this.rank.equals(obj.rank)&this.color.equals(obj.color));
    }
}
