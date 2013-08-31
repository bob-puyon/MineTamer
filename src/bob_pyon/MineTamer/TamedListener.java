package bob_pyon.MineTamer;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class TamedListener implements Listener{

	private PluginMain plg;

	public TamedListener(PluginMain instance) {
		this.plg = instance;
	}


	@EventHandler
	public void onEntityTameEvent(EntityTameEvent event){
		AnimalTamer at = event.getOwner();
		Player p = plg.getServer().getPlayer( at.getName() );

		p.sendMessage("何かを飼うことに成功しました！");
		//飼いならされたメッセージを表示
		//逃がすにはコマンドを持って逃がすこと
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e)
	{
		Player p = e.getPlayer();
		Entity entity = e.getRightClicked();

		// 対象のエンティティが飼うことができない場合は終了
		if( !(entity instanceof Tameable) ){ return; }

		AnimalTamer tamer = ((Tameable)entity).getOwner();

		// 対象のエンティティに飼い主がいない場合も保護の余地なしのため終了
		if( tamer == null ) { return; }

		/* *** この時点で飼育可能で飼い主がいることが保証される *** */
		// また動物観点では馬かどうかを見る必要なない

		OfflinePlayer off_p = plg.getServer().getOfflinePlayer( tamer.getName() );

		p.sendMessage( "すでに飼い主がいるため効果がありません 飼い主：" + off_p.getName() );
		if( off_p.isOnline() ){
			Player player = off_p.getPlayer();
			player.sendMessage("あなたのペットに対して何か操作が試みられています");
		}

		e.setCancelled(true);
	}

}
