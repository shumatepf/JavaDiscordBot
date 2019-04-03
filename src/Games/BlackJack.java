package Games;

import CasinoBot.App;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class BlackJack extends Game {

	public BlackJack(User user) {
		super(user);
	}

	public boolean allStanding() {
		for (Player player : players) {
			if (!player.isStand()) {
				return false;
			}
		}
		return true;
	}

	// displays the cards visible to all players in the common channel
	public String displayVisCards() {
		String str = "";
		for (Player player : players) {
			str += player.showVisCards();
		}
		return str;
	}

	@Override
	public void handleEvent(User user, MessageChannel ch, String command) {
		String[] clist = command.split("\\s+");
		switch (clist[0]) {
		case "join":
			if (add(user) && active && !late) {
				ch.sendMessage("*" + user.getName() + "* has joined the game").queue();
			} else {
				App.help(ch, 1);
			}
			break;
		case "leave":
			if (!remove(user)) {
				App.help(ch, 1);
			}
			break;
		case "start":
			if (active) {
				if (isCreator(user)) {
					gameStart(ch);
				} else {
					App.help(ch, 2);
				}
			} else {
				App.help(ch, 1);
			}
			break;
		case "show-cards":
			if (active && late) {
				ch.sendMessage(displayVisCards()).queue();
			}
			break;
		case "hit":
			if (active && late && containsPlayer(user) && !getPlayer(user).isStand()) {
				dealHand(user, 1);
				System.out.print("hit");
				Player curp = getPlayer(user);
				user.openPrivateChannel().queue((channel) -> {
					channel.sendMessage("**New Card:** \n`" + curp.showCard(curp.getNumCards() - 1) + "`").queue();
				});
			}
			break;
		case "stand":
			if (active && late && containsPlayer(user) && !getPlayer(user).isStand()) {
				getPlayer(user).setStand(true);
				ch.sendMessage("*" + user.getName() + "* is standing").queue();
				if (allStanding()) {
					ch.sendMessage("**END RESULTS:**").queue();
					for (Player p : getPlayers()) {
						ch.sendMessage("*" + p.user.getName() + "'s* cards:\n" + p.showHand()).queue();
					}
				}
			}
			break;
		case "end":
			if (isCreator(user)) {
				ch.sendMessage("**The game has been ended**").queue();
				active = false;
				late = false;
				reset();
			} else {
				ch.sendMessage("You must be the creator to end the game").queue();
			}
			break;
		}

	}
	
	/*
	 * Starts game with players who joined - no longer able to join
	 */
	private void gameStart(MessageChannel ch) {
		ch.sendMessage("**GAME STARTED**\n*all players have been messaged their hands*").queue();
		late = true;
		deal(2);
		for (Player player : getPlayers()) {
			System.out.println(player.user.getName());
			player.setActive(true);
			player.user.openPrivateChannel().queue((channel) -> {
				channel.sendMessage("**Your hand:**\n" + player.showHand()).queue();
			});
		}
	}

}
