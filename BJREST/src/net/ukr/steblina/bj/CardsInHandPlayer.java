package net.ukr.steblina.bj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardsInHandPlayer  implements CardsInHand, Serializable{


	private static final long serialVersionUID = -7322080101622789331L;
	
	private List<Card> inHand = new ArrayList<Card>();
	
	@Override
	public int getPoints(){
    	 int points=0;
    	 int aceCount=0;
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
	
	 @Override
	 public List<Card> showCards(){
		return inHand;
	 }
	 
	 public String showCardsStr(){
		 StringBuilder sb = new StringBuilder();
		 for (Card card: inHand){
			 sb.append(card+"; ");
		 }
		 return sb.toString();
	 }
	 
	@Override
	public boolean addCard(Card card) {
		return inHand.add(card);
	}
	
	public boolean equals(CardsInHand obj) {
		if(this.inHand.size()!=obj.showCards().size())
			return false;
		for(int i=0; i< this.inHand.size();i++){
			if(!this.inHand.get(i).equals(obj.showCards().get(i)))
				return false;
		}
		return true;
    }
}
