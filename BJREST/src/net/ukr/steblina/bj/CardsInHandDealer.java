package net.ukr.steblina.bj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardsInHandDealer implements CardsInHand, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7009773724131049617L;
	private List<Card> inHand = new ArrayList<Card>();
	private boolean hidden=true;
	
	@Override
	public boolean addCard(Card card) {
		return inHand.add(card);
	}

	@Override
	public List<Card> showCards(){
		if(!hidden)
			return inHand;
		
		return inHand.subList(0, 1);
	}
	
	public String showCardsStr() {
		StringBuilder sb = new StringBuilder();
		if(hidden){
			sb.append(inHand.get(0));
			sb.append("; Hidden;");
			return sb.toString();
			}
		for(Card card : inHand){
			sb.append(card+"; ");			
		}
		return sb.toString();
	}

	@Override
	public int getPoints() {
		 int points=0;
    	 int aceCount=0;
    	 if(hidden){
    		 try{
        		 points+=Integer.parseInt(inHand.get(0).rank);
        		 }catch(NumberFormatException e){
        			 if(inHand.get(0).rank.equals("Ace"))
        				 points+=11;
        			 else
        				 points+=10;
        	 }
    		 return points;
    	 }
    	 for(Card card : inHand){
    		 try{
    		 points+=Integer.parseInt(card.rank);
    		 }catch(NumberFormatException e){
    			 if(card.rank.equals("Ace"))
    				 aceCount++;
    			 else
    			 points+=10;
    		 }
    	 }
    	 
    	 int tmpPoints = 0;
    	 for(int i=0;i<aceCount;i++){
    		 if(points+tmpPoints+11<=21)
    			 tmpPoints+=11;
    		 else if(tmpPoints==11){
    			 points+=1;
    			 tmpPoints-=10;
    		 }else{
    			 points+=1;
    		 }
    	 }
    	 
    	 points+=tmpPoints;    	 
    	 return points;
	}
	
	public void setVisible(){
		hidden=false;
	}
	
	public boolean equals(CardsInHand obj) {
		
		if(this.showCards().size()!=obj.showCards().size())
			return false;
		for(int i=0; i< this.showCards().size();i++){
			if(!this.showCards().get(i).equals(obj.showCards().get(i)))
				return false;
		}
		return true;
    }
}
