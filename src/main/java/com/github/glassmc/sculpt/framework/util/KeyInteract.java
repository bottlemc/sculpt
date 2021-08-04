package com.github.glassmc.sculpt.framework.util;

import com.github.glassmc.sculpt.framework.element.Element;

public class KeyInteract<T extends Element> {

    private final T element;
    private final char character;

    public KeyInteract(T element, char character) {
        this.element = element;
        this.character = character;
    }

    public Element getElement() {
        return element;
    }

    public char getCharacter() {
        return character;
    }

}
