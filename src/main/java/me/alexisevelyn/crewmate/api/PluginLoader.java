package me.alexisevelyn.crewmate.api;

import me.alexisevelyn.crewmate.Server;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
                        Enumeration<JarEntry> entries = jar.entries();

                        classLoader.addURL(file.toURI().toURL());

                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String entryName = entry.getName();

                            Plugin newPlugin = findPlugin(entryName);
                            if (newPlugin != null) {
                                server.getEventBus().register(newPlugin);
                                break;
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
        if (className != null && className.endsWith(".class") && !className.contains("$")) {
            String actualClassName = className.substring(0, className.length() - 6).replace("/", ".");
            Class<?> aClass = Class.forName(actualClassName, false, classLoader);

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
