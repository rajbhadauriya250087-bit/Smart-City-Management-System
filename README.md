# Smart City Management System — Java GUI + Web

College project starter designed to demonstrate the supplied Java GUI and Java Web marking rubrics.

## Modules
- Civic issue reporting: report potholes, garbage, broken streetlights, water leaks and other city issues.
- Issue tracking: list and search issues, update status and delete records.
- Desktop application: Java Swing interface with background refresh using SwingWorker.
- Web application: Jakarta Servlet + JSP.
- MySQL persistence through JDBC and PreparedStatement.
- OOP: abstract base class, inheritance, polymorphism, interface-driven repository, custom exception.
- Collections & generics: List<CityIssue>, ArrayList<CityIssue>.
- Multithreading/synchronization: background desktop loading and synchronized repository methods.

## Requirements
JDK 17+, Maven 3.9+, MySQL 8+, Apache Tomcat 10.1+.

## Setup
1. Run `database/schema.sql` in MySQL.
2. Configure environment variables:
   - `DB_URL` default `jdbc:mysql://localhost:3306/smart_city`
   - `DB_USER` default `root`
   - `DB_PASSWORD` your local MySQL password (do not commit it).
3. Build with `mvn clean package`.
4. Deploy `target/smart-city-management.war` to Tomcat.
5. Visit `http://localhost:8080/smart-city-management/issues`.
6. To run the desktop GUI, launch `edu.project.smartcity.ui.SmartCityDesktopApp` from your IDE.

This is a source-code starter; compile and test it with your own MySQL/Tomcat setup before submission.
