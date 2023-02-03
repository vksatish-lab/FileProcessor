package com.highbridge.file.domain;

public class Row {

    private int firstNumber;
    private int secondNumber;

    public Row(int firstNumber, int secondNumber) {
        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
    }

    public int getFirstNumber() {
        return firstNumber;
    }

    public int getSecondNumber() {
        return secondNumber;
    }

    @Override
    public String toString() {
        return String.format("First Number - [%d], secondNumber - [%d]", firstNumber, secondNumber);
    }

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }
        if (!(o instanceof Row)) {
            return false;
        }
        Row c = (Row) o;
        // Compare the members and return accordingly
        return this.firstNumber == c.firstNumber && this.secondNumber == c.secondNumber;
    }
}
