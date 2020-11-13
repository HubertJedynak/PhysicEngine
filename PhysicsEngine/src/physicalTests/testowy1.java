package physicalTests;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;

import java.util.HashMap;

public class testowy1 {

    static HashMap<KeyCode, Boolean> keyCodeBooleanHashMap = new HashMap<>();

    public static void main(String[] args) {
        for (KeyCode c : KeyCode.values()) {
            keyCodeBooleanHashMap.put(c, false);
        }
        for (KeyCode c : KeyCode.values()) {
            System.out.println(keyCodeBooleanHashMap.get(c));
        }


    }

}
