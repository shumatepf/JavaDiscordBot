package CasinoBot;

import java.util.Random;

import Games.BlackJack;
import Games.Game;
import Games.Player;
import Games.War;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class App extends ListenerAdapter {

	private static Game game;

	public static void main(String[] args) throws Exception {
		JDA jda = new JDABuilder(AccountType.BOT).setToken(Reference.TOKEN).build();

		jda.addEventListener(new App());
	}

	/*
	 * Message listener. Reads messages from main channel
	 */
	@Override
	public void onMessageReceived(MessageReceivedEvent evt) {
		User user = evt.getAuthor();
		// Player player = new Player(user);
		MessageChannel ch = evt.getChannel();
		Message message = evt.getMessage();

		if (!ch.getName().equals(Reference.CHANNEL)) {
			return; // only accepts commands in the predecided channel - "casino"
		}

		String prefix = "";
		String command = "";

		try {
			prefix = message.getContentRaw().substring(0, Reference.PREFIX.length());
			command = message.getContentRaw().substring(Reference.PREFIX.length());
		} catch (Exception e) {
			System.out.println("there was an error :( perhaps other bot?- " + message.getContentRaw());
			return;
		}

		if (prefix.equals(Reference.PREFIX)) {
			System.out.println("command: " + command);
			String[] clist = command.split("\\s+");
			switch (clist[0]) {
			case "list-games":
				ch.sendMessage("**Current Games:**\n \tBlackjack").queue();
				break;
			case "hello":
				user.openPrivateChannel().queue((channel) -> {
					channel.sendMessage("Hello").queue();
				});
				break;
			case "guess":
				try {
					guess(Integer.parseInt(clist[1]), ch);
				} catch (Exception e) {
					System.out.println("error with guess");
					help(ch, 0);
				}
				break;
			case "blackjack":
				if (game != null) {
					help(ch, 1);
				} else {
					createBJ(user, ch);
				}
				break;
			case "war":
				if (game != null) {
					help(ch, 1);
				} else {
					createWar(user, ch);
				}
				break;
			case "exit":
				ch.sendMessage("Bot shutting down").queue();
				System.exit(1);
			case "help":
				System.out.println("help");
				if (clist.length > 1) {
					switch (clist[1]) {
					case "blackjack":
						help(ch, 4);
						break;
					default:
						help(ch, -1);
					}
				} else {
					help(ch, 0);
				}
				break;
			default:
				if (game != null) {
					System.out.println("Handled by game");
					game.handleEvent(user, command);
				} else {
					help(ch, 1);
				}
				break;
			}
		}
	}

	/*
	 * Private message listener
	 */
	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent evt) {
		if (evt.getAuthor().isBot())
			return;
		System.out.println("private message receieved: " + evt.getMessage().getContentRaw());
		if (game.active && game.late && evt.getMessage().getContentRaw().equals("hand")) {
			evt.getChannel().sendMessage(game.getPlayer(evt.getAuthor().getName()).showHand()).queue();
		}
	}

	/*
	 * Guessing Game
	 */
	private void guess(int number, MessageChannel channel) {
		int random = new Random().nextInt(10);
		if (number == random) {
			channel.sendMessage("wow you got it").queue();
		} else {
			channel.sendMessage("nope, the number was: " + random).queue();
		}
	}

	/*
	 * ----------------------- Black Jack Game Methods -----------------------
	 */

	/*
	 * Create game with creator - still open to join at this point
	 */
	private void createBJ(User user, MessageChannel ch) {
		game = new BlackJack(user, ch);
		game.active = true;
		ch.sendMessage("**Blackjack game created by:** *" + user.getName() + "*").queue();
		ch.sendMessage(String.format(
				"If anyone wants to join, enter `%1$sjoin`\nTo start the game, the creator must enter `%1$sstart`\n",
				Reference.PREFIX)).queue();
	}
	
	private void createWar(User user, MessageChannel ch) {
		game = new War(user, ch);
		game.active = true;
		ch.sendMessage("**War game created by:** *" + user.getName() + "*").queue();
		ch.sendMessage(String.format(
				"If anyone wants to join, enter `%1$sjoin`\nThe game will begin when two people have joined\n",
				Reference.PREFIX)).queue();
	}

	public static void endGame() {
		game = null;
	}

	/*
	 * Prints help messages based on int. Too many different issues
	 */
	public static void help(MessageChannel channel, int num) {
		switch (num) {
		case 0:
			channel.sendMessage(
					String.format("```Basic commands:\n\t%1$shello\n\t%1$sguess (integer between 0 and 9)\n\t%1$slist-games```",
							Reference.PREFIX))
					.queue();
			break;
		case 1:
			channel.sendMessage("There is no active game, the game is already active, or you are already playing")
					.queue();
			break;
		case 2:
			channel.sendMessage("Only " + game.getCreator().user.getName() + " can start the game");
			break;
		case 3:
			channel.sendMessage("Game has not started yet").queue();
			break;
		case 4:
			channel.sendMessage(String.format("```Blackjack commands:\n\t" + "%1$sblackjack\n\t" + "%1$sjoin\n\t"
					+ "%1$sleave\n\t" + "%1$sstart\n\t" + "%1$shit\n\t" + "%1$sshow-cards\n\t" + "%1$sstand\n\t"
					+ "%1$send\n\t" + "%1$sexit```", Reference.PREFIX)).queue();
			break;
		default:
			channel.sendMessage("Unkown Command").queue();
		}

	}

}
