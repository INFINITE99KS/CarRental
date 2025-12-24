package model;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DataManager {
    private static final String DATA_FOLDER = "data/";
    private static final String CUSTOMERS_FILE = DATA_FOLDER + "customers.txt";
    private static final String VEHICLES_FILE = DATA_FOLDER + "vehicles.txt";
    private static final String BOOKINGS_FILE = DATA_FOLDER + "bookings.txt";

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
        System.out.println("All data saved successfully!");
    }

    public static void loadAllData() {
        loadCustomers();
        loadVehicles();
        loadBookings();
        System.out.println("All data loaded successfully!");
    }

    private static void saveCustomers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer customer : Customer.customers) {
                writer.println(customer.getCustomerId() + "|" + 
                              customer.getName() + "|" + 
                              customer.getEmail() + "|" + 
                              customer.getAccount().getUsername() + "|" + 
                              getPassword(customer.getAccount()) + "|" + 
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
            Customer.customers.clear(); // مسح البيانات الحالية
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    String name = parts[1];
                    String email = parts[2];
                    String username = parts[3];
                    String password = parts[4];
                    char role = parts[5].charAt(0);
                    
                    // إنشاء حساب بكلمة المرور الصحيحة
                    Account account = new Account(username, password, role);
                    new Customer(name, email, account);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
    }

    private static void saveVehicles() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(VEHICLES_FILE))) {
            for (Vehicle vehicle : Vehicle.allVehicles) {
                String type = vehicle.getClass().getSimpleName();
                writer.println(type + "|" + 
                              vehicle.getVehicleId() + "|" + 
                              vehicle.getModel() + "|" + 
                              vehicle.getLicenseNumber() + "|" +
                              vehicle.getDailyRate() + "|" + 
                              vehicle.getIsAvailable());
                
                // حفظ البيانات الخاصة بكل نوع
                if (vehicle instanceof Car) {
                    Car car = (Car) vehicle;
                    writer.println("CAR_DATA|" + car.getIsAutmatic());
                } else if (vehicle instanceof Bike) {
                    Bike bike = (Bike) vehicle;
                    writer.println("BIKE_DATA|" + bike.getHelmetInclude());
                } else if (vehicle instanceof Van) {
                    Van van = (Van) vehicle;
                    writer.println("VAN_DATA|" + van.getLoadCapacityInclude());
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
            Vehicle.allVehicles.clear(); // مسح البيانات الحالية
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6 && !line.startsWith("CAR_DATA") && 
                    !line.startsWith("BIKE_DATA") && !line.startsWith("VAN_DATA")) {
                    
                    String type = parts[0];
                    String model = parts[2];
                    String license = parts[3];
                    double rate = Double.parseDouble(parts[4]);
                    boolean available = Boolean.parseBoolean(parts[5]);
                    
                    // قراءة البيانات الخاصة
                    String dataLine = reader.readLine();
                    if (dataLine != null) {
                        String[] dataParts = dataLine.split("\\|");
                        
                        if ("Car".equals(type) && dataParts[0].equals("CAR_DATA")) {
                            boolean isAutomatic = Boolean.parseBoolean(dataParts[1]);
                            Car car = new Car(model, license, rate, isAutomatic);
                            car.setIsAvailable(available);
                        } else if ("Bike".equals(type) && dataParts[0].equals("BIKE_DATA")) {
                            boolean helmetInclude = Boolean.parseBoolean(dataParts[1]);
                            Bike bike = new Bike(model, license, rate, helmetInclude);
                            bike.setIsAvailable(available);
                        } else if ("Van".equals(type) && dataParts[0].equals("VAN_DATA")) {
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
    

    private static void saveBookings() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking booking : Booking.bookings) {
                writer.println(booking.getBookingId() + "|" + 
                              booking.getStartDate() + "|" + 
                              booking.getEndDate() + "|" + 
                              booking.getCustomer().getCustomerId() + "|" + 
                              booking.getBookedVehicle().getVehicleId() + "|" + 
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
            Booking.bookings.clear(); // مسح البيانات الحالية

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    LocalDate startDate = LocalDate.parse(parts[1]);
                    LocalDate endDate = LocalDate.parse(parts[2]);
                    int customerId = Integer.parseInt(parts[3]);
                    int vehicleId = Integer.parseInt(parts[4]);
                    boolean isActiveFromFile = Boolean.parseBoolean(parts[5]); // Read status from file

                    // البحث عن العميل والمركبة
                    Customer customer = findCustomerById(customerId);
                    Vehicle vehicle = findVehicleById(vehicleId);

                    if (customer != null && vehicle != null) {
                        // 1. Create the booking (Constructor sets isActive = true by default)
                        Booking booking = new Booking(startDate, endDate, customer, vehicle);

                        // 2. CRITICAL FIX: Override the status using the value from the file
                        booking.setIsActive(isActiveFromFile);

                        // 3. Sync vehicle availability
                        if (!isActiveFromFile) {
                            // If the booking is essentially "History" (false), ensure vehicle is free
                            vehicle.setIsAvailable(true);
                        } else {
                            // If it is actually "Active", ensure vehicle is taken
                            vehicle.setIsAvailable(false);
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
        }
    }
    

    private static Customer findCustomerById(int id) {
        for (Customer customer : Customer.customers) {
            if (customer.getCustomerId() == id) {
                return customer;
            }
        }
        return null;
    }

    private static Vehicle findVehicleById(int id) {
        for (Vehicle vehicle : Vehicle.allVehicles) {
            if (vehicle.getVehicleId() == id) {
                return vehicle;
            }
        }
        return null;
    }

    private static String getPassword(Account account) {
        return account.getPassword();
    }
}