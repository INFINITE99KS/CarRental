package model;

import java.io.*;
import java.time.LocalDate;

// Data Persistence Layer
// This class handles all File I/O operations.
// It saves the state of the application (Customers, Vehicles, Bookings) to CSV files
// and reloads them when the application starts.
public class DataManager {

    // Define the folder where data will be stored.
    private static final String DATA_FOLDER = "data/";

    // Define file paths. We use .csv (Comma Separated Values) because
    // it is simple, text-based, and can be opened in Excel for debugging.
    private static final String CUSTOMERS_FILE = DATA_FOLDER + "customers.csv";
    private static final String VEHICLES_FILE = DATA_FOLDER + "vehicles.csv";
    private static final String BOOKINGS_FILE = DATA_FOLDER + "bookings.csv";

    // Static Initialization Block
    // Runs once when the class is loaded. Ensures the "data/" directory exists.
    // If we didn't do this, the file writers would crash on a fresh installation.
    static {
        File dataDir = new File(DATA_FOLDER);
        if (!dataDir.exists()) {
            dataDir.mkdirs(); // Create directory if missing
        }
    }

    // --- Public Facade Methods ---
    // These are the simple buttons the rest of the app pushes to save/load everything.

    public static void saveAllData() {
        saveCustomers();
        saveVehicles();
        saveBookings();
        System.out.println("All data saved successfully to CSV!");
    }

    public static void loadAllData() {
        loadCustomers();
        loadVehicles();
        loadBookings();
        System.out.println("All data loaded successfully from CSV!");
    }

    // --- CUSTOMERS SAVING/LOADING ---

