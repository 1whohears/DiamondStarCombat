package com.onewhohears.dscombat.command;

import java.util.Arrays;
import java.util.Collection;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.onewhohears.dscombat.game.GameManager;
import com.onewhohears.dscombat.game.data.GameData;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.TeamArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;

public class GameCommand {
	
	public GameCommand(CommandDispatcher<CommandSourceStack> d) {
		d.register(Commands.literal("game").requires((stack) -> { return stack.hasPermission(2);})
			.then(createNew())
			.then(setup())
			.then(reset())
			.then(remove())
			.then(listRunning())
		);
	}
	
	private ArgumentBuilder<CommandSourceStack,?> setup() {
		return Commands.literal("setup")
			.then(Commands.argument("instance_id", StringArgumentType.word())
			.suggests(suggestStrings(GameManager.getRunningeGameIds()))
				.then(Commands.literal("start")
				.executes(commandStartGame()))
				.then(Commands.literal("add_team")
					.then(Commands.argument("team", TeamArgument.team())
					.executes(commandAddTeam())))
				.then(Commands.literal("remove_team")
					.then(Commands.argument("team", TeamArgument.team())
					.executes(commandRemoveTeam())))
				.then(Commands.literal("add_player")
					.then(Commands.argument("player", EntityArgument.players())
					.executes(commandAddPlayers())))
				.then(Commands.literal("remove_player")
					.then(Commands.argument("player", EntityArgument.players())
					.executes(commandRemovePlayers())))
				.then(Commands.literal("set_center")
						.then(Commands.argument("game_center", BlockPosArgument.blockPos())
						.executes(commandSetCenter())))
				.then(Commands.literal("set_size")
						.then(Commands.argument("game_size", DoubleArgumentType.doubleArg(-5.9999968E7D, 5.9999968E7D))
						.executes(commandSetSize())))
			);
	}
	
	private GameSetupCommand commandSetSize() {
		return (context, gameData) -> {
			double size = DoubleArgumentType.getDouble(context, "game_size");
			gameData.setGameBorderSize(size);
			Component message = Component.literal("Changed "+gameData.getId()+" game size to "+size);
			context.getSource().sendSuccess(message, true);
			return 1;
		};
	}
	
	private GameSetupCommand commandSetCenter() {
		return (context, gameData) -> {
			BlockPos pos = BlockPosArgument.getSpawnablePos(context, "game_center");
			gameData.setGameCenter(new Vec3(pos.getX(), pos.getY(), pos.getZ()), context.getSource().getServer());
			Component message = Component.literal("Changed "+gameData.getId()+" center pos!");
			context.getSource().sendSuccess(message, true);
			return 1;
		};
	}
	
	private GameSetupCommand commandStartGame() {
		return (context, gameData) -> {
			if (!gameData.finishSetupPhase(context.getSource().getServer())) {
				MutableComponent message = Component.literal(gameData.getId()+" is currently unable to finish setup!");
				message.append("\nUse /game setup "+gameData.getId()+" to finish settig up the game.");
				message.append("\n"+gameData.getSetupInfo());
				context.getSource().sendFailure(message);
				return 0;
			}
			Component message = Component.literal("Starting "+gameData.getId()+"!");
			context.getSource().sendSuccess(message, true);
			return 1;
		};
	}
	
