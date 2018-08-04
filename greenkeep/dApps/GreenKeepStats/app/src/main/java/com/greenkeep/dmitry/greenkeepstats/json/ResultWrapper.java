package com.greenkeep.dmitry.greenkeepstats.json;

import java.util.ArrayList;

/**
 * Created by Dmitry on 8/5/2018.
 */

public class ResultWrapper {
    ArrayList<SensorData> rows;
    Boolean more;

    public ResultWrapper() {
    }

    public ArrayList<SensorData> getRows() {
        return rows;
    }

    public void setRows(ArrayList<SensorData> rows) {
        this.rows = rows;
    }

    public Boolean getMore() {
        return more;
    }

    public void setMore(Boolean more) {
        this.more = more;
    }
}
