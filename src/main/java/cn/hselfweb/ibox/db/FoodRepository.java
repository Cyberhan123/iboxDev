package cn.hselfweb.ibox.db;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface FoodRepository extends Repository<Food,Long> {
    Food getAllByFoodId(Long foodId);
}
