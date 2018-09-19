package com.shumatpf.CasinoBot;

import java.util.Random;

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

	private Game game;
	private boolean active = false;
	private boolean late = false;

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
		Player player = new Player(user);
		MessageChannel ch = evt.getChannel();
		Message message = evt.getMessage();

		String prefix = "";
		String command = "";

		// System.out.println(message.getContentRaw());

		try {
			prefix = message.getContentRaw().substring(0, 1);
			command = message.getContentRaw().substring(1);
		} catch (Exception e) {
			System.out.println("there was an error :( perhaps other bot?- " + message.getContentRaw());
			return;
		}

		if (prefix.equals(Reference.PREFIX)) {
			System.out.println("command: " + command);
			String[] clist = command.split("\\s+");
			switch (clist[0]) {
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
				if (active) {
					help(ch, 1);
				} else {
					createBJ(player, ch);
				}
				break;
			case "join":
				if (!game.add(player) || !active || late) {
					help(ch, 1);
				} else {
					ch.sendMessage("Current players:").queue();
					for (Player pl : game.getPlayers()) {
						ch.sendMessage("    " + pl.user.getName()).queue();
					}
				}
				break;
			case "leave":
				if (!game.remove(player) || !active || !late) {
					help(ch, 1);
				}
				break;
			case "start":
				if (active) {
					if (player.equals(game.getCreator())) {
						gameStart(ch);
					} else {
						help(ch, 2);
					}
				} else {
					help(ch, 1);
				}
				break;
			case "top-card":
				if (active && late) {
					game.displayVisCards(ch);
				}
				break;
			case "end":
				break;
			case "help":
			default:
				System.out.println("default");
				help(ch, 0);
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
		if (active && late && evt.getMessage().getContentRaw().equals("hand")) {
			evt.getChannel().sendMessage(game.getPlayer(evt.getAuthor()).showHand()).queue();
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
	 * -----------------------
	 * Black Jack Game Methods
	 * -----------------------
	 */
	
	/*
	 * Create game with creator - still open to join at this point
	 */
	private void createBJ(Player player, MessageChannel channel) {
		game = new Game(player);
		active = true;
		channel.sendMessage("Blackjack game created by: " + player.user.getName()).queue();
		channel.sendMessage("If anyone wants to join, enter '>join'").queue();
		channel.sendMessage("To start the game, the creator must enter '>start'").queue();
	}

	/*
	 * Starts game with players who joined - no longer able to join
	 */
	private void gameStart(MessageChannel ch) {
		late = true;
		ch.sendMessage("game started").queue();
		ch.sendMessage("all players have been messaged their hands").queue();

		game.deal(2);
		for (Player player : game.getPlayers()) {
			System.out.println(player.user.getName());
			player.setActive(true);
			player.user.openPrivateChannel().queue((channel) -> {
				channel.sendMessage(player.showHand()).queue();
			});
		}

	}

	/*
	 * Prints help messages based on int. Too many different issues
	 */
	private void help(MessageChannel channel, int num) {
		switch (num) {
		case 0:
			channel.sendMessage("```Basic commands:\n\t" + ">hello\n\t" + ">guess (integer between 0 and 9)\n"
					+ "Game commands:\n\t" + ">start\n\t" + ">add\n\t" + ">leave\n\t" + ">end```").queue();
			break;
		case 1:
			channel.sendMessage("There is no active game, the game is already active, or you are already playing")
					.queue();
			break;
		case 2:
			channel.sendMessage("Only " + game.getCreator().user.getName() + " can start the game");
		default:
		}

	}

}
