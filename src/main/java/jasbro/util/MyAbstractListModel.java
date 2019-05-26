/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jasbro.util;

import javax.swing.AbstractListModel;

/**
 *
 * @author Azrael
 */
@SuppressWarnings("rawtypes")
public abstract class MyAbstractListModel extends AbstractListModel{
	public void update(Object source, int index0, int index1) { fireContentsChanged(source, index0, index1); }
}