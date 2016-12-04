package parkingControl;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;

public class ForcedSelectionModel extends DefaultListSelectionModel {

    public ForcedSelectionModel () {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void clearSelection() {
    }

    @Override
    public void removeSelectionInterval(int index0, int index1) {
    }

}