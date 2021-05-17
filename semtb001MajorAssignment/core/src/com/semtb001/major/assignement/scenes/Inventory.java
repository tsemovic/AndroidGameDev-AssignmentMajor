package com.semtb001.major.assignement.scenes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// Class for the players inventory
public class Inventory {

    // HashMap to hold the inventory items
    private HashMap<String, Integer> inventory;

    public Inventory() {

        // Instantiate the inventory and add items and their quantity (eg. 5 seeds, 1 hoe)
        inventory = new HashMap<String, Integer>();
        inventory.put("hoe", 1);
        inventory.put("wateringCan", 1);
        inventory.put("seeds", 5);

    }

    // Method to add items to the inventory
    public void addItem(String name, Integer value) {

        // Loop through the inventory
        Iterator itemIterator = inventory.entrySet().iterator();
        while (itemIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) itemIterator.next();

            // If the inventory item is equal to the provided name
            if (mapElement.getKey() == name) {

                // Increase the value of the inventory item by the provided value
                mapElement.setValue((int) mapElement.getValue() + value);
            }
        }
    }

    // Method to get the value of the item name in the inventory
    public Integer getItemValue(String name) {

        // Instantiate the count (quantity of the items)
        Integer count = null;

        // Loop through the items in the inventory
        Iterator itemIterator = inventory.entrySet().iterator();
        while (itemIterator.hasNext()) {

            // If the item name is equal to the provided name
            Map.Entry mapElement = (Map.Entry) itemIterator.next();
            if (mapElement.getKey() == name) {

                // Change the count variable to the item vaue
                count = (Integer) mapElement.getValue();
            }
        }

        // Return the item count
        return count;
    }

    // Method to remove the provided value of the provided item in the inventory
    public void removeItem(String name, Integer value) {

        // Loop through the items in the inventory
        Iterator itemIterator = inventory.entrySet().iterator();
        while (itemIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) itemIterator.next();

            // If the item name is equal to the provided name
            if (mapElement.getKey() == name) {

                // Reduce the item value by the provided amount
                mapElement.setValue((int) mapElement.getValue() - value);
            }
        }
    }

}
