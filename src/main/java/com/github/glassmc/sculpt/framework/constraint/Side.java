package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Pair;
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
        public double getXValue(ElementData elementData, List<ElementData> appliedElements) {
            Side.Direction direction = this.getComponent().getDirection();

            Pair<Double, Double> maxLeftRight = this.getMaximumExtension(elementData, appliedElements, Axis.X);
            if(maxLeftRight != null) {
                if(direction == Side.Direction.NEGATIVE) {
                   return maxLeftRight.getKey() + elementData.getWidth() / 2;
                } else if(direction == Side.Direction.ZERO) {
                    return (maxLeftRight.getKey() + maxLeftRight.getValue()) / 2;
                } else if(direction == Side.Direction.POSITIVE) {
                    return maxLeftRight.getValue() - elementData.getWidth() / 2;
                }
            }
            
            return elementData.getX();
        }

        @Override
        public double getYValue(ElementData elementData, List<ElementData> appliedElements) {
            Side.Direction direction = this.getComponent().getDirection();

            Pair<Double, Double> maxTopBottom = this.getMaximumExtension(elementData, appliedElements, Axis.Y);
            if(maxTopBottom != null) {
                if(direction == Side.Direction.NEGATIVE) {
                    return maxTopBottom.getKey() + elementData.getHeight() / 2;
                } else if(direction == Side.Direction.ZERO) {
                    return (maxTopBottom.getKey() + maxTopBottom.getValue()) / 2;
                } else if(direction == Side.Direction.POSITIVE) {
                    return maxTopBottom.getValue() - elementData.getHeight() / 2;
                }
            }

            return elementData.getY();
        }
        
    }

}
