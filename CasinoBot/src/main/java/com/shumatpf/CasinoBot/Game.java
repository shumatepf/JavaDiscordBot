package com.shumatpf.CasinoBot;

import java.util.ArrayList;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class Game {

	ArrayList<Player> players;
	Deck deck;
	Player creator;

	// CONSTRUCTOR
	public Game(Player player) {
		players = new ArrayList<>();
		deck = new Deck();
		creator = player;
	}

	public Player getCreator() {
		return creator;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public Player getPlayer(User user) {
		for (Player player : players) {
			if (player.user.getName().equals(user.getName())) {
				return player;
			}
		}
		return null;
	}

	// add a user
	public boolean add(Player player) {
		for (Player p : players) {
			if (player.user.getName().equals(p.user.getName()))
				return false;
		}
		player.setActive(true);
		players.add(player);
		return true;
	}

	// remove a user
	public boolean remove(Player player) {
		return players.remove(player);
	}

	public boolean deal(int num) {

		if (players.size() * num >= 52) {
			return false;
		}

		for (Player player : players) {
			dealHand(player, num);
		}

		return true;
	}

	public void dealHand(Player player, int num) {
		for (int i = 0; i < num; i++) {
			addCard(player);
		}
	}

	public void addCard(Player player) {
		player.addCard(deck.draw());
	}

	public void displayVisCards(MessageChannel channel) {
		for (Player player : players) {
			channel.sendMessage(player.user.getName() + "'s card: " + player.showCard(0)).queue();
		}
	}

	public boolean containsPlayer(Player player) {
		for (Player p : players) {
			if (p.user.getName().equals(player.user.getName())) {
				return true;
			}
		}
		return false;
	}

	public void reset() {
		for (Player player : players) {
			player.removeAll();
		}
		deck = new Deck();
	}

}
