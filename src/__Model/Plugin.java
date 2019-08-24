package __Model;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Plugin {
	
	 String path;
	Class<?> loadedClass=null;
	String className;
	private final static Plugin plugin = new Plugin();

    //Singleton DP
	private Plugin() {} // private constructor
	
	public static Plugin getInstance() {
		return plugin; 
	}
	 
   public Class<?> getPlugin(){
    	if(path == null)
    		return null;
    	try {
            @SuppressWarnings("resource")
			JarFile jarFile = new JarFile(path);
    		Enumeration<JarEntry> e = jarFile.entries(); //Byshouf eh elly gowa el jar file
			URL[] urls = { new URL("jar:file:" + path + "!/") };
			URLClassLoader loader = URLClassLoader.newInstance(urls);
    		while(e.hasMoreElements()) {
    			JarEntry JE = (JarEntry)e.nextElement();
    			if(JE.isDirectory()||!JE.getName().endsWith(".class"))
    				continue;
    			className = JE.getName().substring(0, JE.getName().length() - 6);
    			className = className.replace('/', '.');
    			loadedClass = loader.loadClass(className);
                if (Shape.class.isAssignableFrom(loadedClass)&& loadedClass!=null) {
                	return loadedClass;
                }else
                	System.out.println("Not found");
                
    		}
    	}catch (Exception e) {}
    	return null;
    }
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public Class<?> getLoadedClass() {
		return loadedClass;
	}

	public void setLoadedClass(Class<?> loadedClass) {
		this.loadedClass = loadedClass;
	}
    


}
