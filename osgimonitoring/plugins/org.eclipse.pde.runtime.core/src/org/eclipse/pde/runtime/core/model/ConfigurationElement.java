/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Wojciech Galanciak <wojciech.galanciak@gmail.com> - bug 282804
 *******************************************************************************/
package org.eclipse.pde.runtime.core.model;

public class ConfigurationElement extends Attribute {

	private static final long serialVersionUID = 1L;

	private Attribute[] elements = new Attribute[0];

	public void setElements(Attribute[] elements) {
		if (elements == null)
			throw new IllegalArgumentException();

		this.elements = elements;
	}

	public Attribute[] getElements() {
		return elements;
	}
}
