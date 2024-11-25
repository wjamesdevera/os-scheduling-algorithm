package utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public int requestInput(String prompt, Validator validator) {
        int input;
        do {
            input = readValidatedInput(prompt, validator);
        } while (input == -1);
        return input;
    }

    private int readValidatedInput(String prompt, Validator validator) {
        int input = safeReadInt(prompt);
        return  validateInput(input, validator) ? input : -1;
    }

    private boolean validateInput(int input, Validator validator) {
        if (validator.isValid(input)) {
            return true;
        } else {
            System.out.println(validator.getErrorMessage());
            return false;
        }
    }

    private int safeReadInt(String prompt) {
        try {
            System.out.print(prompt);
            int input = scanner.nextInt();
            scanner.nextLine();
            return input;
        } catch (InputMismatchException e) {
            System.out.println("Error: Please enter a valid integer.");
            scanner.next();
            return -1;
        }
    }
}
