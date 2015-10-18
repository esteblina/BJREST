package net.ukr.steblina.bj;

import java.util.List;

interface CardsInHand {
     
     public boolean addCard(Card card);
     public List<Card> showCards();
     public int getPoints();
     
     
}
