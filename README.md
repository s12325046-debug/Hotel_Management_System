# Hotel_Management_System
A comprehensive desktop application for end-to-end hotel operations management.Built with Java Swing UI and MySQL persistent storage,the system handles guests &amp;documents, rooms &amp; status, reservations &amp; packages, services &amp; requests,invoices &amp; payments, and reports &amp; user accounts.Designed as a standalone solution for front-desk staff and management
# Hotel Management System

Final Database course project implemented as a Java desktop application using JDBC, Swing, and MySQL.

## Project Overview

The Hotel Management System supports core hotel operations: user login, dashboard monitoring, guest records, guest documents, room types, rooms, room status auditing, reservations, packages, services, service requests, check-in, check-out, invoice generation, payments, reports, and Admin-only user management.

The database keeps the approved 15-table relational design and uses keys, foreign keys, unique constraints, checks, stored procedures, triggers, views, and realistic seed data for a university demo.

## Technology Stack

- Java 17
- Java Swing desktop GUI
- JDBC with `DriverManager`
- MySQL or MariaDB
- Maven
- MySQL Connector/J
- `at.favre.lib:bcrypt` for bcrypt password verification and hashing

## Software Requirements

- JDK 17 or newer
- Maven 3.8 or newer
- MySQL Server or MariaDB Server
- MySQL Workbench or the MySQL command-line client for importing SQL scripts

## Folder Structure

```text
database/                                      SQL schema, seed data, DML examples, views, procedures, triggers
src/main/java/com/hotelmanagement/Main.java    Application entry point
src/main/java/com/hotelmanagement/config/      Database configuration
src/main/java/com/hotelmanagement/util/        JDBC connection helpers and demo password verifier
src/main/java/com/hotelmanagement/model/       Java model classes
src/main/java/com/hotelmanagement/dao/         JDBC DAO classes
src/main/java/com/hotelmanagement/service/     Business rules and transaction workflows
src/main/java/com/hotelmanagement/ui/          Swing screens and dialogs
```

## Database Import Order

Import these scripts in this exact order:

1. `database/schema.sql`
2. `database/procedures.sql`
3. `database/triggers.sql`
4. `database/views.sql`
5. `database/seed.sql`

`database/dml_operations.sql` is a reference/demo file for representative SQL operations. It is not required for application setup.

The schema creates this database:

```sql
hotel_management_system
```

## Database Configuration

Edit `src/main/java/com/hotelmanagement/config/DatabaseConfig.java` if your local MySQL settings are different.

Default settings:

```text
Host: localhost
Port: 3306
Database: hotel_management_system
Username: root
Password: empty string
```

You may also override the defaults with system properties or environment variables:

```text
DB_HOST
DB_PORT
DB_NAME
DB_USER
DB_PASSWORD
```

## Run With Maven

From the project root:

```bash
mvn clean compile
mvn exec:java
```

To verify the three demo bcrypt hashes independently:

```bash
mvn exec:java -Dexec.mainClass=com.hotelmanagement.util.DemoPasswordVerifier
```

### FlatLaf Native Access Warning

When running with a newer JDK, FlatLaf may print a warning similar to:

```text
WARNING: java.lang.System::load has been called by com.formdev.flatlaf.util.NativeLibrary
```

This warning is not blocking and can be ignored. If desired, IntelliJ VM options can include:

```text
--enable-native-access=ALL-UNNAMED
```

## Demo Accounts

| Role | Email | Demo Password |
| --- | --- | --- |
| Admin | admin@hotel.com | Admin123! |
| Manager | manager@hotel.com | Manager123! |
| Reception | reception@hotel.com | Reception123! |

Passwords are stored as bcrypt hashes in `database/seed.sql`. The application never stores or displays plain-text passwords.

## Implemented Modules

- Login
- Dashboard
- Guests
- Guest Documents
- Room Types
- Rooms
- Room Status Log
- Reservations with optional package selection
- Services
- Packages
- Package-Service many-to-many management
- Service Requests
- Check-In
- Check-Out with automatic Invoice and InvoiceItem generation
- Payments
- Reports
- Admin-only User Management

## Chapter 5 JDBC Alignment

