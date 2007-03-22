/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria IBM CAS, IBM Toronto
 * Lab
 ******************************************************************************/
package org.eclipse.pde.visualization.dependency.views;

import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.viewer.EntityConnectionData;
import org.eclipse.mylar.zest.core.viewer.IConnectionStyleProvider;
import org.eclipse.mylar.zest.core.viewer.IEntityStyleProvider;
import org.eclipse.mylar.zest.core.viewer.StaticGraphViewer;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Computes the colour of the labels and connections. This is subclassed
 * depending on the view style used.
 * 
 * @author Ian Bull
 */
abstract class AbstractVisualizationLabelProvider implements VisualizationLabelProvider, IConnectionStyleProvider, IEntityStyleProvider {

	public Color LIGHT_BLUE = new Color(Display.getDefault(), 216, 228, 248);
	public Color DARK_BLUE = new Color(Display.getDefault(), 1, 70, 122);
	public Color GREY_BLUE = new Color(Display.getDefault(), 139, 150, 171);
	public Color LIGHT_BLUE_CYAN = new Color(Display.getDefault(), 213, 243, 255);
	public Color LIGHT_YELLOW = new Color(Display.getDefault(), 255, 255, 206);
	public Color GRAY = new Color(Display.getDefault(), 128, 128, 128);
	public Color LIGHT_GRAY = new Color(Display.getDefault(), 220, 220, 220);
	public Color BLACK = new Color(Display.getDefault(), 0, 0, 0);
	public Color RED = new Color(Display.getDefault(), 255, 0, 0);
	public Color DARK_RED = new Color(Display.getDefault(), 127, 0, 0);
	public Color ORANGE = new Color(Display.getDefault(), 255, 196, 0);
	public Color YELLOW = new Color(Display.getDefault(), 255, 255, 0);
	public Color GREEN = new Color(Display.getDefault(), 0, 255, 0);
	public Color DARK_GREEN = new Color(Display.getDefault(), 0, 127, 0);
	public Color LIGHT_GREEN = new Color(Display.getDefault(), 96, 255, 96);
	public Color CYAN = new Color(Display.getDefault(), 0, 255, 255);
	public Color BLUE = new Color(Display.getDefault(), 0, 0, 255);
	public Color WHITE = new Color(Display.getDefault(), 255, 255, 255);
	public Color EDGE_WEIGHT_0 = new Color(Display.getDefault(), 192, 192, 255);
	public Color EDGE_WEIGHT_01 = new Color(Display.getDefault(), 64, 128, 225);
	public Color EDGE_WEIGHT_02 = new Color(Display.getDefault(), 32, 32, 128);
	public Color EDGE_WEIGHT_03 = new Color(Display.getDefault(), 0, 0, 128);
	public Color EDGE_DEFAULT = new Color(Display.getDefault(), 64, 64, 128);
	public Color EDGE_HIGHLIGHT = new Color(Display.getDefault(), 192, 32, 32);
	public Color DISABLED = new Color(Display.getDefault(), 230, 240, 250);

	private BundleDescription selected = null;
	protected BundleDescription rootNode = null;
	private HashSet interestingRelationships = new HashSet();
	private HashSet interestingDependencies = new HashSet();
	private Color disabledColor = null;
	protected BundleDescription pinnedNode = null;
	private StaticGraphViewer viewer;

	/**
	 * Create a new Abstract Visualization Label Provider
	 * 
	 * @param viewer
	 */
	public AbstractVisualizationLabelProvider(StaticGraphViewer viewer) {
		this.viewer = viewer;
	}

	public Image getImage(Object element) {
		/*
		 * PDELabelProvider fSharedProvider =
		 * PDEPlugin.getDefault().getLabelProvider(); return
		 * fSharedProvider.get(PDEPluginImages.DESC_PLUGIN_OBJ, 0);
		 */
		return null;
	}

	public String getText(Object element) {
		if (element instanceof BundleDescription) {
			return ((BundleDescription) element).getName();
		}
		return "";
	}

	public void addListener(ILabelProviderListener listener) {

	}

	public boolean isLabelProperty(Object element, String property) {

		return false;
	}

	public void removeListener(ILabelProviderListener listener) {

	}

	public Color getColor(Object rel) {
		if (interestingRelationships.contains(rel)) {
			return DARK_RED;
		}
		return LIGHT_GRAY;
	}

