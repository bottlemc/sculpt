package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.Pair;
import com.github.glassmc.sculpt.framework.element.Element;
import com.github.glassmc.sculpt.framework.util.Axis;

import java.util.List;

public class Side extends Constraint {

    private final Direction direction;

    public Side(Direction direction) {
        this.possibleConstructors.add(new Constructor<>());
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public enum Direction {
        NEGATIVE, POSITIVE, ZERO
    }
    
    public static class Constructor<T extends Side> extends Constraint.Constructor<T> {

        @Override
        public double getXValue(List<Element.Constructor<?>> appliedElements) {
            Side.Direction direction = this.getComponent().getDirection();

            Element.Constructor<?> constructor = this.getComponent().getElement().getConstructor();

            Pair<Double, Double> maxLeftRight = this.getMaximumExtension(constructor, appliedElements, Axis.X);
            if(maxLeftRight != null) {
                if(direction == Side.Direction.NEGATIVE) {
                   return maxLeftRight.getKey() + constructor.getWidth() / 2;
                } else if(direction == Side.Direction.ZERO) {
                    return (maxLeftRight.getKey() + maxLeftRight.getValue()) / 2;
                } else if(direction == Side.Direction.POSITIVE) {
                    return maxLeftRight.getValue() - constructor.getWidth() / 2;
                }
            }
            
            return constructor.getX();
        }

        @Override
        public double getYValue(List<Element.Constructor<?>> appliedElements) {
            Side.Direction direction = this.getComponent().getDirection();

            Element.Constructor<?> constructor = this.getComponent().getElement().getConstructor();

            Pair<Double, Double> maxTopBottom = this.getMaximumExtension(constructor, appliedElements, Axis.Y);
            if(maxTopBottom != null) {
                if(direction == Side.Direction.NEGATIVE) {
                    return maxTopBottom.getKey() + constructor.getHeight() / 2;
                } else if(direction == Side.Direction.ZERO) {
                    return (maxTopBottom.getKey() + maxTopBottom.getValue()) / 2;
                } else if(direction == Side.Direction.POSITIVE) {
                    return maxTopBottom.getValue() - constructor.getHeight() / 2;
                }
            }

            return constructor.getY();
        }
        
    }

}
