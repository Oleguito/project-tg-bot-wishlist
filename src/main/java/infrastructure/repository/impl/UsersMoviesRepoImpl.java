package infrastructure.repository.impl;

import infrastructure.repository.UsersMoviesRepo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import domain.model.entity.MovieEntity;
import domain.model.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import infrastructure.utils.SessionFactoryImpl;

import java.sql.Connection;
import java.util.List;

public class UsersMoviesRepoImpl implements UsersMoviesRepo {

    private final Connection connection;
    private final SessionFactory sessionFactory = SessionFactoryImpl.getInstance();

    public UsersMoviesRepoImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public boolean movieRegistered(MovieEntity movie, Long chatID) {

        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<UserEntity> criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);
        Root<UserEntity> root = criteriaQuery.from(UserEntity.class);
        root.join("movies");
        criteriaQuery.select(root).where(criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("id"), chatID),
                        criteriaBuilder.equal(root.get("movies").get("id"), movie.getId())
                )
        );

        Query<UserEntity> query = session.createQuery(criteriaQuery);
        List<UserEntity> resultList = query.getResultList();
        return resultList.size() > 0;

        // var result = false;
        // try {
        //     // String query = "insert into movies (id, ref, title, year) values (?, ?, ?, ?)";
        //     String query = "SELECT film_is_registered_by_user( ?, ? )";
        //
        //     PreparedStatement preparedStatement = SQLUtils.getPreparedStatement(query, connection);
        //     preparedStatement.setLong(1, movie.getId());
        //     preparedStatement.setLong(2, chatID);
        //     var resultSet = preparedStatement.executeQuery();
        //     resultSet.next();
        //
        //     result = resultSet.getBoolean(1);
        //
        //     preparedStatement.close();
        //
        // } catch (SQLException e) {
        //     throw new RuntimeException("Произошла ошибка выполнения запроса. Информация: " + e.getMessage());
        // }
        //  return result;
    }

    @Override
    public void deleteMoviesOfUser(Long chatId) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        MutationQuery nativeMutationQuery = session.createNativeMutationQuery("delete from users_movies um where um.user_id = :param");
        nativeMutationQuery.setParameter("param", chatId);
        nativeMutationQuery.executeUpdate();
        session.getTransaction().commit();

//        try {
//
//            String query = """
//                    delete from users_movies
//                    where user_id = ?
//                    """;
//
//            PreparedStatement preparedStatement = SQLUtils.getPreparedStatement(query, connection);
//            preparedStatement.setLong(1, chatId);
//            preparedStatement.execute();
//
//            preparedStatement.close();
//
//        } catch (SQLException e) {
//            throw new RuntimeException("Произошла ошибка выполнения запроса", e);
//        }

    }
}
