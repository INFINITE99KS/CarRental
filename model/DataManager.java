package model;

import java.io.*;
import java.time.LocalDate;

// Data Persistence layer, handles I/O, basically what enables the program to not forget about the session it had,
// and save its bookings, customers and everything to the data folder.
public class DataManager {

    // The folder where data will be stored.
    private static final String DATA_FOLDER = "data/";

    // Define file paths. We use .csv because it's simple and can be opened in Excel.
    private static final String CUSTOMERS_FILE = DATA_FOLDER + "customers.csv";
    private static final String VEHICLES_FILE = DATA_FOLDER + "vehicles.csv";
    private static final String BOOKINGS_FILE = DATA_FOLDER + "bookings.csv";

    // A static block, basically something that runs on the JVM calling the class DataManager.
    // It just checks if our folder exists, if it doesn't then it would create that folder.
    static {
        File dataDir = new File(DATA_FOLDER);
        if (!dataDir.exists()) {
            dataDir.mkdirs(); // Create directory if missing
        }
    }

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

    // Customer saving logic.
    private static void saveCustomers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer customer : Customer.customers) {
                // Since we use commas as a way to separate the different fields, if a user enters a comma
                // it may break our writer logic, so what we do is pretty simple, we replace any comma with a space.
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

    // Logic for customer loading.
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

    // Vehicles saving and loading.
    // This is trickier because we have different types (Car, Bike, Van) with different data fields.
    private static void saveVehicles() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(VEHICLES_FILE))) {
            for (Vehicle vehicle : Vehicle.allVehicles) {
                // Get the type dynamically [Car, Van, Bike].
                String type = vehicle.getClass().getSimpleName();

                // Write the common data [ID, Model, Rate, etc...]
                writer.println(type + "," +
                        vehicle.getVehicleId() + "," +
                        vehicle.getModel().replace(",", " ") + "," +
                        vehicle.getLicenseNumber().replace(",", " ") + "," +
                        vehicle.getDailyRate() + "," +
                        vehicle.getIsAvailable());

                // Saving the specific properties of the vehicles.
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

    // Logic for loading vehicles.
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

                    // Read the NEXT line immediately to get the specific data for the specific type.
                    String dataLine = reader.readLine();
                    if (dataLine != null) {
                        String[] dataParts = dataLine.split(",");

                        // Instantiate the correct subclass
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

    // Loading the bookings.
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

                    // Looking for the actual customer and vehicle via their respective IDs.
                    Customer customer = findCustomerById(customerId);
                    Vehicle vehicle = findVehicleById(vehicleId);

                    if (customer != null && vehicle != null) {
                        // Recreate the Booking Object
                        Booking booking = new Booking(startDate, endDate, customer, vehicle);

                        // Restore State:
                        // Set booking active/inactive status
                        booking.setIsActive(isActiveFromFile);

                        // Sync Vehicle Availability
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

    // Looks for the specific customer by their ID.
    private static Customer findCustomerById(int id) {
        for (Customer customer : Customer.customers) {
            if (customer.getCustomerId() == id) return customer;
        }
        return null; // Should ideally handle this case [Deleted users, if such thing exists].
    }

    // Looks for the specific vehicle by its ID.
    private static Vehicle findVehicleById(int id) {
        for (Vehicle vehicle : Vehicle.allVehicles) {
            if (vehicle.getVehicleId() == id) return vehicle;
        }
        return null;
    }
}