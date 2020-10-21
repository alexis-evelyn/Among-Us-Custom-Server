package me.alexisevelyn.crewmate.api;

import java.net.URL;
import java.net.URLClassLoader;

public class CrewmateClassLoader extends URLClassLoader {

    public CrewmateClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

}
