package Games;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import CasinoBot.App;
import Deck.Card;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class War extends Game {

	private ArrayList<Card> pool;
	private Queue<Card> p1Hand;
	private Queue<Card> p2Hand;

	public War(User user, MessageChannel ch) {
		super(user, ch);
		pool = new ArrayList<>();
	}

	@Override
	public void handleEvent(User user, String command) {
		String[] clist = command.split("\\s+");
		switch (clist[0]) {
		case "join":
			if (add(user) && active && !late) {
				ch.sendMessage("*" + user.getName() + "* has joined the game").queue();
				if (players.size() == 2) {
					gameStart();
				}
			} else {
				App.help(ch, 1);
			}
			break;
		case "start": // doesn't work -- need to figure out how to do this
			if (active && !late && players.size() == 1) {
				ch.sendMessage("Now beginning a game vs. bot").queue();
				add(null);
				gameStart();
			}
			break;
		case "go": // each turn begins when creator says "go"
			if (active && late && p1Hand.size() >= 1 && p2Hand.size() >= 1) {
				turn();
			}
			break;
		case "end":
			if (isCreator(user)) {
				ch.sendMessage("**The game has been ended**").queue();
				App.endGame();
			} else {
				ch.sendMessage("You must be the creator to end the game").queue();
			}
			break;
		default:
		}
	}

	@Override
	public void gameStart() {
		ch.sendMessage("**GAME STARTED**\n*all players have been dealt their hands*").queue();
		deal(26);
		p1Hand = new LinkedList<>(players.get(0).getHand());
		p2Hand = new LinkedList<>(players.get(1).getHand());
		late = true;
	}

	private void turn() {
		int p1Val = (p1Hand.peek().getRank() == 1) ? 15 : p1Hand.peek().getRank();
		int p2Val = (p2Hand.peek().getRank() == 1) ? 15 : p2Hand.peek().getRank();

		pool.add(p1Hand.poll());
		pool.add(p2Hand.poll());
		ch.sendMessage("*" + players.get(0).getName() + "*'s card:\n" + pool.get(0).toString()).queue();
		ch.sendMessage("*" + players.get(1).getName() + "*'s card:\n" + pool.get(1).toString()).queue();

		if (p1Val > p2Val) {
			for (Card c : pool) {
				p1Hand.add(c);
			}
			ch.sendMessage("*" + players.get(0).getName() + "* wins the turn").queue();
		} else if (p1Val < p2Val) {
			for (Card c : pool) {
				p2Hand.add(c);
			}
			ch.sendMessage("*" + players.get(1).getName() + "* wins the turn").queue();
		} else {
			try {
				handleTruce();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		pool = new ArrayList<>();
		ch.sendMessage("*" + players.get(0).getName() + "*'s hand size: " + p1Hand.size()).queue();
		ch.sendMessage("*" + players.get(1).getName() + "*'s hand size: " + p2Hand.size()).queue();
	}

	private void handleTruce() throws InterruptedException {
		ch.sendMessage("Truce encountered").queue();
		TimeUnit.SECONDS.sleep(1);
		for (int i = 0; i < 3; i++) {
			TimeUnit.SECONDS.sleep(1);
			pool.add(p1Hand.poll());
			pool.add(p2Hand.poll());
			ch.sendMessage(i + "...").queue();
		}
		TimeUnit.SECONDS.sleep(1);
		turn();
	}

}
