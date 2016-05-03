package com.ernietech.cardgames;

/**
 * Created by Ernie on 5/1/2016.
 */

import com.ernietech.cardcollections.Card;
import com.ernietech.cardcollections.Card.Face;
import com.ernietech.cardcollections.CardCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClockSolitaire {

    public static void main(String [] args){

        //TODO instead of calling on an ClockSolitaire object that we crate. Make
        //all of this code internal to that class.
        ClockSolitaire theGame = new ClockSolitaire();
//        System.out.println("starting game cards are: ");
//        theGame.printPiles(false);
//
//        System.out.println("MOVE 1: ");
//        Card.FrenchCard c1 = theGame.drawClockCard(Face.KING);
//        theGame.putClockCard(c1);
//        System.out.println("card picked: " + c1.toString());
//        theGame.printPiles(false);

        //card to draw always starts with King
        Card.Face toDraw = Face.KING;

        // update pile counter at end. check for win at beginning
        // if we win the kingsForcedLoss flag will never be set
        // we check the conditions again after while loop to see which
        // condition terminated the game
        while((!theGame.kingsForcedLoss) && ( theGame.pilesFinished <= PILES_TO_WIN )){
            Card.FrenchCard pulled = theGame.drawClockCard(toDraw);
            theGame.putClockCard(pulled);

            toDraw = pulled.getFace();

            if(theGame.cardsAreLeft(toDraw)){
                if(toDraw == Face.KING){
                    theGame.kingsForcedLoss = true;
                }
                theGame.pilesFinished++;
            }
        }

        if(theGame.kingsForcedLoss){
            System.out.println("*******************YOU LOOSE");
        }else{
            System.out.println("*******************YOU WIN");
        }

        theGame.printPiles(false);


    }

    ClockSolitaire(){
        //TODO put some of this in static initialization block
        facePileIndexes = new HashMap<>();
        //TODO better way is to override Face to contain an inherent index
        int tempIndex = FACE_INDEX_ACE_START;
        facePileIndexes.put(Face.ACE, tempIndex++);
        facePileIndexes.put(Face.TWO, tempIndex++);
        facePileIndexes.put(Face.THREE, tempIndex++);
        facePileIndexes.put(Face.FOUR, tempIndex++);
        facePileIndexes.put(Face.FIVE, tempIndex++);
        facePileIndexes.put(Face.SIX, tempIndex++);
        facePileIndexes.put(Face.SEVEN, tempIndex++);
        facePileIndexes.put(Face.EIGHT, tempIndex++);
        facePileIndexes.put(Face.NINE, tempIndex++);
        facePileIndexes.put(Face.TEN, tempIndex++);
        facePileIndexes.put(Face.JACK, tempIndex++);
        facePileIndexes.put(Face.QUEEN, tempIndex++);
        facePileIndexes.put(Face.KING, tempIndex++);

        pilesFinished = 0;
        kingsForcedLoss = false; //we do not start with a forced loss

        // deck is not a global because we consume all cards by dealing to hands
        CardCollection.Deck<Card.FrenchCard> deck = new CardCollection.Deck<>();
        pilesFaceDown = new ArrayList<>(13); // 1 through 12 + center pile
        pilesFaceUp = new ArrayList<>(13); // 1 through 12 + center pile

        //initialize EMPTY piles
        for(int i = FACE_INDEX_ACE_START; i <= FACE_INDEX_KING_CENTER; i++){
            pilesFaceDown.add(new ArrayList<Card.FrenchCard>());
            pilesFaceUp.add(new ArrayList<Card.FrenchCard>());
        }

        // NOTE: since arrays in java are zero-based clock values are one
        // less than array index

        // NOTE: array index of center pile is 12

        int clockIndex = FACE_INDEX_ACE_START;

        while(deck.getSize() != 0){
            // draw from deck and add to each face down pile
            pilesFaceDown.get(clockIndex).add(deck.drawRandom());
            if (clockIndex == FACE_INDEX_KING_CENTER){
                clockIndex = FACE_INDEX_ACE_START;
            } else {
                clockIndex++;
            }
        }
    }

    ////////////////////////////////////////
    // Methods to draw cards
    ///////////////////////////////////////

    // draw card and REMOVE from a faceDown pile
    Card.FrenchCard drawClockCard(Card.Face pileKey){
        int i = facePileIndexes.get(pileKey);
        return pilesFaceDown.get(i).remove(0);
    }

    // place card by ADDING to faceUp pile
    void putClockCard(Card.FrenchCard card){
        int i = facePileIndexes.get(card.getFace());
        pilesFaceUp.get(i).add(card);
        return;
    }

    boolean cardsAreLeft(Card.Face pileKey){
        int i = facePileIndexes.get(pileKey);
        return !(pilesFaceUp.get(i).isEmpty());
    }


    ////////////////////////////////////////
    // Methods for printing state
    ///////////////////////////////////////

    // prints list of piles, uses array index to print clock position
    void printPiles(boolean showFaceDown){

        // iterate by array index, over face up and face down piles (face down only if flag)
        for(int i = 0; i <= FACE_INDEX_KING_CENTER; i++){
            System.out.println("CLOCK NUMBER: " + (i + 1)); // add 1 because zero indexed

            System.out.println("face up pile...");
            for(Card.FrenchCard c: pilesFaceUp.get(i)){
                System.out.println(c);
            }

            if(showFaceDown){
                System.out.println("face down pile...");
                for(Card.FrenchCard c: pilesFaceDown.get(i)){
                    System.out.println(c);
                }
            }


        }
    }



    //// instance vars ////
    private List<List<Card.FrenchCard>> pilesFaceDown;
    private List<List<Card.FrenchCard>> pilesFaceUp;

    int pilesFinished; // keep track of number of complete piles
    boolean kingsForcedLoss; // true when all kinds are revealed

    private Map<Card.Face, Integer> facePileIndexes;

    static final int FACE_INDEX_ACE_START = 0;
    static final int FACE_INDEX_KING_CENTER = 12; // also the LAST index

    static final int PILES_TO_WIN = 12;


}