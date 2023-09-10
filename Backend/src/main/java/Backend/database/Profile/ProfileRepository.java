package Backend.database.Profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findById(long id);
    Boolean existsByUsername(String username);
    Profile findByUsername(String username);
    long deleteByUsername(String username);

    @Transactional
    void deleteById(int id);

}
