package com.shumatpf.CasinoBot;

public class Card {
	public static final int HEARTS = 0;
	public static final int DIAMONDS = 1;
	public static final int CLUBS = 2;
	public static final int SPADES = 3;

	private int suit; // says what suit the card is (one of the above 4)
	private int rank; // says what number (or face card) it is J=11, Q=12, K=13

	public Card(int suit, int rank) {
		this.suit = suit;
		this.rank = rank;
	}

	public Card(Card card) {
		this.suit = card.getSuit();
		this.rank = card.getRank();
	}

	public int getSuit() {
		return suit;
	}

	public int getRank() {
		return rank;
	}

	/**
	 * Returns a string like "Ace of spades" or "9 of diamonds"
	 */
	public String toString() {
		return rank() + " of " + suit();
	}

	/**
	 * Returns the rank of the card as a string
	 * 
	 * @return the rank of the card as a string.
	 */
	private String rank() {
		if (rank == 13) {
			return "King";
		} else if (rank == 12) {
			return "Queen";
		} else if (rank == 11) {
			return "Jack";
		} else if (rank == 1) {
			return "Ace";
		} else {
			return rank + "";
		}
	}

	/**
	 * Returns the suit of the card as a string.
	 * 
	 * @return the suit of the card as a string.
	 */
	private String suit() {
		if (suit == HEARTS) {
			return "hearts";
		} else if (suit == DIAMONDS) {
			return "diamonds";
		} else if (suit == SPADES) {
			return "spades";
		} else {
			return "clubs";
		}
	}

}
