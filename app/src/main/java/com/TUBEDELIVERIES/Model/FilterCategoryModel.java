package com.TUBEDELIVERIES.Model;

public class FilterCategoryModel {
    private String type;
    private String name;
    private boolean isChecked;

    public FilterCategoryModel() {
    }

    public FilterCategoryModel(String type, String name, boolean isChecked) {
        this.type = type;
        this.name = name;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
