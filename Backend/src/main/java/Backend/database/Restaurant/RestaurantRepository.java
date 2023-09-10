package Backend.database.Restaurant;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long>
{
    Restaurant findById(long id);
    boolean existsById(long id);
    Restaurant findBySlug(String slug);
}
