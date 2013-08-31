package bob_puyon.MineTamer;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class TamedListener implements Listener{

	private PluginMain plg;
	private boolean isDamage = false;

	public TamedListener(PluginMain instance) {
		this.plg = instance;
	}

	@EventHandler
	public void onEntityTameEvent(EntityTameEvent event){
		AnimalTamer at = event.getOwner();
		Player p = plg.getServer().getPlayer( at.getName() );
		p.sendMessage( formatChatColor( "&eペットを飼うことに成功しました！今後他のプレイヤーからの操作はできません！&f" ) );
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event){

		Entity entity = event.getEntity();

		if( !(entity instanceof Tameable) ){ return; }

		AnimalTamer tamer = ((Tameable)entity).getOwner();

		// 対象のエンティティに飼い主がいない場合も保護の余地なしのため終了
		if( tamer == null ) { return; }

		// 馬に誰か乗っている場合もダメージは許可（本来であれば飼い主しか乗っていないはず）
		if( entity instanceof Vehicle ){
			if( !((Vehicle)entity).isEmpty() ){ return; }
		}

		// 対象のエンティティの飼い主が自分の場合も終了
		if( event.getDamager() instanceof Player ){
			Player what_owner = (Player)event.getDamager();
			if( what_owner.getName().equals(  tamer.getName()) ){ return; }
		}

		// 直接攻撃の場合
		if( event.getCause() == DamageCause.ENTITY_ATTACK ){
			if( event.getDamager() instanceof Player ){
				Player murder = (Player)event.getDamager();
				noticePetProtection(murder, entity);
				event.setDamage(0D);
			}
		}
		// 矢等の遠距離攻撃の場合
		if( event.getCause() == DamageCause.PROJECTILE ){
			if( event.getDamager() instanceof Arrow){
				Arrow a = (Arrow) event.getDamager();
				if(a.getShooter() instanceof Player) {
					Player murder = (Player) a.getShooter();
					murder.sendMessage("You hit something with an arrow!");
					noticePetProtection(murder, entity);
					event.setDamage(0D);
				}
			}
		}
		return;
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

		// 馬に誰か乗っている場合は許可（本来であれば飼い主しか乗っていないはず）
		if( entity instanceof Vehicle ){
			if( !((Vehicle)entity).isEmpty() ){ return; }
		}

		// 対象のエンティティの飼い主が自分の場合も終了
		if( p.getName().equals(  tamer.getName()) ){ return; }

		/* *** この時点で飼育可能エンティティで飼い主がいることが保証される *** */
		// また動物観点では馬かどうかを見る必要なない（詳細情報の提供には必要）

		// 飼い主意外の操作に当たるので操作をキャンセル
		e.setCancelled(true);
	}

	private void noticePetProtection(Player murder, Entity entity){

		// 被害者名の準備
		OfflinePlayer off_p = plg.getServer().getOfflinePlayer( ((Tameable)entity).getOwner().getName() );

		// 加害者への警告
		murder.sendMessage( formatChatColor( "&eすでに &6" + off_p.getName() + "&e が飼い主の為、効果がありません&f") );
		murder.sendMessage( formatChatColor( "&e飼い主がオンラインの場合、この操作は飼い主へ報告されます&f") );

		// 被害者側オンラインの場合、保護通知を連絡
		if( off_p.isOnline() ){
			Player player = off_p.getPlayer();
			String petname = ((LivingEntity)entity).getCustomName();

			player.sendMessage( formatChatColor("&e------------[ ペット保護情報 ]-----------&f") );
			player.sendMessage( formatChatColor("&eあなたのペットに対して何か操作がされようとしました!&f") );
			player.sendMessage( formatChatColor("&eプレイヤー名: &6" + murder.getName() + "&e (操作は無効化されています) &f") );
			if( petname != null ){
				player.sendMessage( formatChatColor( "&eペットの名前: &6" + petname ) );
			}
			player.sendMessage( formatChatColor( "&eペットのいるワールド名: &6" + entity.getLocation().getWorld().getName() +
					"&e  座標: [x:&6" + (int)entity.getLocation().getX() +
					"&e, y:&6" + (int)entity.getLocation().getY() +
					"&e, z:&6" + (int)entity.getLocation().getZ() + "&e]&f") );
		}

	}

	private String formatChatColor(String msg){
		return msg.replaceAll("&([a-z0-9])", "\u00A7$1");
	}
}
