package me.alexisevelyn.crewmate.api;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.handlers.plugins.JarHelper;
import me.alexisevelyn.crewmate.handlers.plugins.MimePluginDetector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
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
            try {
                // Check if valid Crewmate Plugin
                final long startTime = System.currentTimeMillis();
                String fileMimeType = Files.probeContentType(plugin.toPath());
                final long stopTime = System.currentTimeMillis();

                LogHelper.printLine(String.format(Main.getTranslationBundle().getString("timing_file_output"), plugin.getName(), (stopTime - startTime)));

                if (fileMimeType != null && fileMimeType.equals(MimePluginDetector.mimeType)) {
                    JarFile pluginJar = new JarFile(plugin);
                    JarEntry manifestEntry = pluginJar.getJarEntry(MimePluginDetector.manifestFileName);

                    String manifestContents = JarHelper.readTextJarEntry(pluginJar, manifestEntry);

                    JSONObject json = new JSONObject(manifestContents);
                    String mainClass = json.getString(MimePluginDetector.mainClassKey);

                    // Debug String
                    LogHelper.printLine(json.toString());

                    if (mainClass == null)
                        continue;

                    classLoader.addURL(plugin.toURI().toURL());
                    Plugin newPlugin = findPlugin(mainClass);

                    if (newPlugin != null) {
                        LogHelper.printLine(String.format(Main.getTranslationBundle().getString("registering_plugin"), plugin.getName()));
                        server.getEventBus().register(newPlugin);
                    }
                }
            } catch (JSONException | ClassNotFoundException | IOException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                LogHelper.printLineErr(String.format(Main.getTranslationBundle().getString("registering_plugin_fail"), plugin.getName()));
            }
        }

        return getPlugins();
    }

    @Nullable
    // This has the most exceptions I've seen being passed out of a method. I'll probably have to rewrite this
    private static Plugin findPlugin(@NotNull String className) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
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