	public int getConnectionStyle(Object rel) {
		if (interestingRelationships.contains(rel)) {
			return ZestStyles.CONNECTIONS_DASH | ZestStyles.CONNECTIONS_DIRECTED;
		}
		return ZestStyles.CONNECTIONS_DIRECTED;
	}

	public Color getHighlightColor(Object rel) {
		return DARK_RED;
	}

	public Color getNodeHighlightColor(Object entity) {
		return null;
	}

	public int getLineWidth(Object rel) {
		if (interestingRelationships.contains(rel)) {
			return 3;
		}
		return 0;
	}

	public Color getAdjacentEntityHighlightColor(Object entity) {
		return null;
	}

	public Color getBorderColor(Object entity) {
		if (this.selected != null || this.pinnedNode != null) {
			if (entity == this.selected || entity == this.pinnedNode) {
				// If this is the selected node return no colour. The default
				// selected colour is fine.
				return BLACK;
			} else if (interestingDependencies.contains(entity)) {
				// If this entity is directly connected to the selected entity
				return BLACK;
			} else {
				return LIGHT_GRAY;
			}

		}

		return BLACK;
	}

	public Color getBorderHighlightColor(Object entity) {
		return null;
	}

	public int getBorderWidth(Object entity) {
		return 0;
	}

	public boolean highlightAdjacentEntities(Object entity) {
		return false;
	}

	public Color getBackgroundColour(Object entity) {

		if (entity == this.selected || this.pinnedNode == entity) {
			return viewer.getGraphControl().DEFAULT_NODE_COLOR;
		} else if (interestingDependencies.contains(entity)) {
			return viewer.getGraphControl().HIGHLIGHT_ADJACENT_COLOR;
		} else {
			return getDisabledColor();
		}
	}

	public Color getForegroundColour(Object entity) {
		if (this.selected != null || this.pinnedNode != null) {
			if (entity == this.selected || this.pinnedNode == entity) {
				// If this is the selected node return no colour. The default
				// selected colour is fine.
				return BLACK;
			} else if (interestingDependencies.contains(entity)) {
				// If this entity is directly connected to the selected entity
				return BLACK;
			} else {
				return GRAY;
			}

		}
		return BLACK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.pde.visualization.views.VisualizationLabelProvider#setPinnedNode(org.eclipse.osgi.service.resolver.BundleDescription)
	 */
	public void setPinnedNode(BundleDescription pinnedNode) {
		this.pinnedNode = pinnedNode;
	}

	protected BundleDescription getSelected() {
		if (pinnedNode != null) {
			return pinnedNode;
		}
		return selected;
	}

	/**
	 * Sets the current selection
	 * 
	 * @param root
	 * @param currentSelection
	 */
	public void setCurrentSelection(Object root, Object currentSelection) {

		for (Iterator iter = interestingRelationships.iterator(); iter.hasNext();) {
			EntityConnectionData entityConnectionData = (EntityConnectionData) iter.next();
			viewer.unReveal(entityConnectionData);
		}

		this.rootNode = (BundleDescription) root;
		this.selected = null;

		this.selected = (BundleDescription) currentSelection;

		interestingRelationships = new HashSet();
		interestingDependencies = new HashSet();
		if (this.selected != null || this.pinnedNode != null) {
			calculateInterestingDependencies(interestingRelationships, interestingDependencies);
		}

		for (Iterator iter = interestingRelationships.iterator(); iter.hasNext();) {
			Object entityConnectionData = iter.next();
			viewer.reveal(entityConnectionData);
		}

	}

	/**
	 * Calculate all the interesting dependencies. Create an instance of this
	 * method to calculate the entities and relationships that should
	 * "stand-out" for this view.
	 * 
	 * @param interestingRels
	 * @param interestingEntities
	 */
	protected abstract void calculateInterestingDependencies(HashSet interestingRels, HashSet interestingEntities);

	public Object[] getInterestingRelationships() {
		return interestingRelationships.toArray();
	}

	/**
	 * Creates a colour for disabled lines.
	 * 
	 * @return
	 */
	private Color getDisabledColor() {
		if (disabledColor == null) {
			disabledColor = new Color(Display.getDefault(), new RGB(225, 238, 255));
		}
		return disabledColor;
	}

	public IFigure getTooltip(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose() {
		if (this.disabledColor != null) {
			this.disabledColor.dispose();
			this.disabledColor = null;
		}
	}

	public IFigure getNodeFigure(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

}