    private static void saveCustomers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer customer : Customer.customers) {
                // SAFETY CRITICAL:
                // Since we use commas as delimiters, if a user puts a comma in their name
                // (e.g., "Doe, John"), it breaks the file format.
                // We use .replace(",", " ") to sanitize inputs before writing.
                writer.println(customer.getCustomerId() + "," +
                        customer.getName().replace(",", " ") + "," +
                        customer.getEmail().replace(",", " ") + "," +
                        customer.getAccount().getUsername().replace(",", " ") + "," +
                        customer.getAccount().getPassword() + "," +
                        customer.getAccount().getRole());
            }
        } catch (IOException e) {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }

    private static void loadCustomers() {
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) return; // Stop if no data exists yet

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Customer.customers.clear(); // Clear memory to avoid duplicates

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Basic validation to ensure the line isn't corrupted
                if (parts.length >= 6) {
                    String name = parts[1];
                    String email = parts[2];
                    String username = parts[3];
                    String password = parts[4];
                    char role = parts[5].charAt(0);

                    // Reconstruct the Account and Customer objects in memory
                    Account account = new Account(username, password, role);
                    new Customer(name, email, account);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
    }

    // --- VEHICLES SAVING/LOADING ---
    // This is trickier because we have different types (Car, Bike, Van) with different data fields.

    private static void saveVehicles() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(VEHICLES_FILE))) {
            for (Vehicle vehicle : Vehicle.allVehicles) {
                // 1. Get the type dynamically (Car, Bike, etc.)
                String type = vehicle.getClass().getSimpleName();

                // 2. Write the common data (ID, Model, Rate, etc.)
                writer.println(type + "," +
                        vehicle.getVehicleId() + "," +
                        vehicle.getModel().replace(",", " ") + "," +
                        vehicle.getLicenseNumber().replace(",", " ") + "," +
                        vehicle.getDailyRate() + "," +
                        vehicle.getIsAvailable());

                // 3. Polymorphic Saving:
                // Check specific type and write a second line with unique data.
                if (vehicle instanceof Car) {
                    writer.println("CAR_DATA," + ((Car) vehicle).getIsAutmatic());
                } else if (vehicle instanceof Bike) {
                    writer.println("BIKE_DATA," + ((Bike) vehicle).getHelmetInclude());
                } else if (vehicle instanceof Van) {
                    writer.println("VAN_DATA," + ((Van) vehicle).getLoadCapacityInclude());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving vehicles: " + e.getMessage());
        }
    }

    private static void loadVehicles() {
        File file = new File(VEHICLES_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Vehicle.allVehicles.clear();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Check if this is a "Parent" line (starts with Car, Bike, or Van)
                if (parts.length >= 6 && !line.startsWith("CAR_DATA") &&
                        !line.startsWith("BIKE_DATA") && !line.startsWith("VAN_DATA")) {

                    String type = parts[0];
                    String model = parts[2];
                    String license = parts[3];
                    double rate = Double.parseDouble(parts[4]);
                    boolean available = Boolean.parseBoolean(parts[5]);

                    // Read the NEXT line immediately to get the specific data (Automatic, Helmet, etc.)
                    String dataLine = reader.readLine();
                    if (dataLine != null) {
                        String[] dataParts = dataLine.split(",");

                        // Factory Logic: Instantiate the correct subclass
                        if ("Car".equals(type)) {
                            boolean isAutomatic = Boolean.parseBoolean(dataParts[1]);
                            Car car = new Car(model, license, rate, isAutomatic);
                            car.setIsAvailable(available); // Restore availability state
                        } else if ("Bike".equals(type)) {
                            boolean helmet = Boolean.parseBoolean(dataParts[1]);
                            Bike bike = new Bike(model, license, rate, helmet);
                            bike.setIsAvailable(available);
                        } else if ("Van".equals(type)) {
                            double capacity = Double.parseDouble(dataParts[1]);
                            Van van = new Van(model, license, rate, capacity);
                            van.setIsAvailable(available);
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading vehicles: " + e.getMessage());
        }
    }

    // --- BOOKINGS SAVING/LOADING ---
    // Bookings link Customers and Vehicles together.
    // In the file, we only save the IDs. In memory, we need the actual Objects.

    private static void saveBookings() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking booking : Booking.bookings) {
                // Save IDs instead of full objects to keep file small
                writer.println(booking.getBookingId() + "," +
                        booking.getStartDate() + "," +
                        booking.getEndDate() + "," +
                        booking.getCustomer().getCustomerId() + "," +
                        booking.getBookedVehicle().getVehicleId() + "," +
                        booking.isActive());
            }
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }

    private static void loadBookings() {
        File file = new File(BOOKINGS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Booking.bookings.clear();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 6) {
                    LocalDate startDate = LocalDate.parse(parts[1]);
                    LocalDate endDate = LocalDate.parse(parts[2]);
                    int customerId = Integer.parseInt(parts[3]);
                    int vehicleId = Integer.parseInt(parts[4]);
                    boolean isActiveFromFile = Boolean.parseBoolean(parts[5]);

                    // RELATIONAL MAPPING:
                    // We have IDs (e.g., Customer #5, Vehicle #2).
                    // We must find the actual Java objects in the lists we just loaded.
                    Customer customer = findCustomerById(customerId);
                    Vehicle vehicle = findVehicleById(vehicleId);

                    if (customer != null && vehicle != null) {
                        // Recreate the Booking Object
                        Booking booking = new Booking(startDate, endDate, customer, vehicle);

                        // Restore State:
                        // 1. Set booking active/inactive status
                        booking.setIsActive(isActiveFromFile);

                        // 2. Sync Vehicle Availability
                        // If the booking is still active, the car MUST be unavailable.
                        // If the booking is done, the car MUST be available.
                        if (!isActiveFromFile) {
                            vehicle.setIsAvailable(true);
                        } else {
                            vehicle.setIsAvailable(false);
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
        }
    }

    // --- HELPER METHODS ---
    // These act like "Database Lookups" to find objects by their unique ID.

    private static Customer findCustomerById(int id) {
        for (Customer customer : Customer.customers) {
            if (customer.getCustomerId() == id) return customer;
        }
        return null; // Should ideally handle this case (e.g., deleted users)
    }

    private static Vehicle findVehicleById(int id) {
        for (Vehicle vehicle : Vehicle.allVehicles) {
            if (vehicle.getVehicleId() == id) return vehicle;
        }
        return null;
    }
}