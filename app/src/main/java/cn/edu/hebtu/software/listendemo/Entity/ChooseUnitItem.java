package cn.edu.hebtu.software.listendemo.Entity;

public class ChooseUnitItem {
    private boolean isSelected;
    private Unit unit;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
