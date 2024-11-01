package forOlderJava.absurdityAppForJava.domain.order.service.response;

import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import forOlderJava.absurdityAppForJava.domain.order.entity.OrderStatus;
import forOlderJava.absurdityAppForJava.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final OrderRepository orderRepository;
    private final RedisTemplate<String, Long> orderCountRedisTemplate;
    private final ListOperations<String, String> listOperations;

    public Long getTotalNumberOfOrdersByMemberId(final Long memberId, final String cacheKey) {

        Long cachedCount = orderCountRedisTemplate.opsForValue().get(cacheKey);

        if (cachedCount != null) {
            return cachedCount;
        }

        long dbCount = orderRepository.countByMember_Id(memberId);
        orderCountRedisTemplate.opsForValue().set(cacheKey, dbCount + 1);

        return dbCount;
    }

    public double getAverageRatingByItemId(final Long itemId, final String cacheKey) {

        String averageRating = listOperations.index(cacheKey, 0);

        if (averageRating != null) return Double.parseDouble(averageRating);

        Double dbAverageRating = orderRepository.findAverageRatingByItemId(itemId);
        Long numberOfOrder = orderRepository.countByItem_Id(itemId);

        listOperations.rightPushAll(cacheKey, String.valueOf(dbAverageRating), String.valueOf(numberOfOrder));

        return dbAverageRating;

    }

    public void increaseOrderCount(final Long memberId, final String cachedKey) {
        Long cachedCount = orderCountRedisTemplate.opsForValue().get(cachedKey);

        if (cachedCount != null) {
            orderCountRedisTemplate.opsForValue().set(cachedKey, cachedCount + 1);
            return;
        }
        long dbCount = orderRepository.countByMember_Id(memberId);
        orderCountRedisTemplate.opsForValue().set(cachedKey, dbCount + 1);
    }

    public double getCompletionRate(final Long memberId, final String cacheKey) {

        String cachedRate = listOperations.index(cacheKey, 0);

        if (cachedRate != null) return Double.parseDouble(cachedRate);

        List<OrderStatus> completedStatus = List.of(OrderStatus.COME_OUT);

        double dbCompletionRate = orderRepository.findCompletionRateByMemberId(memberId, completedStatus);
        Long totalOrders = orderRepository.countByMember_Id(memberId);

        listOperations.rightPushAll(cacheKey, String.valueOf(dbCompletionRate), String.valueOf(totalOrders));
        return dbCompletionRate;
    }

    public void updateCompletionRate(final Long memberId, final String cacheKey, final boolean isCompleted) {

        String completionRate = listOperations.index(cacheKey, 0);
        String totalOrders = listOperations.index(cacheKey, 1);

        if (completionRate != null && totalOrders != null) {
            long currentTotal = Long.parseLong(totalOrders);
            double currentRate = Double.parseDouble(completionRate);

            double newRate = calculateNewCompletionRate(currentRate, currentTotal, isCompleted);

            listOperations.set(cacheKey, 0, String.valueOf(newRate));
            listOperations.set(cacheKey, 1, String.valueOf(currentTotal + 1));
        } else {
            synchronizeCompletionRate(memberId, cacheKey);
        }
    }

    public void synchronizeCompletionRate(final Long memberId, final String cacheKey) {

        List<OrderStatus> completedStatus = List.of(OrderStatus.COME_OUT);

        double dbCompletionRate = orderRepository.findCompletionRateByMemberId(memberId, completedStatus);
        long dbTotalOrders = orderRepository.countByMember_Id(memberId);

        listOperations.rightPushAll(cacheKey, String.valueOf(dbCompletionRate), String.valueOf(dbTotalOrders));
    }

    private double calculateNewCompletionRate(double currentRate, long totalOrders, boolean isCompleted) {
        long completedOrders = (long) (currentRate * totalOrders);
        if (isCompleted) completedOrders++;
        return Math.round((double) completedOrders / (totalOrders + 1) * 100) / 100.0;
    }
}
