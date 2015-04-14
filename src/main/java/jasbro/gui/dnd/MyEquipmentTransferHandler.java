package jasbro.gui.dnd;

import jasbro.Jasbro;
import jasbro.game.items.Equipment;
import jasbro.game.items.Inventory.ItemData;
import jasbro.game.items.Item;
import jasbro.gui.objects.div.EquippedItemPanel;
import jasbro.gui.pictures.ImageUtil;

import java.awt.Component;
import java.awt.Container;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;
import org.imgscalr.Scalr;

public class MyEquipmentTransferHandler extends TransferHandler {
    private final static Logger log = Logger.getLogger(MyEquipmentTransferHandler.class);

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.LINK;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        if (c instanceof JList) {
            @SuppressWarnings("unchecked")
            Item item = (Item)((JList<ItemData>) c).getSelectedValue().getItem();
            if (item instanceof Equipment) {
                Equipment equipment = (Equipment) item;
                setDragImage(ImageUtil.getInstance().getImageResizedSpeed(equipment.getIcon(), 50, 50, Scalr.Mode.AUTOMATIC));
                return new TransferableEquipment(equipment);
            }
            else {
                return null;
            }            
        } else if (c instanceof EquippedItemPanel) {
            EquippedItemPanel equippedItemPanel = (EquippedItemPanel) c;
            Equipment equipment = equippedItemPanel.getItem();
            if (equipment != null) {
                setDragImage(ImageUtil.getInstance().getImageResizedSpeed(equipment.getIcon(), 50, 50, Scalr.Mode.AUTOMATIC));
                return new TransferableEquipment(equipment);
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    private boolean importPossible(Component comp) {
        return comp instanceof CanReceiveEquipmentDrop;
    }

    @Override
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        log.error("Method not supported");
        return false;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        Component comp = support.getComponent();
        boolean canImport = false;
        do {
            canImport = importPossible(comp);
            if (!canImport) {
                comp = comp.getParent();
            }
        } while (canImport == false && comp != null);
        
        if (comp instanceof CanReceiveEquipmentDrop) {
            if (((CanReceiveEquipmentDrop) comp).getEquipmentSlot().getEquipmentType() == getItem(support).getEquipmentType()) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    @Override
    public boolean importData(TransferSupport support) {
        return importData((JComponent) support.getComponent(), support.getTransferable());
    }

    @Override
    public boolean importData(JComponent comp, Transferable t) {
        try {
            Container cont = (Container) comp;
            while (!(cont instanceof CanReceiveEquipmentDrop)) {
                cont = cont.getParent();
            }
            Equipment equipment = (Equipment)Jasbro.getInstance().getItems().get((String) t.getTransferData(t.getTransferDataFlavors()[0]));
            ((CanReceiveEquipmentDrop) cont).receiveEquipmentDrop(equipment);


            return false;
        } catch (Exception e) {
            log.error("Error on importing data", e);
            return false;
        }
    }
    
    public Equipment getItem(TransferSupport support) {
        try {
            return (Equipment)Jasbro.getInstance().getItems().get((String) support.getTransferable().getTransferData(
                    support.getTransferable().getTransferDataFlavors()[0]));
        }
        catch (Exception e) {
            return null;
        }
    }
}
