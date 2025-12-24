package model;

// Custom Exception Classes
// This class acts as a container for all the specific error types used in the system.
//
// WHY USE THIS?
// Instead of crashing the app with generic errors, these specific exceptions allow
// the Controllers (UI) to catch specific problems and show nice popup messages to the user.
// This satisfies the "Exception Handling" requirement in the project guidelines.
public class CustomExceptions {

    // -------------------------------------------------------------------------
    // 1. InvalidBookingException
    // Thrown when a booking cannot be processed due to general errors.
    // Example Usage: User tries to book a null vehicle or the customer object is missing.
    // -------------------------------------------------------------------------
    public static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message); // Pass the error message up to the standard Exception class
        }
    }

    // -------------------------------------------------------------------------
    // 2. VehicleNotAvailableException
    // Thrown specifically when a user tries to rent a car that is already rented.
    // Captured in CustomerDashboard to show: "Sorry, this car is currently unavailable."
    // -------------------------------------------------------------------------
    public static class VehicleNotAvailableException extends Exception {
        public VehicleNotAvailableException(String message) {
            super(message);
        }
    }

    // -------------------------------------------------------------------------
    // 3. InvalidDateException
    // Thrown during date validation logic in Customer.java.
    // Triggered if:
    // - Start Date is in the past
    // - End Date is before Start Date
    // - Date format is unreadable
    // -------------------------------------------------------------------------
    public static class InvalidDateException extends Exception {
        public InvalidDateException(String message) {
            super(message);
        }
    }

    // -------------------------------------------------------------------------
    // 4. DuplicateLicenseException
    // Thrown in the Admin Dashboard when adding a new vehicle.
    // Ensures data integrity by preventing two cars from having the same License Plate.
    // -------------------------------------------------------------------------
    public static class DuplicateLicenseException extends Exception {
        public DuplicateLicenseException(String message) {
            super(message);
        }
    }

    // -------------------------------------------------------------------------
    // 5. InvalidVehicleDataException
    // Thrown when Admin inputs are logically wrong.
    // Examples: Negative Daily Rate, Empty Model Name, or Negative Capacity.
    // -------------------------------------------------------------------------
    public static class InvalidVehicleDataException extends Exception {
        public InvalidVehicleDataException(String message) {
            super(message);
        }
    }
}