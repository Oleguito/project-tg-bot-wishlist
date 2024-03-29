package application.service.impl;

import application.service.MoviesService;
import domain.model.db.DBManager;
import domain.model.entity.MovieEntity;
import infrastructure.repository.MoviesRepo;
import infrastructure.repository.UsersMoviesRepo;

import java.util.List;

public class MoviesServiceImpl implements MoviesService {

    private final MoviesRepo moviesRepo;
    private final UsersMoviesRepo usersMoviesRepo;

    public MoviesServiceImpl() {
        
        this.moviesRepo = DBManager.getInstance().getMoviesRepo();
        this.usersMoviesRepo = DBManager.getInstance().getUsersMoviesRepo();
    }

    @Override

    public void saveMovie(MovieEntity movieEntity, Long chat_id) {
        moviesRepo.saveMovie(movieEntity, chat_id);

    }
    
    @Override
    public boolean movieRegisteredInUsersMovies(MovieEntity movie, Long chatId) {
        return usersMoviesRepo.movieRegistered(movie,chatId);
    }
    
    @Override
    public void deleteAllMoviesOfUserFromDatabase(Long chatId) {
        usersMoviesRepo.deleteMoviesOfUser(chatId);
    }

    @Override
    public List<MovieEntity> getMovies(Long chat_id) {
        return moviesRepo.getMovies(chat_id);
    }
}
