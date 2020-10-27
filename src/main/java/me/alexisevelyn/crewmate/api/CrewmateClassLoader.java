package me.alexisevelyn.crewmate.api;

import java.net.URL;
import java.net.URLClassLoader;

public class CrewmateClassLoader extends URLClassLoader {
    @Deprecated
    public CrewmateClassLoader(URL[] urls, ClassLoader parent) {
        this(parent, urls);
    }

    public CrewmateClassLoader(ClassLoader parent, URL... urls) {
        super(urls, parent);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
}
