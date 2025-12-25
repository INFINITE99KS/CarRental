package model;

// Custom Exception Classes to create our own exceptions
public class CustomExceptions {

    //InvalidBookingException
    // Thrown when a booking cannot be processed due to general errors.
    public static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            // Pass the error message up to the standard Exception class to use it with the method getmessage
            super(message);
        }
    }

    //VehicleNotAvailableException
    // Thrown specifically when a user tries to rent a car that is already rented.
    public static class VehicleNotAvailableException extends Exception {
        // Pass the error message up to the standard Exception class
        public VehicleNotAvailableException(String message) {
            super(message);
        }
    }

    //InvalidDateException
    // Thrown during date validation logic in Customer.java.
    // Triggered if Start Date is in the past or End Date is before Start Date or  Date format is unreadable
    public static class InvalidDateException extends Exception {
        // Pass the error message up to the standard Exception class
        public InvalidDateException(String message) {
            super(message);
        }
    }

    //DuplicateLicenseException
    // Thrown in the Admin Dashboard when adding a new vehicle to prevent two cars from having the same License Plate.
    public static class DuplicateLicenseException extends Exception {
        // Pass the error message up to the standard Exception class
        public DuplicateLicenseException(String message) {
            super(message);
        }
    }

    //InvalidVehicleDataException
    // Thrown when Admin inputs are logically wrong.
    // Examples: Negative Daily Rate, Empty Model Name, or Negative Capacity.
    public static class InvalidVehicleDataException extends Exception {
        // Pass the error message up to the standard Exception class
        public InvalidVehicleDataException(String message) {
            super(message);
        }
    }
}