package jsl.group.dispatcher_service.domain;

import java.util.Objects;

public record OrderDispatchMessage(
        Long orderId
) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderDispatchMessage that = (OrderDispatchMessage) o;
        return Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderId);
    }
}
