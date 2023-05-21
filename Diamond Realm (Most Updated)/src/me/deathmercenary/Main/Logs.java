package me.deathmercenary.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logs {
	private Main plugin = Main.getPlugin(Main.class);
	
	public void logErrors(String string) {
		File pluginErrorsFile = new File("plugins/PrismRPG/logs/PluginErrors.txt");
		if(!pluginErrorsFile.exists()) {
			try {
				pluginErrorsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(pluginErrorsFile, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.println(string);
		pw.flush();
		pw.close();
	}
	
	public void chatLogs(String string) {
		File pluginErrorsFile = new File("plugins/PrismRPG/logs/ChatLogs.txt");
		if(!pluginErrorsFile.exists()) {
			try {
				pluginErrorsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(pluginErrorsFile, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.println(string);
		pw.flush();
		pw.close();
	}
}
