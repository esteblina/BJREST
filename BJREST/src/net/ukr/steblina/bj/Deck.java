package net.ukr.steblina.bj;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Deck implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = -2059414679976854119L;
/*	spades-пики, clubs-трефы,hearts-черви,diamonds-бубны...
	туз-ace
	король-king
	дама - queen
	валет-knave, Jack 
	джокер -joker*/
	public List<Card> deck = new LinkedList<Card>();
	private final String[] ranks={"2","3","4","5","6","7","8","9","10","Jack","Queen","King","Ace"};
	private final String[] colors={"Spades","Clubs","Hearts","Diamonds"};
	
	public Deck(){
		for(String rank : ranks){
			for(String color: colors){
				deck.add(new Card(rank, color));
			}
		}
		Collections.shuffle(deck);
		
	}

	public boolean equals(Deck obj) {
		if(this.deck.size()!=obj.deck.size())
			return false;
		for(int i=0; i< this.deck.size();i++){
			if(!this.deck.get(i).equals(obj.deck.get(i)))
				return false;
		}
		return true;
    }

}
