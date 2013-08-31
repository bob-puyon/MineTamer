package bob_puyon.MineTamer;

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
		//ワールド名を指定して指定したワールドの
		return true;
	}



}