/*******************************************************************************
 * Copyright (c) 2016 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.commons.yaml.completion;

import org.springframework.ide.vscode.commons.yaml.structure.YamlDocument;

/**
 * Defines a way to obtain a 'toplevel' {@link YamlAssistContext} for a given {@link YamlDocument}
 *
 * @author Kris De Volder
 */
public interface YamlAssistContextProvider {

	YamlAssistContext getGlobalAssistContext(YamlDocument doc);

}