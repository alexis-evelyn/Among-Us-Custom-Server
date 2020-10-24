package me.alexisevelyn.crewmate.api;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class PluginLoader {

    private static final List<Plugin> plugins = new ArrayList<>();
    private static final List<Plugin> activePlugins = new ArrayList<>();
    private static final List<Plugin> disabledPlugins = new ArrayList<>();

    private static final CrewmateClassLoader classLoader = new CrewmateClassLoader(new URL[]{}, PluginLoader.class.getClassLoader());

    @NotNull
    public static List<Plugin> getActivePlugins() {
        return Collections.unmodifiableList(activePlugins);
    }

    @NotNull
    public static List<Plugin> getDisabledPlugins() {
        return Collections.unmodifiableList(disabledPlugins);
    }

    @NotNull
    public static List<Plugin> getPlugins() {
        return Collections.unmodifiableList(plugins);
    }

    @NotNull
    public static List<Plugin> loadPlugins(File pluginsFolder, Server server) {
        // Create Plugins Folder If It Does Not Exist
        if (!pluginsFolder.exists()) {
            //noinspection ResultOfMethodCallIgnored
            pluginsFolder.mkdirs();

            return getPlugins();
        }

        // Load Plugins If Folder Exists
        for (File plugin : pluginsFolder.listFiles()) {
            String name = plugin.getName();
            // TODO: Decide And Write
            // The plugin validation should be a separate function for both me.alexisevelyn.crewmate.handlers.plugins.MimePluginDetector and this function to use.
            // Preliminary Plugin Validation Before Parsing Should Be Performed By me.alexisevelyn.crewmate.handlers.plugins.MimePluginDetector.

            if (name.endsWith(".jar")) {
                try {
                    JarFile jar = new JarFile(plugin);
                    ZipEntry pluginJson = jar.getEntry("crewmate.plugin.json");

                    if (pluginJson != null) {
                        InputStream pluginJsonInput = jar.getInputStream(pluginJson);
                        StringBuilder textBuilder = new StringBuilder();

                        try (Reader reader = new BufferedReader(new InputStreamReader
                                (pluginJsonInput, Charset.forName(StandardCharsets.UTF_8.name())))) {

                            int c;
                            while ((c = reader.read()) != -1) {
                                textBuilder.append((char) c);
                            }
                        }

                        JSONObject json = new JSONObject(textBuilder.toString());
                        LogHelper.printLine(json.toString());

                        try {
                            String mainClass = json.getString("main");
                            if (mainClass == null) continue;
                            classLoader.addURL(plugin.toURI().toURL());
                            Plugin newPlugin = findPlugin(mainClass);

                            if (newPlugin != null) {
                                LogHelper.printLine(String.format(Main.getTranslationBundle().getString("registering_plugin"), plugin.getName()));
                                server.getEventBus().register(newPlugin);
                            }
                        } catch (JSONException | ClassNotFoundException ignored) {
                            LogHelper.printLineErr(String.format(Main.getTranslationBundle().getString("registering_plugin_fail"), plugin.getName()));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return getPlugins();
    }

    @Nullable
    private static Plugin findPlugin(@NotNull String className) throws Exception {
        Class<?> aClass = Class.forName(className, false, classLoader);

        if (!aClass.isInterface()) {
            if (Plugin.class.isAssignableFrom(aClass)) {
                Plugin instance = (Plugin) aClass.getDeclaredConstructor().newInstance();
                String id = instance.getID();

                if (id != null && !id.isEmpty() && id.toLowerCase().equalsIgnoreCase(id) && id.replaceAll(" ", "").equals(id)) {
                    if (getPlugin(id) == null) {
                        boolean enabled = true; // TODO: Replace with setting of some sort.

                        // Add Plugin to Enabled/Disabled Plugins List
                        if (enabled)
                            activePlugins.add(instance);
                        else
                            disabledPlugins.add(instance);

                        // Add Plugin to Global Plugin List
                        plugins.add(instance);
                        
                        // Call onEnable Method if Plugin Is Allowed To Start
                        if (enabled)
                            instance.onEnable();
                        
                        return instance;
                    } else {
                        throw new NullPointerException(Main.getTranslationBundle().getString("plugin_null"));
                    }
                } else {
                    throw new NullPointerException(Main.getTranslationBundle().getString("plugin_id_null"));
                }
            }
        }

        return null;
    }

    @Nullable
    public static Plugin getPlugin(@NotNull String id){
        for (Plugin plugin : plugins){
            if (plugin.getID().equals(id))
                return plugin;
        }

        return null;
    }
}
