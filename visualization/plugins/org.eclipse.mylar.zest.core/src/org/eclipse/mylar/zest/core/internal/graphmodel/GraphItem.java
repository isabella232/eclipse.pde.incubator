/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.graphmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Widget;

/**
 * Provides support for property changes.  All model elements extend this class.
 * Also extends the Item (Widget) class to be used inside a StructuredViewer.   
 * 
 * @author Chris Callendar
 */
public abstract class GraphItem extends Item implements IGraphItem  {
	 
	/** Delegate used to implemenent property-change-support. */
	private transient PropertyChangeSupport pcsDelegate = new PropertyChangeSupport(this);
	private boolean isVisible;
	/**
	 * @param parent
	 * @param style
	 */
	public GraphItem(Widget parent) {
		super(parent, SWT.NO_BACKGROUND);
		isVisible = true;
	}
	
	
	
	/** 
	 * Attach a non-null PropertyChangeListener to this object.
	 * @param l a non-null PropertyChangeListener instance
	 * @throws IllegalArgumentException if the parameter is null
	 */
	public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
		if (l == null) {
			throw new IllegalArgumentException();
		}
		pcsDelegate.addPropertyChangeListener(l);
	}
	
	/** 
	 * Remove a PropertyChangeListener from this component.
	 * @param l a PropertyChangeListener instance
	 */
	public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
		if (l != null) {
			pcsDelegate.removePropertyChangeListener(l);
		}
	}
	
	/** 
	 * Report a property change to registered listeners (for example edit parts).
	 * @param property the programmatic name of the property that changed
	 * @param oldValue the old value of this property
	 * @param newValue the new value of this property
	 */
	public void firePropertyChange(String property, Object oldValue, Object newValue) {
		if (pcsDelegate.hasListeners(property)) {
			pcsDelegate.firePropertyChange(property, oldValue, newValue);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphItem#isVisible()
	 */
	public boolean isVisible() {
		return isVisible;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphItem#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		boolean old = isVisible();
		this.isVisible = visible;
		if (old ^ visible)
			firePropertyChange(VISIBLE_PROP, new Boolean(old), new Boolean(visible));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose() {
		//@tag zest.bug.167132-ListenerDispose : remove all listeners.
		pcsDelegate = new PropertyChangeSupport(this);
		super.dispose();
	}

}
