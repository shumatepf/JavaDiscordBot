package Games;

import CasinoBot.App;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class War extends Game {

	public War(User user) {
		super(user);
	}

	@Override
	public void handleEvent(User user, MessageChannel ch, String command) {
		String[] clist = command.split("\\s+");
		switch (clist[0]) {
		case "join":
			if (add(user) && active && !late) {
				ch.sendMessage("*" + user.getName() + "* has joined the game").queue();
				if (players.size() == 2) {
					gameStart(ch);
				}
			} else {
				App.help(ch, 1);
			}
			break;
		case "start": // doesn't work -- need to figure out how to do this
			if (active && !late && players.size() == 1) {
				ch.sendMessage("Now beginning a game vs. bot").queue();
			}
			break;
		case "go": // each turn begins when creator says "go"
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
	public void gameStart(MessageChannel ch) {
		ch.sendMessage("**GAME STARTED**\n*all players have been dealt their hands*").queue();
		late = true;
		deal(26);
	}

}
