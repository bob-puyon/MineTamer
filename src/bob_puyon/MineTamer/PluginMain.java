package bob_puyon.MineTamer;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

//Vaultプラグインの経済概念連携
//残り時間の確認について
//何分前、何秒前にアナウンスを行う？
//alias「flylmt」

//サーバー再起動（プラグインDisable時）の場合
//設定の書き戻しとゴミ情報リフレッシュについて

//ワールド移動時
//地上最終での保護フラグ除去
//スレッドの開始タイミング
//飛行スピード

//追加実装案
//1.残りの飛行時間を持ってるアイテムの耐久度で表現する


public class PluginMain extends JavaPlugin{

	//扱うloggerの取得
	public final static Logger logger = Logger.getLogger("Minecraft");
	public final static String logPrefix = "[MineTamer] ";
	public final static String msgPrefix = "[\u00A77MineTamer\u00A7f]";

	@Override
	public void onEnable() {

		//イベントリスナーの登録
		TamedListener eventListener = new TamedListener(this);
		getServer().getPluginManager().registerEvents(eventListener, this);

		//コマンド「flylimiter」を登録（詳しい動作は引数で捌く）
		getCommand("minetamer").setExecutor(new MineTamerCommand(this));

		//起動メッセージ
		PluginDescriptionFile file_pdf = this.getDescription();
		logger.info("[" +file_pdf.getName()+ "] v" + file_pdf.getVersion() + " is enabled!");
	}

	@Override
	public void onDisable() {
		//停止メッセージ
		PluginDescriptionFile file_pdf = this.getDescription();
		logger.info("[" +file_pdf.getName()+ "] v" + file_pdf.getVersion() + " is disabled!");
	}
}
