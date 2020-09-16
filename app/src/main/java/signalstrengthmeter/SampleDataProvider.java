package signalstrengthmeter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SampleDataProvider {

    public static List<DataModel> dataItemList;
    public static Map<String,DataModel> dataItemMap;

    static {
        dataItemList =new ArrayList<>();
        dataItemMap = new HashMap<>();

        addItem(new DataModel(null,"0","home"));
        addItem(new DataModel(null,"0","car"));
        addItem(new DataModel(null,"0","office"));
        addItem(new DataModel(null,"0","earphone"));

    }

    private static void addItem(DataModel model){
        dataItemList.add(model);
        dataItemMap.put(model.getMacId(), model);
    }

}
