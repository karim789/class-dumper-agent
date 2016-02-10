package org.toto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Agent {

	public static void premain(final String agentArgs, Instrumentation inst) {

		String[] split = agentArgs.split(",");

		final String pacageRegExp = split[0];
		System.out.println("Agent:packageRegExp= " + pacageRegExp);
		final String path = (split.length > 1) ? split[1] : null;
		System.out.println("Agent:path= " + path);

		inst.addTransformer(new ClassFileTransformer() {

			public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
					ProtectionDomain protectionDomain, byte[] classfileBuffer)
					throws IllegalClassFormatException {

				className = className.replace('/', '.');
				className = className.replace('$', '.');

				if (className.matches(pacageRegExp) || interfacesMatch(classBeingRedefined, pacageRegExp)) {
					try {
						FileOutputStream fileOS;
						System.out.println("writing " + className);
						File file = new File(path, className + ".class");
						fileOS = new FileOutputStream(file);
						fileOS.write(classfileBuffer);
						fileOS.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("not writting " + className
							+ " because class or inteface doesn't match " + agentArgs);
				}

				return classfileBuffer;
			}

		});
	}

	public static boolean interfacesMatch(Class<?> clazz, String pacageRegExp) {
		for (Class<?> interfaze : clazz.getInterfaces()) {
			if (interfaze.getName().matches(pacageRegExp)) {
				return true;
			}
		}
		return false;

	}
}
