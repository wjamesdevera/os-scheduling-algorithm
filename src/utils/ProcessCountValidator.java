package utils;

public class ProcessCountValidator implements Validator {
    private final int min;
    private final int max;

    public ProcessCountValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean isValid(int input) {
        return input >= min && input <= max;
    }

    @Override
    public String getErrorMessage() {
        String ERROR_MESSAGE_FORMAT = "Error: Please enter a number between %d and %d.";
        return String.format(ERROR_MESSAGE_FORMAT, min, max);
    }
}
