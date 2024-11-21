package utils;

public class TimeQuantumValidator implements Validator {

    public TimeQuantumValidator() {
    }

    @Override
    public boolean isValid(int input) {
        return true;
    }

    @Override
    public String getErrorMessage() {
        return "";
    }
}
