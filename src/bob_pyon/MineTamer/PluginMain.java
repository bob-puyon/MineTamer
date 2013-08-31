package bob_pyon.MineTamer;

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
	public final static String logPrefix = "[FlyLimiter] ";
	public final static String msgPrefix = "\u00A77[FlyLimiter] \u00A7f";

	@Override
	public void onEnable() {

		//イベントリスナーの登録
		TamedListener eventListener = new TamedListener(this);
		getServer().getPluginManager().registerEvents(eventListener, this);

		//コマンド「flylimiter」を登録（詳しい動作は引数で捌く）
		getCommand("flylimiter").setExecutor(new MineTamerCommand(this));

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
	/*
	public void savePlayerData() {
		File playerFile = new File(getDataFolder(), PLAYER_CONFIGFILE );
		try {
			cfgRestFlyTime.save(playerFile);
		} catch (IOException e) {
			logger.warning("Failed to save players.yml, fly times will not be saved!");
		}
	}

	public void loadPlayerData() {
		File configFile = new File(getDataFolder(), PLUGIN_CONFIGFILE );
		configData = new YamlConfiguration();

		if ( configFile.exists() ) {
			try {
				configData.load( configFile );
			} catch (IOException e) {
				logger.warning("Invalid players.yml, will be overwritten");
				return;
			} catch (InvalidConfigurationException e) {
				logger.warning("Invalid players.yml, will be overwritten");
				return;
			}
			//前回から継続中のプレイヤーを飛行中リストに入れる
			for( Iterator<String> it = configData.getConfigurationSection("disabled_world").getKeys(false).iterator(); it.hasNext(); ){
				this.disabled_flyarea.add( it.next() );
			}
		}

		File playerFile = new File(getDataFolder(), PLAYER_CONFIGFILE );
		cfgRestFlyTime = new YamlConfiguration();

		if (playerFile.exists()) {
			try {
				cfgRestFlyTime.load(playerFile);
			} catch (IOException e) {
				logger.warning("Invalid players.yml, will be overwritten");
				return;
			} catch (InvalidConfigurationException e) {
				logger.warning("Invalid players.yml, will be overwritten");
				return;
			}
			//前回から継続中のプレイヤーを飛行中リストに入れる
			for( Iterator<String> it = cfgRestFlyTime.getConfigurationSection("flyinguser").getKeys(false).iterator(); it.hasNext();  ){
				this.flying_state.add( it.next() );
			}
		}
	}

	private void refreshPlayerData(){

		//Player.ymlのflyinguserセクションが存在しない場合リフレッシュを拒否
		if( !this.cfgRestFlyTime.isConfigurationSection("flyinguser") ){
			logger.info("'flyinguser' Section does not exist!! - Player.yml refresh abort");
			return;
		}

		///////////////////////////////////////////////////////
		// HashMapフィールドを利用したゴミ情報の洗い出し開始 //
		///////////////////////////////////////////////////////

		//現在使用されているプレイヤー情報ををHashMapに書き出し
		HashMap<String,Object> remap =
				new HashMap<String,Object>(this.cfgRestFlyTime.getConfigurationSection("flyinguser").getValues(false));

		//クリーンアップした情報を書き込むためのコンフィグ変数を用意
		YamlConfiguration clr_cfgRestFlyTime = new YamlConfiguration();
		//flyinguserセクションの準備
		clr_cfgRestFlyTime.createSection("flyinguser");

		//飛行状態が有効化中のユーザーを書き戻し、終了後のユーザーは書き戻さない
		for( Iterator<Map.Entry<String, Object>> it = remap.entrySet().iterator(); it.hasNext(); ){

			//Mapから１組ずつキー（ユーザ名）と値（飛行ポイント）を取り出す
			Map.Entry<String, Object> entry = it.next();
			String fly_name = entry.getKey();
			Integer fly_time = new Integer( entry.getValue().toString() );

			//ポイントに残りがない(マイナスの)場合、書き戻さないで次のユーザー精査へ
			if( fly_time == null || fly_time < 0 ){ continue; }
			//ポイントに残りがある(0以上の)場合、継続して使用ユーザーとしてリストアップ
			clr_cfgRestFlyTime.set( "flyinguser." + fly_name, fly_time);
		}

		//クリーンアップされた clr_cfgRestFlyTime を Player.yml に 書き込み
		File playerFile = new File(getDataFolder(), PLAYER_CONFIGFILE );
		try {
			clr_cfgRestFlyTime.save(playerFile);
		} catch (IOException e) {
			logger.warning("Failed to save players.yml, fly times will not be saved!");
		}
		//本来扱っているファイルで読み直す
		this.loadPlayerData();
	}
	*/
