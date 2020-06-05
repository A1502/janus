package com.wuxian.janus.struct.primary;

public abstract class BaseIdType<T extends Comparable> implements Comparable {

    protected T value;

    public BaseIdType(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public abstract void setStringValue(String format);

    public boolean valueEquals(T value) {
        if (this.value != null) {
            return this.value.equals(value);
        } else {
            return value == null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else {
            if (obj instanceof BaseIdType) {
                BaseIdType tmp = (BaseIdType) obj;
                if (this.value != null) {
                    return this.value.equals(tmp.value);
                } else {
                    return tmp.value == null;
                }
            } else {
                return false;
            }
        }
    }

    @Override
    public int hashCode() {
        if (value == null) {
            return Integer.MIN_VALUE;
        }
        return value.hashCode();
    }

    @Override
    public String toString() {
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    @Override
    public int compareTo(Object o) {
        return this.value.compareTo(((BaseIdType) o).value);
    }
}
