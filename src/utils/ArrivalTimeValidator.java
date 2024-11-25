package utils;

public class ArrivalTimeValidator implements Validator{

    public ArrivalTimeValidator() {
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
