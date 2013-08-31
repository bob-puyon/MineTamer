package bob_pyon.MineTamer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MineTamerCommand implements CommandExecutor {
	private PluginMain plg;

	MineTamerCommand(PluginMain instance) {
		this.plg = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO:実装すべきコマンドとは？
		// args:
		// 1.status (alias:st)
		// 2.list
		// 3.buy
		//***以下,admin系コマンド***
		// 4.reload
		// 5.give <player>(後で実装)
		return true;
	}



}