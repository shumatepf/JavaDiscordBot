package com.shumatpf.CasinoBot;

public class TestDeck {
	public static void main(String[] args) {
		Deck deck = new Deck();
		deck.suffle();
		System.out.println(deck.toString());
	}
}