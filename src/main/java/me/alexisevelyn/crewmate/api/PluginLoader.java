package me.alexisevelyn.crewmate.api;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Server;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

    public static List<Plugin> getActivePlugins() {
        return Collections.unmodifiableList(activePlugins);
    }

    public static List<Plugin> getDisabledPlugins() {
        return Collections.unmodifiableList(disabledPlugins);
    }

    public static List<Plugin> getPlugins() {
        return Collections.unmodifiableList(plugins);
    }

    public static List<Plugin> loadPlugins(File dir, Server server) {
        if (!dir.exists()) { dir.mkdirs(); }
        else {
            for (File file : dir.listFiles()) {
                String name = file.getName();
                if (name.endsWith(".jar")) {
                    try {
                        JarFile jar = new JarFile(file);
                        ZipEntry pluginJson = jar.getEntry("plugin.json");
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
                                classLoader.addURL(file.toURI().toURL());
                                Plugin newPlugin = findPlugin(mainClass);

                                if (newPlugin != null) {
                                    LogHelper.printLine("Plugin found, registering.");
                                    server.getEventBus().register(newPlugin);
                                }
                            } catch (JSONException | ClassNotFoundException ignored) {
                                LogHelper.printLineErr("[" + file.getName() + "] Main class not found.");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return getPlugins();
    }

    private static Plugin findPlugin(String className) throws Exception {
        Class<?> aClass = Class.forName(className, false, classLoader);

        if (!aClass.isInterface()) {
            if (Plugin.class.isAssignableFrom(aClass)) {
                Plugin instance = (Plugin) aClass.getDeclaredConstructor().newInstance();
                String id = instance.getId();

                if (id != null && !id.isEmpty() && id.toLowerCase().equalsIgnoreCase(id) && id.replaceAll(" ", "").equals(id)) {
                    if (getPlugin(id) == null) {
                        boolean enabled = true; // TODO: Replace with setting of some sort.
                        if (enabled) {
                            activePlugins.add(instance);
                        } else {
                            disabledPlugins.add(instance);
                        }
                        plugins.add(instance);
                        if (enabled) instance.onEnable();
                        return instance;
                    } else {
                        throw new NullPointerException("Plugin was null.");
                    }
                } else {
                    throw new NullPointerException("Plugin ID is incorrect format.");
                }
            }
        }
        return null;
    }

    public static Plugin getPlugin(String id){
        for(Plugin plugin : plugins){
            if(plugin.getId().equals(id)){
                return plugin;
            }
        }
        return null;
    }

}
