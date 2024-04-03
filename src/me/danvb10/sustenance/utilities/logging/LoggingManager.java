package me.danvb10.sustenance.utilities.logging;

import me.danvb10.sustenance.Sustenance;

public class LoggingManager {

    Sustenance plugin;
    Boolean isVerbose = false;

    public LoggingManager(Sustenance plugin) {
        this.plugin = plugin;
    }

    public void log(String s) {
        plugin.getLogger().info(s);
    }

    public void warn(String s) {
        plugin.getLogger().warning(s);
    }

    public void error(String s) {
        plugin.getLogger().severe(s);
    }

    public void verbose(String s) {

        if(plugin.getConfig().getBoolean("settings.verbose-logging")) {
            plugin.getLogger().info("<VERBOSE> " + s);
        }

        // Always send all verbose logging to a plugin log file (future)
    }

    // Do this now since we couldn't do it earlier
    public void refreshVerbose() {
        isVerbose = plugin.getConfig().getBoolean("settings.verbose-logging");
        if(isVerbose) {
            verbose("VERBOSE MODE IS ~ENABLED~");
        }
    }
}
