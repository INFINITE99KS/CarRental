package model;

// Custom Exception Classes for better error handling
public class CustomExceptions {
    
    public static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
        }
    }
    
    public static class VehicleNotAvailableException extends Exception {
        public VehicleNotAvailableException(String message) {
            super(message);
        }
    }
    
    public static class InvalidDateException extends Exception {
        public InvalidDateException(String message) {
            super(message);
        }
    }
    
    public static class DuplicateLicenseException extends Exception {
        public DuplicateLicenseException(String message) {
            super(message);
        }
    }
    
    public static class InvalidVehicleDataException extends Exception {
        public InvalidVehicleDataException(String message) {
            super(message);
        }
    }
}