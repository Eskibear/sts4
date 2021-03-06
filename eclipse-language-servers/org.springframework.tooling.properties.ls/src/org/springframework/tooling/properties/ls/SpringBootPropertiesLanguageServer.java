/*******************************************************************************
 * Copyright (c) 2016, 2017 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.tooling.properties.ls;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.springframework.tooling.ls.eclipse.commons.JRE;
import org.springframework.tooling.ls.eclipse.commons.STS4LanguageServerProcessStreamConnector;

import static org.springframework.tooling.ls.eclipse.commons.console.preferences.LanguageServerConsolePreferenceConstants.*;

/**
 * @author Martin Lippert
 */
public class SpringBootPropertiesLanguageServer extends STS4LanguageServerProcessStreamConnector {

	public SpringBootPropertiesLanguageServer() {
		super(BOOT_PROPS_SERVER);
		List<String> commands = new ArrayList<>();
		commands.add(JRE.currentJRE().getJavaExecutable());

//		commands.add("-Xdebug");
//		commands.add("-agentlib:jdwp=transport=dt_socket,address=8899,server=y,suspend=n");

		commands.add("-Dlsp.lazy.completions.disable=true");
		commands.add("-Dlsp.completions.indentation.enable=true");

		commands.add("-jar");
		commands.add(getLanguageServerJARLocation());

		String workingDir = getWorkingDirLocation();

		setCommands(commands);
		setWorkingDirectory(workingDir);
	}
	
	protected String getLanguageServerJARLocation() {
		String languageServer = "boot-properties-language-server-" + Constants.LANGUAGE_SERVER_VERSION + ".jar";

		Bundle bundle = Platform.getBundle(Constants.PLUGIN_ID);
		File dataFile = bundle.getDataFile(languageServer);
		Exception error = null;
		try {
			copyLanguageServerJAR(languageServer);
		}
		catch (Exception e) {
			error = e;
		}
		if (!dataFile.exists()) {
			File userHome = new File(System.getProperty("user.home"));
			File locallyBuiltJar = new File(
					userHome, 
					"git/sts4/headless-services/boot-properties-language-server/target/boot-properties-language-server-"+Constants.LANGUAGE_SERVER_VERSION + ".jar"
			);
			if (locallyBuiltJar.exists()) {
				return locallyBuiltJar.getAbsolutePath();
			}
			if (error!=null) {
				error.printStackTrace();
			}
		}
		
		return dataFile.getAbsolutePath();
	}
	
	protected void copyLanguageServerJAR(String languageServerJarName) throws Exception {
		Bundle bundle = Platform.getBundle(Constants.PLUGIN_ID);
		InputStream stream = FileLocator.openStream( bundle, new Path("servers/" + languageServerJarName), false );
		
		File dataFile = bundle.getDataFile(languageServerJarName);
		Files.copy(stream, dataFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

}
