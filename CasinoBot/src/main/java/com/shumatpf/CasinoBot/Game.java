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
		for (int i = 0; i < 10; i++) {
			deck.shuffle();
		}
	}

	// get the creator of the game
	public Player getCreator() {
		return creator;
	}

	// get the list of players
	public ArrayList<Player> getPlayers() {
		return players;
	}

	// return the player object associated with user
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
	public boolean remove(User user) {
		Player temp = this.getPlayer(user);
		return players.remove(temp);
	}

	// deals num cards to each player - used at start of game
	public boolean deal(int num) {

		if (players.size() * num >= 52) {
			return false;
		}

		for (Player player : players) {
			dealHand(player, num);
		}

		return true;
	}

	// deals num cards to one person - used at start and whenever a player hits
	public void dealHand(Player player, int num) {
		for (int i = 0; i < num; i++) {
			player.addCard(deck.draw());
		}
	}

	// displays the cards visible to all players in the common channel
	public void displayVisCards(MessageChannel channel) {
		for (Player player : players) {
			channel.sendMessage(player.showVisCards()).queue();
		}
	}

	// returns true if the player is in the game
	public boolean containsPlayer(Player player) {
		for (Player p : players) {
			if (p.user.getName().equals(player.user.getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean allStanding() {
		for (Player player : players) {
			if (!player.isStand()) {
				return false;
			}
		}
		return true;
	}

	// resets the game - creates new deck and removes cards from all players
	public void reset() {
		for (Player player : players) {
			player.removeAll();
		}
		deck = new Deck();
	}

	// ends the game - dont know if this has much functionality
	public void end() {
		deck = new Deck();
		players = new ArrayList<>();
	}

}
