package com.hasan.storyvibrance.OnBoard;

public class ViewPagerItem {
    private String heading, description;

    public ViewPagerItem(String heading, String description) {
        this.heading = heading;
        this.description = description;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }
}

