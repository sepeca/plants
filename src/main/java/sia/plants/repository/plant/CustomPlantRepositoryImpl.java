package sia.plants.repository.plant;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class CustomPlantRepositoryImpl implements CustomPlantRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void createSmartPlantWithImages(
            String plantName,
            String species,
            UUID organizationId,
            String locationName,
            String categoryName,
            String humidity,
            String lightRequirements,
            String water,
            String temperatureRange,
            List<String> imageUrls
    ) {
        Session session = entityManager.unwrap(Session.class);

        session.doWork(connection ->{
        try (CallableStatement stmt = connection.prepareCall(
                "CALL create_smart_plant_with_images(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            stmt.setString(1, plantName);
            stmt.setString(2, species);
            stmt.setObject(3, organizationId);
            stmt.setString(4, locationName);
            stmt.setString(5, categoryName);
            stmt.setString(6, humidity);
            stmt.setString(7, lightRequirements);
            stmt.setString(8, water);
            stmt.setString(9, temperatureRange);

            if(imageUrls != null) {
                Array urlArray = connection.createArrayOf("text", imageUrls.toArray());
                stmt.setArray(10, urlArray);
            }else{
                stmt.setNull(10, java.sql.Types.ARRAY);
            }
            stmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute stored procedure: " + e.getMessage(), e);
        }
        });
    }
    @Override
    @Transactional
    public void deletePlantCascade(Integer plantId) {
        Session session = entityManager.unwrap(Session.class);
        session.doWork(connection -> {
            try (CallableStatement stmt = connection.prepareCall("CALL delete_plant_cascade(?)")) {
                stmt.setInt(1, plantId);
                stmt.execute();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to delete plant: " + e.getMessage(), e);
            }
        });
    }


}
