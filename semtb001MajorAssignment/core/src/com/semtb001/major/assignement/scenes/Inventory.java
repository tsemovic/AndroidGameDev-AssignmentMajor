package com.semtb001.major.assignement.scenes;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Inventory {

    private Stage stage;
    private HashMap<String, Integer> inventory;

    public Inventory(){
        inventory = new HashMap<String, Integer>();

        inventory.put("hoe", 1);
        inventory.put("wateringCan", 1);
        inventory.put("seeds", 5);

    }

    public void addItem(String name, Integer value){
        Iterator itemIterator = inventory.entrySet().iterator();

        while (itemIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)itemIterator.next();

            if(mapElement.getKey() == name){
                mapElement.setValue((int) mapElement.getValue() + value);
            }
        }
    }

    public Integer getItem(String name){
        Integer count = null;
        Iterator itemIterator = inventory.entrySet().iterator();

        while (itemIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)itemIterator.next();

            if(mapElement.getKey() == name){
                count = (Integer) mapElement.getValue();
            }
        }

        return count;
    }

    public void removeItem(String name, Integer value){
        Iterator itemIterator = inventory.entrySet().iterator();
        while (itemIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)itemIterator.next();

            if(mapElement.getKey() == name){
                mapElement.setValue((int) mapElement.getValue() - value);
            }
        }
    }

}
