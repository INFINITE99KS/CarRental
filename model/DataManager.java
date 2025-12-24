package model;

import java.io.*;
import java.time.LocalDate;

public class DataManager {
    private static final String DATA_FOLDER = "data/";

    // CHANGED: Files are now .csv (Excel/Spreadsheet compatible)
    private static final String CUSTOMERS_FILE = DATA_FOLDER + "customers.csv";
    private static final String VEHICLES_FILE = DATA_FOLDER + "vehicles.csv";
    private static final String BOOKINGS_FILE = DATA_FOLDER + "bookings.csv";

    // Ensure data directory exists
    static {
        File dataDir = new File(DATA_FOLDER);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

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

    // --- CUSTOMERS ---

    private static void saveCustomers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer customer : Customer.customers) {
                // SAFETY: Replace commas in user text with spaces to prevent file errors
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
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Customer.customers.clear();

            while ((line = reader.readLine()) != null) {
                // SPLIT by comma
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String name = parts[1];
                    String email = parts[2];
                    String username = parts[3];
                    String password = parts[4];
                    char role = parts[5].charAt(0);

                    Account account = new Account(username, password, role);
                    new Customer(name, email, account);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
    }

    // --- VEHICLES ---

    private static void saveVehicles() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(VEHICLES_FILE))) {
            for (Vehicle vehicle : Vehicle.allVehicles) {
                String type = vehicle.getClass().getSimpleName();

                writer.println(type + "," +
                        vehicle.getVehicleId() + "," +
                        vehicle.getModel().replace(",", " ") + "," +
                        vehicle.getLicenseNumber().replace(",", " ") + "," +
                        vehicle.getDailyRate() + "," +
                        vehicle.getIsAvailable());

                // Save specific data
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

                // Ensure we are reading a main vehicle line, not a data line
                if (parts.length >= 6 && !line.startsWith("CAR_DATA") &&
                        !line.startsWith("BIKE_DATA") && !line.startsWith("VAN_DATA")) {

                    String type = parts[0];
                    String model = parts[2];
                    String license = parts[3];
                    double rate = Double.parseDouble(parts[4]);
                    boolean available = Boolean.parseBoolean(parts[5]);

                    // Read the next line for specific details
                    String dataLine = reader.readLine();
                    if (dataLine != null) {
                        String[] dataParts = dataLine.split(",");

                        if ("Car".equals(type)) {
                            boolean isAutomatic = Boolean.parseBoolean(dataParts[1]);
                            Car car = new Car(model, license, rate, isAutomatic);
                            car.setIsAvailable(available);
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

    // --- BOOKINGS ---

    private static void saveBookings() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking booking : Booking.bookings) {
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

                    Customer customer = findCustomerById(customerId);
                    Vehicle vehicle = findVehicleById(vehicleId);

                    if (customer != null && vehicle != null) {
                        // Create Booking (Default is Active)
                        Booking booking = new Booking(startDate, endDate, customer, vehicle);

                        // Force status from file (The fix for your "Active" bug)
                        booking.setIsActive(isActiveFromFile);

                        // Sync vehicle availability
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

    // --- HELPERS ---

    private static Customer findCustomerById(int id) {
        for (Customer customer : Customer.customers) {
            if (customer.getCustomerId() == id) return customer;
        }
        return null;
    }

    private static Vehicle findVehicleById(int id) {
        for (Vehicle vehicle : Vehicle.allVehicles) {
            if (vehicle.getVehicleId() == id) return vehicle;
        }
        return null;
    }
}