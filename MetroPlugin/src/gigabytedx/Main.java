package gigabytedx;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public final Logger logger = Logger.getLogger("Minecraft");
	public static Main plugin;
	static String pluginName;
	static boolean isDebugModeEnabled = true;
	int xCount = 0, zCount = 0;
	String[] materials = new String[256];
	Byte[] data = new Byte[256];
	public static File file;
	public static Configuration conf;

	public static List<String> eastCompat;
	public static List<String> southCompat;
	public static List<String> westCompat;
	public static List<String> northCompat;

	public static FileConfiguration customConfig = null;
	public static List<Generator> generators = new ArrayList<Generator>();

	public static HashMap<String, FileConfiguration> moduleConfigurations = new HashMap<String, FileConfiguration>();

	@Override
	public void onDisable() {

		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Has Been Disabled!");
	}

	@Override
	public void onEnable() {

		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " Has Been Enabled!");
		pluginName = pdfFile.getName();
		this.saveDefaultConfig();
		conf = this.getConfig();
		// load module configurations from file
		loadModuleConfigurations();
		loadCompatabilities();
		file = this.getDataFolder();
	}

	@SuppressWarnings("unchecked")
	private void loadCompatabilities() {

		File temp;
		FileConfiguration FC;

		//load compatabilities for each direction from yml to configuration
		temp = new File(getDataFolder(), "Compats/NorthCompat.yml");
		FC = YamlConfiguration.loadConfiguration(temp);
		northCompat = (List<String>) FC.getList("CompatibleModules");

		temp = new File(getDataFolder(), "Compats/EastCompat.yml");
		FC = YamlConfiguration.loadConfiguration(temp);
		eastCompat = (List<String>) FC.getList("CompatibleModules");

		temp = new File(getDataFolder(), "Compats/SouthCompat.yml");
		FC = YamlConfiguration.loadConfiguration(temp);
		southCompat = (List<String>) FC.getList("CompatibleModules");

		temp = new File(getDataFolder(), "Compats/WestCompat.yml");
		FC = YamlConfiguration.loadConfiguration(temp);
		westCompat = (List<String>) FC.getList("CompatibleModules");

	}

	private void loadModuleConfigurations() {

		// get modules list yml file from plugin folder
		File registedModulesFile = new File(getDataFolder(), "RegistedModules.yml");

		// get fileConfiguration for that list
		FileConfiguration FC = YamlConfiguration.loadConfiguration(registedModulesFile);

		// set list variable to list received from the config file
		@SuppressWarnings("unchecked")
		List<String> registedModulesList = (List<String>) FC.getList("RegistedModules");

		// add list to hashmap
		if (registedModulesList != null)
			addModulesToHashMap(registedModulesList);
	}

	private void addModulesToHashMap(List<String> registedModulesList) {

		// loop through each module name in the list
		for (String x : registedModulesList) {

			// add module configuration file to hash map
			String path = "ModuleConfigs/" + x + ".yml";
			File temp = new File(getDataFolder(), path);
			System.out.println("plugins/MetroGen/ModuleConfigs/" + x + ".yml");

			//add configuration to hashmap
			moduleConfigurations.put(x, YamlConfiguration.loadConfiguration(temp));
			Main.sendDebugInfo(x + " Has been successfully loaded");

		}

	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, final String[] args) {

		// get player who sent command
		Player player = (Player) sender;

		// on createModule command
		if (commandLabel.equalsIgnoreCase("createmodule") && args.length == 1) {
			try {
				//save block arrays to file
				ReadAndWrite.save("ModuleData/" + args[0] + "_BlockTypes", getBlocks(), getDataFolder());
				ReadAndWrite.save("ModuleData/" + args[0] + "_BlockData", getBlockData(), getDataFolder());
				player.sendMessage("saved?");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			try {
				//get array from file and paste it above the old one
				setBlocks((String[]) ReadAndWrite.read("ModuleData/" + args[0] + "_BlockTypes", getDataFolder()), (Byte[]) ReadAndWrite.read("ModuleData/" + args[0] + "_BlockData", getDataFolder()), 0, 0);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//on generate command
		} else if (commandLabel.equalsIgnoreCase("generate")) {
			//create new generator
			Generator.moduleChuncks.add("0.0");
			generators.add(new Generator(this, new Mod(0, 0, "StraightSouthNorth"), 0, 0));
		}
		return false;
	}

	private String[] getBlocks() {

		int blockCount = 0;

		for (int y = 0; y <= 0; y++) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {

					materials[blockCount] = Bukkit.getWorld("world").getBlockAt(x, 4, z).getType().toString();
					blockCount++;

				}
			}
		}
		return materials;
	}

	@SuppressWarnings("deprecation")
	private Byte[] getBlockData() {

		int blockCount = 0;

		for (int y = 0; y <= 0; y++) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {

					data[blockCount] = Bukkit.getWorld("world").getBlockAt(x, 4, z).getData();
					blockCount++;

				}
			}
		}
		return data;
	}

	public static void sendDebugInfo(String debugText) {

		// output debug info if debug mode is enabled.
		if (isDebugModeEnabled)

			// Output received text to console with info prefix.
			System.out.println("[" + pluginName + "]" + " " + debugText);

	}

	public static void sendSevereInfo(String debugText) {

		// Output received text to console with severe prefix.
		System.out.println("[" + pluginName + "]" + " " + debugText);

	}

	@SuppressWarnings("deprecation")
	private void setBlocks(String[] blockArray, Byte[] blockDataArray, int xOffset, int zOffset) {

		int blockCount = 0;

		for (int x = xOffset; x < xOffset + 16; x++) {
			for (int z = zOffset; z < zOffset + 16; z++) {

				Bukkit.getWorld("world").getBlockAt(x, 10, z).setType(Material.getMaterial(blockArray[blockCount]));

				Bukkit.getWorld("world").getBlockAt(x, 10, z).setData(blockDataArray[blockCount]);

				blockCount++;

			}
		}

	}
}