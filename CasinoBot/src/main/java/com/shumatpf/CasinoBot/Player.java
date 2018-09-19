package com.shumatpf.CasinoBot;

import java.util.ArrayList;

import net.dv8tion.jda.core.entities.User;

public class Player {

	public User user;
	private ArrayList<Card> hand;
	private boolean inGame = false;

	public Player(User user) {
		this.user = user;
		hand = new ArrayList<>();
	}

	public void setActive(boolean b) {
		inGame = b;
	}

	public boolean isActive() {
		return inGame;
	}

	public void addCard(Card card) {
		hand.add(card);
	}

	public void addCards(ArrayList<Card> cards) {
		hand.addAll(cards);
	}

	public boolean removeCard(Card card) {
		return hand.remove(card);
	}

	public ArrayList<Card> removeAll() {
		ArrayList<Card> temp = new ArrayList<>(hand);
		hand = new ArrayList<>();
		return temp;
	}

	public String showHand() {
		String str = "Your hand:\n";
		for (Card card : hand) {
			str += (card.toString() + "\n");
		}
		return str;
	}

	public boolean equals(Player player) {
		return player.user.getName().equals(this.user.getName());
	}

}