- JDBC connection: `DatabaseConnection.getConnection()` uses `DriverManager.getConnection(...)`.
- `PreparedStatement`: DAO classes use parameterized SQL for CRUD, searches, filters, reports, and transaction reads/writes.
- `ResultSet`: DAO classes map database rows into model objects.
- `ResultSetMetaData`: `ReportDAO` builds dynamic report tables from query metadata.
- `CallableStatement`: `RoomStatusService.changeRoomStatus(...)` calls `sp_record_room_status_change`.
- Transaction control: `ReservationService`, `CheckInService`, `CheckOutService`, and `PaymentService` use `setAutoCommit(false)`, `commit()`, and `rollback()`.
- Stored procedures: `database/procedures.sql` includes simple procedures for available rooms, room status changes, and invoice balance.
- Triggers: `database/triggers.sql` includes `trg_payment_update_invoice_status`, which updates `Invoice.status` after inserting a `Payment`.
- Direct SQL from Java: DAO classes contain explicit SQL statements without ORM.

## Important Workflows

Reservation creation is transaction-controlled. The service opens one connection, disables auto-commit, re-checks room availability with the approved overlap rule, inserts `Reservation`, inserts `ReservationItem`, commits on success, and rolls back on failure.

Room status changes use `CallableStatement` to call `sp_record_room_status_change`, which reads the old room status, updates `Room.status`, and inserts a `RoomStatusLog` row.

Check-In is transaction-controlled. It verifies a confirmed reservation, updates it to `Checked-In`, marks the room `Occupied`, writes `RoomStatusLog`, commits on success, and rolls back on failure.

Check-Out is transaction-controlled. It verifies a checked-in reservation, creates an invoice from stored room, package, and service price snapshots, inserts invoice items, marks the reservation `Checked-Out`, marks the room `Cleaning`, writes `RoomStatusLog`, commits on success, and rolls back on failure.

Payment processing is transaction-controlled for balance validation and payment insertion. After a payment is inserted, the database trigger updates invoice status to `Unpaid`, `Partially-Paid`, or `Paid`.

Reports demonstrate analytical SQL with joins, grouping, aggregation, date filtering, ordering, `PreparedStatement`, `ResultSet`, and `ResultSetMetaData`.

User Management is Admin-only. It demonstrates role-based application behavior, CRUD operations on `Users`, and secure BCrypt hashing before passwords are inserted or reset.

## Recommended Demo Scenario

1. Import the database scripts in the required order.
2. Run the application with Maven.
3. Log in as Admin.
4. Review the Dashboard.
5. Create a guest and add a guest document.
6. Create or edit a room type and room.
7. Change a room status and show the status log.
8. Create a service and package, then assign services to the package.
9. Create a reservation with a room and optional package.
10. Add a service request for the reservation.
11. Check in the reservation.
12. Check out the reservation and generate an invoice.
13. Add a payment and observe the trigger-updated invoice status.
14. Run reports.
15. Manage users as Admin.

## Final Testing Checklist

- Import `schema.sql`.
- Import `procedures.sql`.
- Import `triggers.sql`.
- Import `views.sql`.
- Import `seed.sql`.
- Run `mvn clean compile`.
- Run `mvn exec:java`.
- Log in as `admin@hotel.com`.
- Create a guest.
- Add a guest document.
- Create or edit a room type.
- Create or edit a room.
- Change room status and verify `RoomStatusLog`.
- Create or edit a service.
- Create or edit a package.
- Assign a service to a package.
- Create a reservation.
- Add a service request.
- Check in the reservation.
- Check out the reservation and verify invoice items.
- Add a payment and verify invoice status changes.
- Run each report.
- Add, edit, deactivate, reset password for, and delete users where allowed.

## Known Limitations

- The application is a local desktop academic project, not a multi-user production system.
- Reservation creation supports one room per reservation.
- Guest document `file_url` is stored as text; real file upload is not implemented.
- Standalone invoice editing is intentionally not included. Invoices are generated during Check-Out and paid through Payments.
- Payment deletion/refunds are not implemented; the trigger demonstrates status updates after payment insertion.
- Advanced authorization is limited to Admin-only User Management; operational modules are intentionally simple for the course demo.
