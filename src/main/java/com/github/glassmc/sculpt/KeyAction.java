package com.github.glassmc.sculpt;

import com.github.glassmc.sculpt.framework.backend.Key;

public class KeyAction {

    private final Key key;
    private final char character;

    public KeyAction(Key key, char character) {
        this.key = key;
        this.character = character;
    }

    public Key getKey() {
        return key;
    }

    public char getCharacter() {
        return character;
    }

}
