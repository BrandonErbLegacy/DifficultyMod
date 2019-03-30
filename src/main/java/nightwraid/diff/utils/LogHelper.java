package nightwraid.diff.utils;

import nightwraid.diff.general.DifficultyMod;
import nightwraid.diff.settings.GeneralSettings;

public class LogHelper {
	public static void LogInfo(String message) {
		if (GeneralSettings.debugModeEnabled) {
			DifficultyMod.logger.info(message);
		}
	}
	
	public static void LogError(String message) {
		if (GeneralSettings.debugModeEnabled) {
			DifficultyMod.logger.error(message);
		}
	}
	
	public static void LogError(String message, Exception ex) {
		if (GeneralSettings.debugModeEnabled) {
			DifficultyMod.logger.error(message);
			ex.getStackTrace();
		}
	}
}
