package Backend.database.Food;

import Backend.database.Profile.Profile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long>{

    Food findById(int id);
    boolean existsByName(String name);
    boolean existsBySubRestaurant(String subRestaurant);
    boolean existsByNameAndSubRestaurant(String name, String subRestaurant);
    Food findByNameAndSubRestaurant(String name, String subRestaurant);
    List<Food> findAllByIsServed(boolean isServed);

    @Transactional
    void deleteById(int id);

    

}
