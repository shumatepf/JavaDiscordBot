package com.shumatpf.Deck;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
	private ArrayList<Card> draw; // draw pile
	private ArrayList<Card> discard; // discard pile
	private int numDraw; // how many cards in the draw pile
	private int numDiscard; // how many cards in discard pile
	private int pos;

	/**
	 * Creates a 52 card standard deck, in order.
	 */
	public Deck() {
		draw = new ArrayList<>(52);
		discard = new ArrayList<>(52);
		numDraw = 52;
		numDiscard = 0;
		pos = 0;
		for (int i = 0; i < 52; i++) {
			draw.add(null);
		}
		for (int i = 0; i < 4; i++) { // i is the suit
			for (int j = 1; j <= 13; j++) { // j is the rank
				int x = i * 13 + j - 1; // the index where this card goes
				draw.set(x, new Card(i, j));
			}
		}
	}

	/**
	 * Returns a card in the draw pile.
	 * 
	 * @param i
	 *            the index of the card to return
	 * @return the card at the given index
	 */
	public Card cardAt(int i) {
		return draw.get(i);
	}

	public Card draw() {
		if (pos == 51) {
			return null;
		}
		Card temp = new Card(draw.get(pos));
		draw.set(pos, null);
		pos++;
		return temp;
	}

	public String toString() {
		String ret = "draw pile: ";
		for (int q = 0; q < numDraw - 1; q++) {
			ret = ret + draw.get(q) + "\n";
		}
		ret = ret + draw.get(numDraw - 1);
		return ret;
	}

	public void shuffle() {
		int n = draw.size();
		Random random = new Random();
		random.nextInt();
		for (int i = 0; i < n; i++) {
			int change = i + random.nextInt(n - i);
			swap(draw, i, change);
		}
	}

	private void swap(ArrayList<Card> a, int i, int change) {
		Card helper = new Card(a.get(i));
		a.set(i, a.get(change));
		a.set(change, helper);
	}
}