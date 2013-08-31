package bob_puyon.MineTamer;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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
		p.sendMessage("&eペットを飼うことに成功しました！ 今後他のプレイヤーからの操作はできません！&f");
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

		// 対象のエンティティの飼い主が自分の場合も終了
		if( p.getName().equals(  tamer.getName()) ){ return; }

		/* *** この時点で飼育可能で飼い主がいることが保証される *** */
		// また動物観点では馬かどうかを見る必要なない（詳細情報の提供には必要）

		OfflinePlayer off_p = plg.getServer().getOfflinePlayer( tamer.getName() );
		String petname = ((LivingEntity)entity).getCustomName();

		p.sendMessage( formatChatColor( "&eすでに &6" + off_p.getName() + "&e が飼い主の為、効果がありません&f") );
		p.sendMessage( formatChatColor( "&e飼い主がオンラインの場合、この操作は飼い主へ報告が行われます&f") );

		if( off_p.isOnline() ){
			Player player = off_p.getPlayer();
			player.sendMessage( formatChatColor("&e------------[ ペット保護情報 ]-----------&f") );
			player.sendMessage( formatChatColor("&eあなたのペットに対して何か操作がされようとしました!&f") );
			player.sendMessage( formatChatColor("&eプレイヤー名: &6" + p.getName() + "&e (操作は無効化されています) &f") );
			if( petname != null ){
				player.sendMessage( formatChatColor( "&eペットの名前: &6" + petname ) );
			}
			player.sendMessage( formatChatColor( "&eペットのいるワールド名: &6" + entity.getLocation().getWorld().getName() + "&e") );
			player.sendMessage( formatChatColor( "&eペットのいる座標:" +
												" [x:&6" + (int)entity.getLocation().getX() +
												"&e, y:&6" + (int)entity.getLocation().getY() +
												"&e, z:&6" + (int)entity.getLocation().getZ() + "&e]&f") );
			player.sendMessage( formatChatColor("&e-------------------------------------&f") );
		}
		// 飼い主意外の操作に当たるので操作をキャンセル
		e.setCancelled(true);
	}

	private String formatChatColor(String msg){
		return msg.replaceAll("&([a-z0-9])", "\u00A7$1");
	}
}