	private GameSetupCommand commandRemovePlayers() {
		return (context, gameData) -> {
			Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "player");
			if (players.size() == 0) {
				context.getSource().sendSuccess(Component.literal("No players could be removed."), true);
				return 1;
			}
			for (ServerPlayer player : players) {
				if (gameData.removeAgentById(player.getStringUUID())) {
					Component message = Component.literal("Failed to remove ").append(player.getDisplayName()).append(" from "+gameData.getId()+"!");
					context.getSource().sendFailure(message);
				} else {
					Component message = Component.literal("Removed player ").append(player.getDisplayName()).append(" from "+gameData.getId()+"!");
					context.getSource().sendSuccess(message, true);
				}
			}
			return 1;
		};
	}
	
	private GameSetupCommand commandAddPlayers() {
		return (context, gameData) -> {
			if (!gameData.canAddIndividualPlayers()) {
				Component message = Component.literal("The game instance "+gameData.getId()+" does not allow individual players!");
				context.getSource().sendFailure(message);
				return 0;
			}
			Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "player");
			if (players.size() == 0) {
				context.getSource().sendSuccess(Component.literal("No players could be added."), true);
				return 1;
			}
			for (ServerPlayer player : players) {
				if (gameData.getAddIndividualPlayer(player) == null) {
					Component message = Component.literal("Failed to add ").append(player.getDisplayName()).append(" to "+gameData.getId()+"!");
					context.getSource().sendFailure(message);
				} else {
					Component message = Component.literal("Added player ").append(player.getDisplayName()).append(" to "+gameData.getId()+"!");
					context.getSource().sendSuccess(message, true);
				}
			}
			return 1;
		};
	}
	
	private GameSetupCommand commandRemoveTeam() {
		return (context, gameData) -> {
			PlayerTeam team = TeamArgument.getTeam(context, "team");
			if (!gameData.removeAgentById(team.getName())) {
				Component message = Component.literal("Already removed "+team.getName()+" from "+gameData.getId()+"!");
				context.getSource().sendSuccess(message, true);
				return 1;
			}
			MutableComponent message = Component.literal("Removed "+team.getName()+" from "+gameData.getId()+"!");
			context.getSource().sendSuccess(message, true);
			return 1;
		};
	}
	
	private GameSetupCommand commandAddTeam() {
		return (context, gameData) -> {
			if (!gameData.canAddTeams()) {
				Component message = Component.literal("The game instance "+gameData.getId()+" does not allow teams!");
				context.getSource().sendFailure(message);
				return 0;
			}
			PlayerTeam team = TeamArgument.getTeam(context, "team");
			if (gameData.getAddTeam(team) == null) {
				Component message = Component.literal("Failed to add "+team.getName()+" to "+gameData.getId()+"!");
				context.getSource().sendFailure(message);
				return 0;
			}
			MutableComponent message = Component.literal("Added team "+team.getName()+" to "+gameData.getId()+"!");
			context.getSource().sendSuccess(message, true);
			return 1;
		};
	}
	
	private ArgumentBuilder<CommandSourceStack,?> createNew() {
		return Commands.literal("create_new")
			.then(Commands.argument("game_type", StringArgumentType.word())
			.suggests(suggestStrings(GameManager.getRunningeGameIds()))
				.then(Commands.argument("instance_id", StringArgumentType.word())
				.executes(commandCreateNew()))
			);
	}
	
	private Command<CommandSourceStack> commandCreateNew() {
		return (context) -> {
			String gameTypeId = StringArgumentType.getString(context, "game_type");
			if (!GameManager.hasGameType(gameTypeId)) {
				Component message = Component.literal("The Game Type "+gameTypeId+" does not exist!");
				context.getSource().sendFailure(message);
				return 0;
			}
			String gameInstanceId = StringArgumentType.getString(context, "instance_id");
			if (GameManager.isGameRunning(gameInstanceId)) {
				Component message = Component.literal(gameInstanceId+" already exists! You may want to reset or remove it.");
				context.getSource().sendFailure(message);
				return 0;
			}
			GameData gameData = GameManager.startNewGame(gameTypeId, gameInstanceId);
			if (gameData == null) {
				Component message = Component.literal("Unable to start new game "+gameInstanceId+" of type "+gameTypeId);
				context.getSource().sendFailure(message);
				return 0;
			}
			gameData.setGameCenter(context.getSource().getPosition());
			MutableComponent message = Component.literal("Started new game of type "+gameTypeId+" called "+gameInstanceId+".");
			message.append("\nUse /game setup "+gameInstanceId+" to configure the game.");
			message.append("\n"+gameData.getSetupInfo());
			context.getSource().sendSuccess(message, true);
			return 1;
		};
	}
	
	private ArgumentBuilder<CommandSourceStack,?> reset() {
		return Commands.literal("reset")
			.then(Commands.argument("instance_id", StringArgumentType.word())
			.suggests(suggestStrings(GameManager.getRunningeGameIds()))
				.then(Commands.literal("confirm_reset")
				.executes(commandReset()))
			);
	}
	
	private GameDataCommand commandReset() {
		return (context, gameData) -> {
			gameData.reset(context.getSource().getServer());
			context.getSource().sendSuccess(Component.literal(gameData.getId()+" was reset!"), true);
			return 1;
		};
	}
	
	private ArgumentBuilder<CommandSourceStack,?> remove() {
		return Commands.literal("remove")
			.then(Commands.argument("instance_id", StringArgumentType.word())
			.suggests(suggestStrings(GameManager.getRunningeGameIds()))
				.then(Commands.literal("confirm_remove")
				.executes(commandRemove()))
			);
	}
	
	private GameDataCommand commandRemove() {
		return (context, gameData) -> {
			gameData.reset(context.getSource().getServer());
			GameManager.removeGame(gameData.getId());
			context.getSource().sendSuccess(Component.literal(gameData.getId()+" was removed!"), true);
			return 1;
		};
	}
	
	private ArgumentBuilder<CommandSourceStack,?> listRunning() {
		return Commands.literal("list_running").executes((context) -> {
			Component message = Component.literal(Arrays.deepToString(GameManager.getRunningeGameIds()));
			context.getSource().sendSuccess(message, true);
			return 1;
		});
	}
	
	public static SuggestionProvider<CommandSourceStack> suggestStrings(String[] strings) {
		return (context, builder) -> {
			return SharedSuggestionProvider.suggest(strings, builder);
		};
	}
	
	public interface GameDataCommand extends Command<CommandSourceStack> {
		default int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
			String gameInstanceId = StringArgumentType.getString(context, "instance_id");
			if (!GameManager.isGameRunning(gameInstanceId)) {
				Component message = Component.literal("The game instance "+gameInstanceId+" does not exist!");
				context.getSource().sendFailure(message);
				return 0;
			}
			return runGameData(context, GameManager.getRunningGame(gameInstanceId));
		}
		int runGameData(CommandContext<CommandSourceStack> context, GameData gameData) throws CommandSyntaxException;
	}
	
	public interface GameSetupCommand extends GameDataCommand {
		default int runGameData(CommandContext<CommandSourceStack> context, GameData gameData) throws CommandSyntaxException {
			if (!gameData.isSetupPhase()) {
				Component message = Component.literal("The game instance "+gameData.getId()+" is not in the setup phase! You must reset (Dangerous!)");
				context.getSource().sendFailure(message);
				return 0;
			}
			return runSetup(context, gameData);
		}
		int runSetup(CommandContext<CommandSourceStack> context, GameData gameData) throws CommandSyntaxException;
	}
	
}
