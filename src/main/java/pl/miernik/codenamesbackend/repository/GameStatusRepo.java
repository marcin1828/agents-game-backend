package pl.miernik.codenamesbackend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.miernik.codenamesbackend.data.GameStatus;

@Repository
public interface GameStatusRepo extends CrudRepository<GameStatus, Long> {

    GameStatus findAllByToken(String Token);

}
