package Backend.database.Review;

import Backend.database.Food.Food;
import Backend.database.Profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findById(int id);
    boolean existsById(int id);

    @Transactional
    void deleteById(int id);
}
