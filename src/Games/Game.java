package Games;

import java.util.ArrayList;

import Deck.Deck;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public abstract class Game {

	protected ArrayList<Player> players;
	protected MessageChannel ch;
	private Deck deck;
	private Player creator;
	public boolean active;
	public boolean late;
	

	// CONSTRUCTOR
	public Game(User user, MessageChannel ch) {
		players = new ArrayList<>();
		this.ch = ch;
		deck = new Deck();
		creator = new Player(user);
		for (int i = 0; i < 10; i++) {
			deck.shuffle();
		}
	}

	public abstract void handleEvent(User user, String command);

	public abstract void gameStart();

	// get the creator of the game
	public Player getCreator() {
		return creator;
	}

	public int numPlayers() {
		return players.size();
	}

	// get the list of players
	public ArrayList<Player> getPlayers() {
		return players;
	}

	// return the player object associated with user
	public Player getPlayer(String user) {
		for (Player player : players) {
			if (player.getName().equals(user)) {
				return player;
			}
		}
		return null;
	}

	// add a user
	public boolean add(User user) {
		if (user != null) {
			for (Player p : players) {
				if (user.getName().equals(p.getName()))
					return false;
			}
		}
		players.add(new Player(user));
		return true;
	}

	// remove a user
	public boolean remove(User user) {
		Player temp = this.getPlayer(user.getName());
		return players.remove(temp);
	}

	// deals num cards to each player - used at start of game
	public boolean deal(int num) {

		if (players.size() * num > 52) {
			return false;
		}

		for (Player player : players) {
			dealHand(player.getName(), num);
		}

		return true;
	}

	// deals num cards to one person - used at start and whenever a player hits
	public void dealHand(String user, int num) {
		Player p = this.getPlayer(user);
		for (int i = 0; i < num; i++) {
			p.addCard(deck.draw());
		}
	}

	// returns true if the player is in the game
	public boolean containsPlayer(User user) {
		for (Player p : players) {
			if (p.user.getName().equals(user.getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean isCreator(User user) {
		return user.getName().equals(creator.user.getName());
	}

	// resets the game - creates new deck and removes cards from all players
	public void reset() {
		active = true;
		late = false;
		players = new ArrayList<>();
		deck = new Deck();
	}

}
